# javakdb

![Travis (.com) branch](https://img.shields.io/travis/com/kxsystems/javakdb/master) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=KxSystems_javakdb&metric=coverage)](https://sonarcloud.io/dashboard?id=KxSystems_javakdb) <a href="https://sonarcloud.io/dashboard?id=KxSystems_javakdb"><img src="https://sonarcloud.io/images/project_badges/sonarcloud-white.svg" width="125"></a>

## Introduction

KDB+ IPC interface for the Java programming language. This will allow your application to

- query KDB+
- subscribe to a KDB+ publisher
- publish to a KDB+ consumer
- serialize/deserialize KDB+ formatted data

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