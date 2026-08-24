# Using Java with kdb+

## Quickstart

Javakdb is the original Java driver, a.k.a `c.java`, from KX for interfacing [Java](https://www.java.com/en/) with kdb+ via TCP/IP. This driver allows Java applications to

 - query kdb+
 - subscribe to a kdb+ publisher
 - publish to a kdb+ consumer 

using a straightforward and compact API. The four methods of the single class `c` of immediate interest are

method    | purpose
--------- | --------
`c`.      | the constructor
`c.ks`    | send an async message
`c.k`.    | send a sync message
`c.close` | close the connection

To establish a connection to a kdb+ process listening on the localhost on port 12345, invoke the relevant constructor of the `c` class

```java
 c c=new c("localhost",12345,System.getProperty("user.name")+":mypasswordhere");
```

A KException will be thrown if the kdb+ process rejects the connection attempt.

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

And to close a connection once it is no longer needed:

```java
c.close();
```

> **Closing unused connections**
> 
> Closing unused connections is important to help avoid unnecessary resource usage on the remote process.

The Java driver is effectively a data marshaller between Java and kdb+: sending an object to kdb+ typically results in kdb+ evaluating that object in some manner. The default message handlers on the kdb+ side are initialized to the kdb+ `value` operator, which means they will evaluate a string expression, e.g.

```java
c.k("2+3")
```

or a list of (function; arg0; arg1; ...; argN), e.g.

```java
c.k(new Object[]{'+',2,3})
```

Usually when querying a database, one would receive a table as a result. This is indeed the common case with kdb+, and a table is represented in this Java interface as the `c.Flip` class. A flip has an array of column names, and an array of arrays containing the column data.

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

```csv
sym,time,price,size
MSFT,15:39:23.746172000,320.2,100
GOOG,15:39:23.746172001,120.1,300
```

A keyed table is represented as a dictionary where both the key and the value of the dictionary are flips themselves. To obtain a table without keys from a keyed table, use the `c.td(d)` method. In the example below, note that the table is created with `sym` as the key, and the table is unkeyed using `c.td`.

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

## Maximum Message Size

The maximum transmissible message size is 2GB due to a limitation with the maximum array size in Java, therefore [capability 3](https://code.kx.com/q/basics/ipc/#handshake) will be used within the kdb+ handshake.

## Type mapping

Kdb+ types are mapped to and from Java types by this driver, and the example [`TypesMapping.java`](examples.md#typesmapping) demonstrates the construction of atoms, vectors, a dictionary, and a table, sending them to kdb+ for echo back to Java, for comparison with the original type and value. 

The output is recorded here for clarity, which correspond to the [`Kdb+ data types`](https://code.kx.com/q/basics/datatypes/):

|            Java type|            kdb+ type|                            value sent|                            kdb+ value|
|--------------------:|--------------------:|-------------------------------------:|-------------------------------------:|
|     java.lang.Object|             (0) list|                                      |                                      |
|    java.lang.Boolean|          (-1)boolean|                                  true|                                    1b|
|            boolean[]|    (1)boolean vector|                                  true|                                   ,1b|
|       java.util.UUID|             (-2)guid|  f5889a7d-7c4a-4068-9767-a009c8ac46ef|  f5889a7d-7c4a-4068-9767-a009c8ac46ef|
|     java.util.UUID[]|       (2)guid vector|  f5889a7d-7c4a-4068-9767-a009c8ac46ef| ,f5889a7d-7c4a-4068-9767-a009c8ac46ef|
|       java.lang.Byte|             (-4)byte|                                    42|                                  0x2a|
|               byte[]|       (4)byte vector|                                    42|                                 ,0x2a|
|      java.lang.Short|            (-5)short|                                    42|                                   42h|
|              short[]|      (5)short vector|                                    42|                                  ,42h|
|    java.lang.Integer|              (-6)int|                                    42|                                   42i|
|                int[]|        (6)int vector|                                    42|                                  ,42i|
|       java.lang.Long|             (-7)long|                                    42|                                    42|
|               long[]|       (7)long vector|                                    42|                                   ,42|
|      java.lang.Float|             (-8)real|                                 42.42|                                42.42e|
|              float[]|       (8)real vector|                                 42.42|                               ,42.42e|
|     java.lang.Double|            (-9)float|                                 42.42|                                 42.42|
|              doube[]|      (9)float vector|                                 42.42|                                ,42.42|
|  java.lang.Character|            (-10)char|                                     a|                                   "a"|
|               char[]|      (10)char vector|                                     a|                                  ,"a"|
|     java.lang.String|          (-11)symbol|                                    42|                               &#96;42|
|   java.lang.String[]|    (11)symbol vector|                                    42|                              ,&#96;42|
|    java.time.Instant|       (-12)timestamp|               2017-07-07 15:22:38.976|         2017.07.07D15:22:38.976000000|
|  java.time.Instant[]| (12)timestamp vector|               2017-07-07 15:22:38.976|        ,2017.07.07D15:22:38.976000000|
|           kx.c.Month|           (-13)month|                               2000-12|                              2000.12m|
|         kx.c.Month[]|     (13)month vector|                               2000-12|                             ,2000.12m|
|   java.time.LocalDate|            (-14)date|                            2017-07-07|                            2017.07.07|
| java.time.LocalDate[]|      (14)date vector|                            2017-07-07|                           ,2017.07.07|
|   java.util.LocalDateTime|        (-15)datetime|    Fri Jul 07 15:22:38 GMT+03:00 2017|               2017.07.07T15:22:38.995|
| java.util.LocalDateTime[]|  (15)datetime vector|    Fri Jul 07 15:22:38 GMT+03:00 2017|              ,2017.07.07T15:22:38.995|
|        kx.c.Timespan|        (-16)timespan|                    15:22:38.995000000|                  0D15:22:38.995000000|
|      kx.c.Timespan[]|  (16)timespan vector|                    15:22:38.995000000|                 ,0D15:22:38.995000000|
|          kx.c.Minute|          (-17)minute|                                 12:22|                                 12:22|
|        kx.c.Minute[]|    (17)minute vector|                                 12:22|                                ,12:22|
|          kx.c.Second|          (-18)second|                              12:22:38|                              12:22:38|
|        kx.c.Second[]|    (18)second vector|                              12:22:38|                             ,12:22:38|
|  java.time.LocalTime|            (-19)time|                              15:22:38|                          15:22:38.995|
| java.time.LocalTime[]|      (19)time vector|                              15:22:38|                         ,15:22:38.995|
|              .c.Flip|            (98)table|                                      |                                      |
|              .c.Dict|       (99)dictionary|                                      |                                      |

### Null types

#### Creating null values

The reference for data type suffix and numerical index can be found [here](https://code.kx.com/q/basics/datatypes/).
For example, the type suffix for `int` is `i` with a numerical index of `6`.

For each type suffix, "hijefcspmdznuvt", we can get a reference to a null q value by by using the `NULL` utility method. 
An example of creating an object array containing a null integer and a null long:

```java
Object[] twoNullIntegers = {c.NULL('i'), c.NULL('j')}; // i - int, j - long
```

We can also  get a reference to a null q value by indexing into the `NULL` Object array.

```java
Object[] twoNullIntegers = {c.NULL[6], c.NULL[7]}; // i - int, j - long
```

Note the q null values are not the same as Java’s null.

#### Testing for null

An object can be tested where it is a q null using the `c` utility method

```java
public static boolean qn(Object x);
```

### GUID

The globally unique identifier (GUID) type was introduced into kdb+ with
version 3.0 for the purpose of storing arbitrary 16-byte values, such as
transaction IDs. Storing such values in this form allows for savings in
tasks such as memory and storage usage, as well as improved performance
in certain operations such as table lookups when compared with standard
types such as Strings.

Java has its own unique identifier type: `java.util.UUID` (universally
unique identifier). In the API the kdb+ GUID type maps directly to this
object through the extraction and provision of its most and least
significant long values. Otherwise, the only high-level difference in
how this type can be used when compared to other types handled by the
API is that a `RuntimeException` will be thrown if an attempt is made to
serialize and pass a UUID object to a kdb+ instance with a version lower
than 3.0.

### Dictionaries and tables  

Kdb+ dictionaries (type 99) and tables (type 98) are represented by the
internal classes Dict and Flip respectively. 

The Dict class consists of two public `java.lang.Object` fields (`x` for keys, `y` for
values) and a basic constructor, which allows any of the represented
data types to be used. However, while from a Java perspective any object
could be passed to the constructor, dictionaries in q are always
structured as two lists. This means that if the object is being created
to pass to a q session directly, the Object fields in a Dict object
should be assigned arrays of a given representative type, as passing in
an atomic object will result in an error.

For example, the first of the following dictionary instantiation is
legal with regards to the Java object, but because the pairs being
passed in are atomic, it would signal a type error in q. Instead, the
second example should be used, and can be seen as mirroring the practice
of enlisting single values in q:
```java
new c.Dict("Key","Value"); // not q-compatible
new c.Dict(new String[] {"Key"}, new String[] {"Value"}); // q-compatible
```
As the logical extension of that, in order to represent a list as a
single key or pair, multi-dimensional arrays should be used:
```java
new c.Dict(new String[] {"Key"}, new String[][] {{"Value1","Value2","Value3"}});
```
Flip (table) objects
consist of a String array for columns, an Object array for values, a
constructor and a method for returning the Object array for a given
column. The constructor takes a dictionary as its parameter, which is
useful for the conversion of one to the other should the dictionary in
question consist of single symbol keys. Of course, with the fields of
the class being public, the columns and values can be assigned manually.

Keyed tables in q are dictionaries in terms of type, and therefore will
be represented as a Dict object in Java. The method
`td(Object)`
will create a Flip object from a keyed table Dict, but will remove its
keyed nature in the process

## Timezone

For global data capture, it is common practice to store events using a GMT timestamp. To minimize confusion, it is easiest to set the current timezone to GMT, either explicitly in the `c` class as 

```java
c.tz=TimeZone.getTimeZone("GMT");
```

or from the environment, e.g.

```bash
export TZ=GMT;...
```

otherwise kdb+ will use the default timezone from the environment, and adjust values between local and GMT during serialization.


## Message types

There are three message types in kdb+

|Msg Type|Description|
|--------|-----------|
|   async| send via `c.ks(…)`. This call blocks until the message has been fully sent. There is no guarantee that the server has processed this message by the time the call returns.|
|    sync| send via `c.k(…)`. This call blocks until a response message has been received, and returns the response which could be either data or an error.|
|response| this should _only_ ever be sent as a response to a sync message. If your Java process is acting as a server, processing incoming sync messages, a response message can be sent with `c.kr(responseObject)`. If the response should indicate an error, use `c.ke("error string here")`.|

If `c.k()` is called with no arguments, the call will block until a message is received of _any_ type. This is useful for subscribing to a tickerplant, to receive incoming async messages published by the ticker plant.


## Sending sync/async messages 

The methods for sending sync/async messages are overloaded as follows:

-   Methods which send async messages do not return a value:

```java
public void ks(String s) throws IOException 
public void ks(String s, Object x) throws IOException
public void ks(String s, Object x, Object y) throws IOException
public void ks(String s, Object x, Object y, Object z) throws IOException
```

-   Methods which send sync messages return an Object, the result from the remote processing the sync message:

```java
public Object k(Object x) throws KException, IOException
public Object k(String s) throws KException, IOException
public Object k(String s, Object x) throws KException, IOException
public Object k(String s, Object x, Object y) throws KException, IOException
public Object k(String s, Object x, Object y, Object z) throws KException, IOException
```
-   If no argument is given, the `k` call will block until a message is received, deserialized to an Object. It is used by the c synchronous methods in order to capture and return response objects, and is also used in server-oriented applications in order to capture incoming messages from client processes.

```java 
public Object k() throws KException, IOException
```

## Exceptions

The `c` class throws IOExceptions for network errors such as read/write failures and throws KExceptions for higher-level cases, such as remote execution errors arising during the query at hand.

## Accessing items of lists

List items can be accessed using the `at` method of the utility class `c`:

```java
Object c.at(Object x, int i) // Returns the object at x[i] or null
```

and set them with `set`:

```java
void c.set(Object x, int i, Object y) // Set x[i] to y, or the appropriate q null value if y is null
```

## SSL/TLS

Secure, encrypted connections may be established using SSL/TLS, by specifying the `useTLS` argument to the `c` constructor as true, e.g.

```java
c c=new c("localhost",12345,System.getProperty("user.name"),true);
```

The kdb+ process [must be enabled](https://code.kx.com/q/kb/ssl) to accept TLS connections.

Prior to using SSL/TLS, ensure that the required certificates and keys have been imported into your keystore. 
The Java [keytool](https://docs.oracle.com/javase/10/tools/keytool.htm) application provides the facility to manage certificates/keys/etc.

Consult the [JSSE reference guide](https://docs.oracle.com/en/java/javase/11/security/java-secure-socket-extension-jsse-reference-guide.html) for details on configuring a Java application for SSL/TLS communication.

### Example

This example uses the certificates and keys generated from the following [script](https://code.kx.com/q/kb/ssl/#checking-configuration).
Passwords used are for example only.

The private key (`client-private-key.pem`) and client certificate (`client-cert.pem`) can be converted to PKCS12 format using OpenSSL. 
The folling command is used to create a keystore file `keystore.p12`. When prompted for a password, we will use `kdbkdbpass`.
```bash
openssl pkcs12 -export -inkey client-private-key.pem -in client-cert.pem -out keystore.p12 -name client-alias
```

Convert the CA certificate to a Java Truststore (JKS format). The example will use `ca-cert.pem` to generate the truststore file `truststore.jks` with a password `changeit`.
```bash
keytool -importcert -trustcacerts -file ca-cert.pem  -keystore truststore.jks -storepass changeit -alias ca-alias
```

When running the application, set the standard Java JSSE properties so the SSL/TLS connection has access to the keystores, for example:
```bash
mvn exec:java -pl javakdb-examples -Dexec.mainClass="com.kx.examples.TypesMapping" -Djavax.net.ssl.keyStore=keystore.p12 -Djavax.net.ssl.keyStorePassword=kdbkdbpass -D=javax.net.ssl.trustStore=truststore.jks -D=javax.net.ssl.trustStorePassword=changeit
```

To troubleshoot, supply `-Djavax.net.debug=ssl` on the command line when invoking your Java application.

## UDS (unix domain sockets)

kdb+ can use UDS for comms, see [here](https://code.kx.com/q/basics/listening-port/#unix-domain-socket) for details.

Java ipc requires java version 16 or greater, OS support & client/server residing on same machine.
Java reference [here](https://inside.java/2021/02/03/jep380-unix-domain-sockets-channels/)

example of client connection when kdb+ listening on 5010
```java
c=new c("/tmp/kx.5010",System.getProperty("user.name")+":mypassword");
```

example of creating server when kdb+ connecting with h:hopen`:unix://1234
```java
java.net.UnixDomainSocketAddress address = java.net.UnixDomainSocketAddress.of("/tmp/kx.1234");
ServerSocketChannel serverChannel = ServerSocketChannel.open(java.net.StandardProtocolFamily.UNIX);
serverChannel.bind(address);
// pass serverChannel to c contructor to wait til new client connection occurs
```

## IPv4 / IPv6

Using IPv4 or IPv6 networking with Java is automatic for both server and client instances. Its operation is influenced by Java system properties.

Reference: [Java Networking Properties](https://docs.oracle.com/javase/8/docs/api/java/net/doc-files/net-properties.html), [Java Networking IPv6 User Guide](https://docs.oracle.com/javase/6/docs/technotes/guides/net/ipv6_guide/index.html)

For example, connecting to an IPv6 address can be as follows:
```java
c=new c("fd31:1624:3d00:3908:480:d528:7d7b:bf03",5010);
```

Hostnames can resolve to IPv4 or IPv6 address based on the Java system properies and whether the hostname can be resolved to an IPv4 or IPv6 address.

For example, connecting to an hostname that only resolves to an IPv6 address, while setting the system property `java.net.preferIPv4Stack=true` can throw a `java.net.SocketException`.
Likewise, when creating a server socket with the API on a host with IPv6 support, setting `java.net.preferIPv4Stack=true` prevents the port from also listening on IPv6 (preventing client connections via IPv6).

## Server instance

It is also possible to set up the object to accept incoming connections
from kdb+ processes rather than just making them. There are two
constructors which, when passed a server socket reference, will allow a
q session to establish a handle against the `c` object:
```java
public c(ServerSocket s)
public c(ServerSocket s,IAuthenticate a)
```
`IAuthenticate` is an interface within the `c` class that can be
implemented to emulate kdb+ server-side authentication, allowing the
establishment of authentication rules similar to that which might be
done through the q function [`.z.pw`](https://code.kx.com/q/ref/dotz#zpw-validate-user).
