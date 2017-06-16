[![N|Solid](https://avatars2.githubusercontent.com/u/11446750?v=3&s=200)](https://kx.com)
# javakdb

javakdb is the original Java driver, a.k.a c.java, from Kx Systems for interfacing Java with kdb+ via tcp/ip. kdb+ is a database with a builtin programming and query langauge, q. This driver allows Java applications to
 - query kdb+
 - subscribe to a kdb+ publisher
 - publish to a kdb+ consumer 

using a straightforward and compact api.

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
Object[]row={new c.Timespan(),"SYMBOL",new Double(93.5),new Integer(300)};
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

