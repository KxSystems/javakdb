# Running Java kdb+ ipc benchmarks

Benchmarks use the Java Microbenchmark Harness ([JMH](https://openjdk.org/projects/code-tools/jmh/)).
After [building](build.md) the source, the following are examples of using the benchmarks provided.

In the following examples, replace `javakdb-benchmark-2.1.1.jar` with the current version found in the directory.

List all options

```bash
java -jar javakdb-benchmark/target/javakdb-benchmark-2.1.1.jar -h
```

Run all benchmarks provided

```bash
java -jar javakdb-benchmark/target/javakdb-benchmark-2.1.1.jar
```

List all benchmarks provided

```bash
java -jar javakdb-benchmark/target/javakdb-benchmark-2.1.1.jar -l
```

Example of running one benchmark test called `SerializationBenchmark.serializeInts`

```bash
java -jar javakdb-benchmark/target/javakdb-benchmark-2.1.1.jar SerializationBenchmark.serializeInts
```
