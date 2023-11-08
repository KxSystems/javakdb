![Java](docs/java.jpg)

# javakdb

![Travis (.com) branch](https://img.shields.io/travis/com/kxsystems/javakdb/master) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=KxSystems_javakdb&metric=coverage)](https://sonarcloud.io/dashboard?id=KxSystems_javakdb) <a href="https://sonarcloud.io/dashboard?id=KxSystems_javakdb"><img src="https://sonarcloud.io/images/project_badges/sonarcloud-white.svg" width="125"></a>


KDB+ IPC interface for the Java programming language. This will allow your application to

- query kdb+
- subscribe to a kdb+ publisher
- publish to a kdb+ consumer
- serialize/deserialize kdb+ formatted data
- act as a server for a kdb+ instance

## Releases

Latest release can be downloaded [here](https://github.com/KxSystems/javakdb/releases). The github master branch will contain the latest development version for testing prior to release (may contain planned major version changes).

## Documentation

:open_file_folder: Documentation is in the [`docs`](docs/README.md) folder.

## Building from source

Java 1.8 (and above) is recommended. Please ensure that your `JAVA_HOME` environment variable is set to the version of Java you have installed (or the one preferred if you have multiple versions).

You will also need [Apache Maven](https://maven.apache.org/) installed. Run the following to check you have it set up and configured correctly

```bash
mvn -version
```

In order to build the library, run the following within the directory where the `pom.xml` file is located (from the downloaded source).

```bash
mvn clean compile
```

If you wish to deploy the library to your machines local repository, in order to be used by other maven projects on your machine, run the following

```bash
mvn clean install
```

Please refer to the [Apache Maven documentation](https://maven.apache.org/guides/index.html) for further details

## Code examples

Supplied with the code is a series of code examples. The following describes each with an example of how to run from Maven (note: Maven is not required to run the applications, but used here for convenience). 

`mvn clean install` should be performed prior to running.

### GridViewer

Creates a Swing GUI that presents the contents of a KDB+ table (Flip).  It shows the mapping of the Flip class to a Swing TableModel. The contents of the table are some random data that we instruct KDB+ to generate.

Prerequisite: 

- a kdb+ server running on port 5010 on your machine i.e. `q -p 5010`


Run command:

```bash
mvn exec:java -pl javakdb-examples -Dexec.mainClass="com.kx.examples.GridViewer"
```

### QueryResponse

Instructs the remote kdb+ process to execute q code (kdb+ native language) and receives the result. The same principle can be used to execute q functions. Example of a sync request.

Prerequisite:

- a kdb+ server running on port 5010 on your machine i.e. `q -p 5010`


Run command:

```bash
mvn exec:java -pl javakdb-examples -Dexec.mainClass="com.kx.examples.QueryResponse"
```


### SerializationOnly

Example of code that can be used to serialize/deserialize a Java type (array of ints) to kdb+ format. 

Run command:

```bash
mvn exec:java -pl javakdb-examples -Dexec.mainClass="com.kx.examples.SerializationOnly"
```

### Server

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

### Feed

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

### TypesMapping

Example app that creates each of the kdb+ types in Java, and communicates with kdb+ to check that the type has been correctly matched with its q type (kdb+ default language). Prints the Java type and corresponding q type.

Prerequisite: 

- a kdb+ server running on port 5010 on your machine i.e. `q -p 5010`.


Run command:

```bash
mvn exec:java -pl javakdb-examples -Dexec.mainClass="com.kx.examples.TypesMapping"
```

### Subscriber

Example app that subscribes to real-time updates from a table that is maintained in kdb+. 

Prerequisite: 

- a kdb+ server running on port 5010 on your machine. The instance must have the `.u.sub` function defined. An example of `.u.sub` can be found in [KxSystems/kdb-tick](https://github.com/KxSystems/kdb-tick), an example tickerplant. You can execute this tickerplant process by running `q tick.q` (the default port is set to 5010).

Run command:

```bash
mvn exec:java -pl javakdb-examples -Dexec.mainClass="com.kx.examples.Subscriber"
```


