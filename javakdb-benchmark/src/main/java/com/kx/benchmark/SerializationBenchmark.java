package com.kx.benchmark;

import com.kx.c;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(3)
@State(Scope.Thread)
public class SerializationBenchmark {

    private c connection;

    private int[] ints;
    private long[] longs;
    private double[] doubles;
    private byte[] bytes;

    private byte[] serializedInts;
    private byte[] serializedLongs;
    private byte[] serializedDoubles;
    private byte[] serializedBytes;

    @Setup
    public void setup() throws IOException {
        connection = new c();

        int size = 1_000_000;

        ints = new int[size];
        longs = new long[size];
        doubles = new double[size];
        bytes = new byte[size];

        for (int i = 0; i < size; ++i) {
            ints[i] = i;
            longs[i] = i;
            doubles[i] = i;
            bytes[i] = (byte)i;
        }

        serializedInts = connection.serialize(0, ints, false);
        serializedLongs = connection.serialize(0, longs, false);
        serializedDoubles = connection.serialize(0, doubles, false);
        serializedBytes = connection.serialize(0, bytes, false);
    }

    @Benchmark
    public byte[] serializeInts() throws IOException {
        return connection.serialize(0, ints, false);
    }

    @Benchmark
    public byte[] serializeLongs() throws IOException {
        return connection.serialize(0, longs, false);
    }

    @Benchmark
    public byte[] serializeDoubles() throws IOException {
        return connection.serialize(0, doubles, false);
    }

    @Benchmark
    public byte[] serializeBytes() throws IOException {
        return connection.serialize(0, bytes, false);
    }

    @Benchmark
    public Object deserializeInts() throws Exception {
        return connection.deserialize(serializedInts);
    }

    @Benchmark
    public Object deserializeLongs() throws Exception {
        return connection.deserialize(serializedLongs);
    }

    @Benchmark
    public Object deserializeDoubles() throws Exception {
        return connection.deserialize(serializedDoubles);
    }

    @Benchmark
    public Object deserializeBytes() throws Exception {
        return connection.deserialize(serializedBytes);
    }
}
