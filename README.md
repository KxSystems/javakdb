[![N|Solid](https://avatars2.githubusercontent.com/u/11446750?v=3&s=200)](https://kx.com)
# javakdb

javakdb is the original Java driver, a.k.a c.java, from Kx Systems for interfacing Java with kdb+ via tcp/ip. kdb+ is a database with a builtin programming and query langauge, q. This driver allows Java applications to
 - query kdb+
 - subscribe to a kdb+ publisher
 - publish to a kdb+ consumer 

using a straightforward and compact api. The 4 methods of the single class "c" which are of immediate interest are

 - the constructor, c
 - send an async message, c.ks
 - send a sync message, c.k
 - close the connection, c.close

To establish a connection to a kdb+ process which is listening on the localhost on port 12345, invoke the relevant constructor of the c class

```java
 c c=new c("localhost",12345,System.getProperty("user.name"));
```

Then, to issue a query and read the response, use

```java
Object result=c.k("2+3");
System.out.println("result is "+result); // expect to see 5 printed
```

or to subscribe to a kdb+ publisher, here kdb+tick, use
```java
  c.k(".u.sub","mytable",x);
  while(true)
    System.out.println("Received "+c.k());
```
or to publish to a kdb+ consumer, here a kdb+ ticker plant, use

```java
// Assuming a remote schema of
// mytable:([]time:`timespan$();sym:`symbol$();price:`float$();size:`long$())
Object[]row={new c.Timespan(),"SYMBOL",new Double(93.5),new Long(300)};
c.k(".u.upd","mytable",row);
```
And to finally close a connection once it is no longer needed, use

```java
c.close();
```

The Java driver is effectively a data marshaller between Java and kdb+; sending an object to kdb+ typically results in kdb+ evaluating that object in some manner. The default message handlers on the kdb+ side are initialized to the kdb+ value operator, which means they will evaluate a string expression, e.g.
```java
c.k("2+3")
```
or a list of (function; arg0; arg1; ...; argN), e.g.
```java
c.k(new Object[]{'+',2,3})
```

Usually when querying a database, one would receive a table as a result. This is indeed the common case with kdb+, and a table is represented in this java interface as the c.Flip class. A flip has an array of column names, and an array of arrays containing the column data.

The following is example code to iterate over a flip, printing each row to the console.

```java
c.Flip flip=(c.Flip)c.k("([]sym:`MSFT`GOOG;time:0 1+.z.n;price:320.2 120.1;size:100 300)");
for(int col=0;col<flip.x.length;col++)
  System.out.print((col>0?",":"")+flip.x[col]);
System.out.println();
for(int row=0;row<n(flip.y[0]);row++){
  for(int col=0;col<flip.x.length;col++)
    System.out.print((col>0?",":"")+c.at(flip.y[col],row));
    System.out.println();
}
```

resulting in the following printing at the console

```
sym,time,price,size
MSFT,15:39:23.746172000,320.2,100
GOOG,15:39:23.746172001,120.1,300
```

A keyed table is represented as a dictionary where both the key and the value of the dictionary are flips themselves. To obtain a table without keys from a keyed table, use the c.td(d) method. In the example below, note that the table is created with sym as the key, and the table is unkeyed using c.td.

```java
c.Flip flip=c.td(c.k("([sym:`MSFT`GOOG]time:0 1+.z.n;price:320.2 120.1;size:100 300)"));
```

To create a table to send to kdb+, first construct a flip of a dictionary of column names with a list of column data. e.g.

```java
c.Flip flip=new c.Flip(new c.Dict(
  new String[]{"time","sym","price","volume"},
  new Object[]{new c.Timespan[]{new c.Timespan(),new c.Timespan()},
               new String[]{"ABC","DEF"},
               new double[]{123.456,789.012},
               new long[]{100,200}}));
```

and then send it via a sync or async message

```java
Object result=c.k("{x}",flip); // a sync msg, echos the flip back as result
```

## Type Mapping
kdb+ types are mapped to and from java types by this driver, and the example src/kx/examples/TypesMapping.java demonstrates the construction of atoms, vectors, a dictionary, and a table, sending them to kdb+ for echo back to java, for comparison with the original type and value. The output is recorded here for clarity:

|            Java Type|            kdb+ Type|                            Value Sent|                            kdb+ Value|Match|
|---------------------|---------------------|--------------------------------------|--------------------------------------|-----|
|    java.lang.Boolean|          (-1)boolean|                                  true|                                    1b| true|
|                   [Z|    (1)boolean vector|                                  true|                                   ,1b| true|
|       java.util.UUID|             (-2)guid|  f5889a7d-7c4a-4068-9767-a009c8ac46ef|  f5889a7d-7c4a-4068-9767-a009c8ac46ef| true|
|     [Ljava.util.UUID|       (2)guid vector|  f5889a7d-7c4a-4068-9767-a009c8ac46ef| ,f5889a7d-7c4a-4068-9767-a009c8ac46ef| true|
|       java.lang.Byte|             (-4)byte|                                    42|                                  0x2a| true|
|                   [B|       (4)byte vector|                                    42|                                 ,0x2a| true|
|      java.lang.Short|            (-5)short|                                    42|                                   42h| true|
|                   [S|      (5)short vector|                                    42|                                  ,42h| true|
|    java.lang.Integer|              (-6)int|                                    42|                                   42i| true|
|                   [I|        (6)int vector|                                    42|                                  ,42i| true|
|       java.lang.Long|             (-7)long|                                    42|                                    42| true|
|                   [J|       (7)long vector|                                    42|                                   ,42| true|
|      java.lang.Float|             (-8)real|                                 42.42|                                42.42e| true|
|                   [F|       (8)real vector|                                 42.42|                               ,42.42e| true|
|     java.lang.Double|            (-9)float|                                 42.42|                                 42.42| true|
|                   [D|      (9)float vector|                                 42.42|                                ,42.42| true|
|  java.lang.Character|            (-10)char|                                     a|                                   "a"| true|
|                   [C|      (10)char vector|                                     a|                                  ,"a"| true|
|     java.lang.String|          (-11)symbol|                                    42|                                   `42| true|
|   [Ljava.lang.String|    (11)symbol vector|                                    42|                                  ,`42| true|
|   java.sql.Timestamp|       (-12)timestamp|               2017-07-07 15:22:38.976|         2017.07.07D15:22:38.976000000| true|
| [Ljava.sql.Timestamp| (12)timestamp vector|               2017-07-07 15:22:38.976|        ,2017.07.07D15:22:38.976000000| true|
|           kx.c$Month|           (-13)month|                               2000-12|                              2000.12m| true|
|         [Lkx.c$Month|     (13)month vector|                               2000-12|                             ,2000.12m| true|
|        java.sql.Date|            (-14)date|                            2017-07-07|                            2017.07.07| true|
|      [Ljava.sql.Date|      (14)date vector|                            2017-07-07|                           ,2017.07.07| true|
|       java.util.Date|        (-15)datetime|    Fri Jul 07 15:22:38 GMT+03:00 2017|               2017.07.07T15:22:38.995| true|
|     [Ljava.util.Date|  (15)datetime vector|    Fri Jul 07 15:22:38 GMT+03:00 2017|              ,2017.07.07T15:22:38.995| true|
|        kx.c$Timespan|        (-16)timespan|                    15:22:38.995000000|                  0D15:22:38.995000000| true|
|      [Lkx.c$Timespan|  (16)timespan vector|                    15:22:38.995000000|                 ,0D15:22:38.995000000| true|
|          kx.c$Minute|          (-17)minute|                                 12:22|                                 12:22| true|
|        [Lkx.c$Minute|    (17)minute vector|                                 12:22|                                ,12:22| true|
|          kx.c$Second|          (-18)second|                              12:22:38|                              12:22:38| true|
|        [Lkx.c$Second|    (18)second vector|                              12:22:38|                             ,12:22:38| true|
|        java.sql.Time|            (-19)time|                              15:22:38|                          15:22:38.995| true|
|      [Ljava.sql.Time|      (19)time vector|                              15:22:38|                         ,15:22:38.995| true|


##Message Types
There are 3 message types in kdb+
|Msg Type|Description|
|--------|-----------|
|   async| send via c.ks(...). This call blocks until the message has been fully sent. There is no guarantee that the server has processed this message by the time the call returns.|
|    sync| send via c.k(...). This call blocks until a response message has been received, and returns the response which could be either data or an error.|
|response| this should ONLY ever be sent as a response to a sync message. If you java process is acting as a server, processing incoming sync messages, a response message can be sent with c.kr(responseObject). If the response should indicate an error, use c.ke("error string here").|

If c.k() is called with no arguments, the call  will block until a message is received of ANY type. This is useful for subscribing to a tickerplant, to receive incoming async messages published by the ticker plant.
 
## SSL/TLS
Secure, encrypted connections may be established using SSL/TLS, by specifying useTLS argument to the c constructore as true. e.g.
```java
c c=new c("localhost",12345,System.getProperty("user.name"),true);
```
n.b. The kdb+ process must be enabled to accept TLS connections.

Prior to using SSL/TLS, ensure that the server's certificate has been imported into your keystore. e.g.
```
keytool -printcert -rfc -sslserver localhost:5010 > example.pem
keytool -importcert -file example.pem -alias example.com -storepass changeit -keystore ./keystore
java -Djavax.net.ssl.trustStore=./keystore -Djavax.net.ssl.keystore=./keystore kx.c
 ```
To troubleshoot ssl, supply -Djavax.net.debug=ssl on cmd line when invoking your Java application.
