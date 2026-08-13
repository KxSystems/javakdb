# Running Java kdb+ ipc benchmarks

Benchmarks use the Java Microbenchmark Harness ([JMH](https://openjdk.org/projects/code-tools/jmh/)).
After [building](build.md) the source, the following are examples of using the benchmarks provided.

List all options

```bash
java -jar javakdb-benchmark/target/benchmarks.jar -h
```

Run all benchmarks provided

```bash
java -jar javakdb-benchmark/target/benchmarks.jar
```

List all benchmarks provided

```bash
java -jar javakdb-benchmark/target/benchmarks.jar -l
```

Example of running one benchmark test called `SerializationBenchmark.serializeInts`

```bash
java -jar javakdb-benchmark/target/benchmarks.jar SerializationBenchmark.serializeInts
```
