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
public class SerializationBenchmark {

    private static final int SIZE = 100_000;

    private static short[] createShorts() {
        short[] values = new short[SIZE];
        for (int i = 0; i < SIZE; ++i) {
            values[i] = (short)i;
        }
        return values;
    }

    private static int[] createInts() {
        int[] values = new int[SIZE];
        for (int i = 0; i < SIZE; ++i) {
            values[i] = i;
        }
        return values;
    }

    private static long[] createLongs() {
        long[] values = new long[SIZE];
        for (int i = 0; i < SIZE; ++i) {
            values[i] = i;
        }
        return values;
    }

    private static double[] createDoubles() {
        double[] values = new double[SIZE];
        for (int i = 0; i < SIZE; ++i) {
            values[i] = i;
        }
        return values;
    }

    private static char[] createChars() {
        char[] values = new char[93];
        for (int i = 0; i < 77; ++i) {
            values[i] = (char)(i+48); /* ascii value, from '0' to '}' */
        }
        return values;
    }

    private static byte[] createBytes() {
        byte[] values = new byte[SIZE];
        for (int i = 0; i < SIZE; ++i) {
            values[i] = (byte)i;
        }
        return values;
    }

    @State(Scope.Thread)
    public static class SerializeShortsState {
        c connection;
        short[] values;

        @Setup
        public void setup() throws IOException {
            connection = new c();
            values = createShorts();
        }
    }

    @State(Scope.Thread)
    public static class SerializeIntsState {
        c connection;
        int[] values;

        @Setup
        public void setup() throws IOException {
            connection = new c();
            values = createInts();
        }
    }

    @State(Scope.Thread)
    public static class SerializeLongsState {
        c connection;
        long[] values;

        @Setup
        public void setup() throws IOException {
            connection = new c();
            values = createLongs();
        }
    }

    @State(Scope.Thread)
    public static class SerializeDoublesState {
        c connection;
        double[] values;

        @Setup
        public void setup() throws IOException {
            connection = new c();
            values = createDoubles();
        }
    }

    @State(Scope.Thread)
    public static class SerializeCharsState {
        c connection;
        char[] values;

        @Setup
        public void setup() throws IOException {
            connection = new c();
            values = createChars();
        }
    }

    @State(Scope.Thread)
    public static class SerializeBytesState {
        c connection;
        byte[] values;

        @Setup
        public void setup() throws IOException {
            connection = new c();
            values = createBytes();
        }
    }

    @State(Scope.Thread)
    public static class DeserializeShortsState {
        c connection;
        byte[] values;

        @Setup
        public void setup() throws IOException {
            connection = new c();
            values = connection.serialize(0, createShorts(), false);
        }
    }

    @State(Scope.Thread)
    public static class DeserializeIntsState {
        c connection;
        byte[] values;

        @Setup
        public void setup() throws IOException {
            connection = new c();
            values = connection.serialize(0, createInts(), false);
        }
    }

    @State(Scope.Thread)
    public static class DeserializeLongsState {
        c connection;
        byte[] values;

        @Setup
        public void setup() throws IOException {
            connection = new c();
            values = connection.serialize(0, createLongs(), false);
        }
    }

    @State(Scope.Thread)
    public static class DeserializeDoublesState {
        c connection;
        byte[] values;

        @Setup
        public void setup() throws IOException {
            connection = new c();
            values = connection.serialize(0, createDoubles(), false);
        }
    }

    @State(Scope.Thread)
    public static class DeserializeCharsState {
        c connection;
        byte[] values;

        @Setup
        public void setup() throws IOException {
            connection = new c();
            values = connection.serialize(0, createChars(), false);
        }
    }

    @State(Scope.Thread)
    public static class DeserializeBytesState {
        c connection;
        byte[] values;

        @Setup
        public void setup() throws IOException {
            connection = new c();
            values = connection.serialize(0, createBytes(), false);
        }
    }

    @Benchmark
    public byte[] serializeShorts(SerializeShortsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    @Benchmark
    public byte[] serializeInts(SerializeIntsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    @Benchmark
    public byte[] serializeLongs(SerializeLongsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    @Benchmark
    public byte[] serializeDoubles(SerializeDoublesState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    @Benchmark
    public byte[] serializeChars(SerializeCharsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    @Benchmark
    public byte[] serializeBytes(SerializeBytesState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    @Benchmark
    public Object deserializeShorts(DeserializeShortsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    @Benchmark
    public Object deserializeInts(DeserializeIntsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    @Benchmark
    public Object deserializeLongs(DeserializeLongsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    @Benchmark
    public Object deserializeDoubles(DeserializeDoublesState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    @Benchmark
    public Object deserializeChars(DeserializeCharsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    @Benchmark
    public Object deserializeBytes(DeserializeBytesState state) throws Exception {
        return state.connection.deserialize(state.values);
    }
}
