# java ipc examples for kdb+

Supplied with the code is a series of code examples. The following describes each with an example of how to run from Maven (note: Maven is not required to run the applications, but used here for convenience). 

`mvn clean install` should be performed prior to running.

## GridViewer

Creates a Swing GUI that presents the contents of a KDB+ table (Flip).  It shows the mapping of the Flip class to a Swing TableModel. The contents of the table are some random data that we instruct KDB+ to generate.

Prerequisite: 

- a kdb+ server running on port 5010 on your machine i.e. `q -p 5010`


Run command:

```bash
mvn exec:java -pl javakdb-examples -Dexec.mainClass="com.kx.examples.GridViewer"
```

## QueryResponse

Instructs the remote kdb+ process to execute q code (kdb+ native language) and receives the result. The same principle can be used to execute q functions. Example of a sync request.

Prerequisite:

- a kdb+ server running on port 5010 on your machine i.e. `q -p 5010`


Run command:

```bash
mvn exec:java -pl javakdb-examples -Dexec.mainClass="com.kx.examples.QueryResponse"
```


## SerializationOnly

Example of code that can be used to serialize/deserialize a Java type (array of ints) to kdb+ format. 

Run command:

```bash
mvn exec:java -pl javakdb-examples -Dexec.mainClass="com.kx.examples.SerializationOnly"
```

## Server

Creates a Java app that listens on TCP port 5010, which a kdb+ process can communicate with. It will echo back sync messages and discard async messages. The following is an example of running kdb+ from the same machine (i.e. running the q executable and typing commands at the q prompt) that will communicate with the Java server.

```q
q
q)h:hopen `::5010
q)h"hello"
q)neg[h]"hello"
```

Run command:

```bash
mvn exec:java -pl javakdb-examples -Dexec.mainClass="com.kx.examples.Server"
```

## Feed

Example of creating an update function remotely (to capture table inserts), along with table creation and population of the table.
Table population has an example of single-row inserts (lower latency) and bulk inserts (better throughput and resource utilization).

Prerequisites: 

- a kdb+ server running on port 5010 on your machine i.e. `q -p 5010`. 
- as this example depends on a `.u.upd` function being defined and a table name `mytable` pre-existing, you may wish to run the following within the kdb+ server (in normal environments, these table and function definitions should be pre-created by your kdb+ admin). 

```q
q).u.upd:{[tbl;row] insert[tbl](row)}
q)mytable:([]time:`timespan$();sym:`symbol$();price:`float$();size:`long$())
```


Run command:

```bash
mvn exec:java -pl javakdb-examples -Dexec.mainClass="com.kx.examples.Feed"
```

## TypesMapping

Example app that creates each of the kdb+ types in Java, and communicates with kdb+ to check that the type has been correctly matched with its q type (kdb+ default language). Prints the Java type and corresponding q type.

Prerequisite: 

- a kdb+ server running on port 5010 on your machine i.e. `q -p 5010`.


Run command:

```bash
mvn exec:java -pl javakdb-examples -Dexec.mainClass="com.kx.examples.TypesMapping"
```

## Subscriber

Example app that subscribes to real-time updates from a table that is maintained in kdb+. 

Prerequisite: 

- a kdb+ server running on port 5010 on your machine. The instance must have the `.u.sub` function defined. An example of `.u.sub` can be found in [KxSystems/kdb-tick](https://github.com/KxSystems/kdb-tick), an example tickerplant. You can execute this tickerplant process by running `q tick.q` (the default port is set to 5010).

Run command:

```bash
mvn exec:java -pl javakdb-examples -Dexec.mainClass="com.kx.examples.Subscriber"
```

