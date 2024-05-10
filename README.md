![Java](docs/java.jpg)

# javakdb

![Travis (.com) branch](https://img.shields.io/travis/com/kxsystems/javakdb/master) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=KxSystems_javakdb&metric=coverage)](https://sonarcloud.io/dashboard?id=KxSystems_javakdb) <a href="https://sonarcloud.io/dashboard?id=KxSystems_javakdb"><img src="https://sonarcloud.io/images/project_badges/sonarcloud-white.svg" width="125"></a>


kdb+ IPC interface for the Java programming language. This will allow your application to

- query kdb+
- subscribe to a kdb+ publisher
- publish to a kdb+ consumer
- serialize/deserialize kdb+ formatted data
- act as a server for a kdb+ instance

## Releases

Latest release can be downloaded [here](https://github.com/KxSystems/javakdb/releases). The github master branch will contain the latest development version for testing prior to release (may contain planned major version changes).

Releases are also available from the Maven Central Repo, at [https://central.sonatype.com/namespace/com.kx](https://central.sonatype.com/namespace/com.kx). A guide to integrating with your build system can be found [here](https://central.sonatype.org/consume/).

## Documentation

:point_right: Documentation is in the [`docs`](docs/README.md) folder.

## API Reference

HTML docs can be generated via running
```
mvn -pl javakdb javadoc:javadoc
```

## Building From Source

:point_right: [`Building guide`](docs/build.md)

## Code Examples

:point_right: [`Examples`](docs/examples.md)

