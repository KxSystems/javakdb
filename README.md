# javakdb

![Travis (.com) branch](https://img.shields.io/travis/com/kxsystems/javakdb/master) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=KxSystems_javakdb&metric=coverage)](https://sonarcloud.io/dashboard?id=KxSystems_javakdb) <a href="https://sonarcloud.io/dashboard?id=KxSystems_javakdb"><img src="https://sonarcloud.io/images/project_badges/sonarcloud-white.svg" width="125"></a>

## Introduction

KDB+ IPC interface for the Java programming language. This will allow your application to

- query KDB+
- subscribe to a KDB+ publisher
- publish to a KDB+ consumer
- serialize/deserialize KDB+ formatted data
- act as a server for a KDB+ instance

## Releases

Latest release can be downloaded [here](https://github.com/KxSystems/javakdb/releases). The github master branch will contain the latest development version for testing prior to release (may contain planned major version changes).

## Documentation

Documentation outlining the functionality available for this interface can be found [here](https://code.kx.com/v2/interfaces/java-client-for-q/).

## Building from Source

Java 1.8 (and above) is recommended. Please ensure that your `JAVA_HOME` environment variable is set to the version of Java you have installed (or the one preferred if you have multiple versions).

You will also need [Apache Maven](https://maven.apache.org/) installed. Run the following the check you have it setup and configured correctly

`mvn -version`

In order to build the library, run the following within the directory where the pom.xml file is located (from the downloaded source).

`mvn clean compile`

If you wish to deploy the library to your machines local repository, in order to be used by other maven projects on your machine, run the following

`mvn clean install`

Please refer to the [Apache Maven documentation](https://maven.apache.org/guides/index.html) for further details

## Code Examples

Supplied with the code is a series of code examples. The following describes each with an example of how to run from Maven (note: Maven is not required to run the applications, but used here for convenience).

### GridViewer

Creates a Swing GUI that presents the contents of a KDB+ table (Flip).  It shows the mapping of the Flip class to a Swing TableModel. The contents of the table are some random data that we instruct KDB+ to generate.

Prerequisite: 

- a KDB+ server running on port 5001 on your machine i.e. q -p 5001


Run command:

- `mvn exec:java -Dexec.mainClass="kx.examples.GridViewer"`

### QueryResponse

Instructs the remote KDB+ process to execute 'q' code (KDB+ native language) & receives the result. The same principle can be used to execute q functions. Example of a sync request.

Prerequisite:

- a KDB+ server running on port 5001 on your machine i.e. q -p 5001


Run command:

- `mvn exec:java -Dexec.mainClass="kx.examples.QueryResponse"`

### SerializationOnly

Example of code that can be used to serialize/dezerialise a Java type (array of ints) to KDB+ format. 

Run command:

- `mvn exec:java -Dexec.mainClass="kx.examples.SerializationOnly"`

### Server

Creates a Java apps that listens on TCP port 5010, which a KDB+ process can communicate with. It will echo back sync messages & discard async messages. The following is an example of running KDB+ from the same machine (i.e. running the q executable and typing commands at the q prompt) that will communicate with the Java server.
```q
q
q)h:hopen `::5010
q)h"hello"
q)neg[h]"hello"
```

Run command

- `mvn exec:java -Dexec.mainClass="kx.examples.Server"`

### Feed

Example of creating an update function remotely (to capture table inserts), along with table creation and population of the table.
Table population has an example of single row inserts (lower latency) and bulk inserts (better throughput and resource utilization).

Prerequisite: 

- a KDB+ server running on port 5010 on your machine i.e. q -p 5010. 

- as this example depends on a .u.upd function being defined and a table name 'mytable' pre-existing, you may wish to run the following within the KDB+ server (in normal environments, these table and function definitions should be pre-created by your KDB+ admin). 

  ```q
  q).u.upd:{[tbl;row] insert[tbl](row)}
  q)mytable:([]time:`timespan$();sym:`symbol$();price:`float$();size:`long$())
  ```


Run command

- `mvn exec:java -Dexec.mainClass="kx.examples.Feed"`

### TypesMapping

Example app that creates each of the KDB+ types in Java, and communicates with KDB+ to check that the type has been correctly matched with its 'q' type (KDB+ default language). Prints the Java type and corresponding 'q' type.

Prerequisite: 

- a KDB+ server running on port 5010 on your machine i.e. q -p 5010


Run command:

- `mvn exec:java -Dexec.mainClass="kx.examples.TypesMapping"`

### Subscriber

Example app that subscribes to real-time updates from a table that is maintained in KDB+. 

Prerequisite: 

- a KDB+ server running on port 5010 on your machine. The instance must have the .u.sub function defined. An example of .u.sub can be found in <a href="https://github.com/KxSystems/kdb-tick">KxSystems/kdb-tick</a> which is an example tickerplant. You can execute this tickerplant process by running `q tick.q` (the default port is set to 5010).

Run command:

- `mvn exec:java -Dexec.mainClass="kx.examples.Subscriber"`
