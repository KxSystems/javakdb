# Building Java kdb+ ipc interface from source

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
