package com.kx.benchmark;

import com.kx.c;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

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
        for (int i = 0; i < values.length; ++i) {
            values[i] = (short)i;
        }
        return values;
    }

    private static int[] createInts() {
        int[] values = new int[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = i;
        }
        return values;
    }

    private static long[] createLongs() {
        long[] values = new long[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = i;
        }
        return values;
    }

    private static double[] createDoubles() {
        double[] values = new double[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = i;
        }
        return values;
    }

    private static c.Minute[] createMinutes() {
        c.Minute[] values = new c.Minute[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = new c.Minute(i);
        }
        return values;
    }

    private static char[] createChars() {
        char[] values = new char[93];
        for (int i = 0; i < 77; ++i) {
            values[i] = (char)(i + 48);
        }
        return values;
    }

    private static byte[] createBytes() {
        byte[] values = new byte[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = (byte)i;
        }
        return values;
    }

    @State(Scope.Thread)
    public abstract static class BenchmarkState {
        c connection;

        @Setup
        public final void setup() throws IOException {
            connection = new c();
            setupValues();
        }

        protected abstract void setupValues() throws IOException;
    }

    public abstract static class SerializeState<T> extends BenchmarkState {
        T values;

        @Override
        protected final void setupValues() {
            values = createValues();
        }

        protected abstract T createValues();
    }

    public abstract static class DeserializeState<T> extends BenchmarkState {
        byte[] values;

        @Override
        protected final void setupValues() throws IOException {
            values = connection.serialize(0, createValues(), false);
        }

        protected abstract T createValues();
    }

    public static class SerializeShortsState extends SerializeState<short[]> {
        @Override
        protected short[] createValues() {
            return createShorts();
        }
    }

    public static class SerializeIntsState extends SerializeState<int[]> {
        @Override
        protected int[] createValues() {
            return createInts();
        }
    }

    public static class SerializeLongsState extends SerializeState<long[]> {
        @Override
        protected long[] createValues() {
            return createLongs();
        }
    }

    public static class SerializeDoublesState extends SerializeState<double[]> {
        @Override
        protected double[] createValues() {
            return createDoubles();
        }
    }

    public static class SerializeMinutesState extends SerializeState<c.Minute[]> {
        @Override
        protected c.Minute[] createValues() {
            return createMinutes();
        }
    }

    public static class SerializeCharsState extends SerializeState<char[]> {
        @Override
        protected char[] createValues() {
            return createChars();
        }
    }

    public static class SerializeBytesState extends SerializeState<byte[]> {
        @Override
        protected byte[] createValues() {
            return createBytes();
        }
    }

    public static class DeserializeShortsState extends DeserializeState<short[]> {
        @Override
        protected short[] createValues() {
            return createShorts();
        }
    }

    public static class DeserializeIntsState extends DeserializeState<int[]> {
        @Override
        protected int[] createValues() {
            return createInts();
        }
    }

    public static class DeserializeLongsState extends DeserializeState<long[]> {
        @Override
        protected long[] createValues() {
            return createLongs();
        }
    }

    public static class DeserializeDoublesState extends DeserializeState<double[]> {
        @Override
        protected double[] createValues() {
            return createDoubles();
        }
    }

    public static class DeserializeMinutesState extends DeserializeState<c.Minute[]> {
        @Override
        protected c.Minute[] createValues() {
            return createMinutes();
        }
    }

    public static class DeserializeCharsState extends DeserializeState<char[]> {
        @Override
        protected char[] createValues() {
            return createChars();
        }
    }

    public static class DeserializeBytesState extends DeserializeState<byte[]> {
        @Override
        protected byte[] createValues() {
            return createBytes();
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
    public byte[] serializeMinutes(SerializeMinutesState state) throws IOException {
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
    public Object deserializeMinutes(DeserializeMinutesState state) throws Exception {
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
