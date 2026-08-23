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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * JMH benchmarks for measuring javakdb serialization and deserialization performance.
 *
 * <p>The benchmarks cover boxed atom values, primitive vectors, temporal values,
 * UUIDs, strings, characters, bytes, and a representative quote batch. Each JMH
 * worker thread owns its own {@link c} instance so that benchmark invocations do
 * not share mutable serialization state.</p>
 *
 * <p>Results are reported as average execution time in microseconds. JMH performs
 * five warm-up iterations, ten measurement iterations, and three forks.</p>
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(3)
public class SerializationBenchmark {

    /** Number of elements used by the standard vector benchmarks. */
    private static final int SIZE = 100_000;

    /**
    * Creates a serialization benchmark instance.
    */
    public SerializationBenchmark() {
        /* default contructor */
    }

    /**
     * Creates boxed short values for the short-atom benchmark.
     *
     * @return an array containing {@link #SIZE} boxed short values
     */
    private static Object[] createShortAtoms() {
        Object[] values = new Object[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = Short.valueOf((short) i);
        }
        return values;
    }

    /**
     * Creates boxed integer values for the integer-atom benchmark.
     *
     * @return an array containing {@link #SIZE} boxed integer values
     */
    private static Object[] createIntAtoms() {
        Object[] values = new Object[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = Integer.valueOf(i);
        }
        return values;
    }

    /**
     * Creates boxed long values for the long-atom benchmark.
     *
     * @return an array containing {@link #SIZE} boxed long values
     */
    private static Object[] createLongAtoms() {
        Object[] values = new Object[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = Long.valueOf(i);
        }
        return values;
    }

    /**
     * Creates values for the short-vector benchmark.
     *
     * @return a short array containing {@link #SIZE} values
     */
    private static short[] createShorts() {
        short[] values = new short[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = (short) i;
        }
        return values;
    }

    /**
     * Creates values for the integer-vector benchmark.
     *
     * @return an integer array containing {@link #SIZE} values
     */
    private static int[] createInts() {
        int[] values = new int[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = i;
        }
        return values;
    }

    /**
     * Creates values for the long-vector benchmark.
     *
     * @return a long array containing {@link #SIZE} values
     */
    private static long[] createLongs() {
        long[] values = new long[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = i;
        }
        return values;
    }

    /**
     * Creates values for the real-vector benchmark.
     *
     * @return a float array containing {@link #SIZE} values
     */
    private static float[] createFloats() {
        float[] values = new float[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = i;
        }
        return values;
    }

    /**
     * Creates values for the floating-point-vector benchmark.
     *
     * @return a double array containing {@link #SIZE} values
     */
    private static double[] createDoubles() {
        double[] values = new double[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = i;
        }
        return values;
    }

    /**
     * Creates values for the minute-vector benchmark.
     *
     * @return an array containing {@link #SIZE} minute values
     */
    private static c.Minute[] createMinutes() {
        c.Minute[] values = new c.Minute[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = new c.Minute(i);
        }
        return values;
    }

    /**
     * Creates values for the timestamp-vector benchmark.
     *
     * @return an array containing {@link #SIZE} timestamp values
     */
    private static Instant[] createInstants() {
        Instant[] values = new Instant[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = Instant.ofEpochMilli(1);
        }
        return values;
    }

    /**
     * Creates alternating boolean values for the boolean-vector benchmark.
     *
     * @return a boolean array containing {@link #SIZE} values
     */
    private static boolean[] createBooleans() {
        boolean[] values = new boolean[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = (i % 2 == 0);
        }
        return values;
    }

    /**
     * Creates values for the date-vector benchmark.
     *
     * @return an array containing {@link #SIZE} date values
     */
    private static LocalDate[] createLocalDates() {
        LocalDate[] values = new LocalDate[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = LocalDate.of(2001, 10, 10);
        }
        return values;
    }

    /**
     * Creates values for the datetime-vector benchmark.
     *
     * @return an array containing {@link #SIZE} datetime values
     */
    private static LocalDateTime[] createLocalDateTimes() {
        LocalDateTime[] values = new LocalDateTime[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = LocalDateTime.of(1990, 1, 1, 10, 30, 59, 1_000_000);
        }
        return values;
    }

    /**
     * Creates values for the GUID-vector benchmark.
     *
     * @return an array containing {@link #SIZE} UUID values
     */
    private static UUID[] createUUIDs() {
        UUID[] values = new UUID[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = new UUID(6_666_666, 7_777_777);
        }
        return values;
    }

    /**
     * Creates values for the string/symbol benchmark.
     *
     * @return an array containing {@link #SIZE} strings
     */
    private static String[] createStrings() {
        String[] values = new String[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = new String("ABCD");
        }
        return values;
    }

    /**
     * Creates values for the time-vector benchmark.
     *
     * @return an array containing {@link #SIZE} local-time values
     */
    private static LocalTime[] createLocalTimes() {
        LocalTime[] values = new LocalTime[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = LocalTime.of(23, 12, 34, 567_000_000);
        }
        return values;
    }

    /**
     * Creates the character data used by the character-vector benchmark.
     *
     * @return the character array used by the benchmark
     */
    private static char[] createChars() {
        char[] values = new char[93];
        for (int i = 0; i < 77; ++i) {
            values[i] = (char) (i + 48);
        }
        return values;
    }

    /**
     * Creates byte values for the byte-vector benchmark.
     *
     * @return a byte array containing {@link #SIZE} values
     */
    private static byte[] createBytes() {
        byte[] values = new byte[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = (byte) i;
        }
        return values;
    }

    /**
     * Creates a representative 100-row quote batch containing time, symbol,
     * price, and size columns.
     *
     * @return the quote-batch columns used by the benchmark
     */
    private static Object[] createQuoteBatch() {
        int batchSize = 100;
        String[] syms = new String[]{"ABC", "DEF", "GHI", "JKL"};
        c.Timespan[] time = new c.Timespan[batchSize];
        String[] sym = new String[batchSize];
        double[] price = new double[batchSize];
        long[] size = new long[batchSize];
        for (int i = 0; i < batchSize; i++) {
            time[i] = new c.Timespan();
            sym[i] = syms[i % syms.length];
            price[i] = i;
            size[i] = i * 10L;
        }
        return new Object[]{time, sym, price, size, price, size};
    }

    /**
     * Base JMH state shared by serialization and deserialization benchmarks.
     *
     * <p>The state is thread scoped because {@link c} contains mutable buffers and
     * buffer-position state that must not be shared by concurrent benchmark threads.</p>
     */
    @State(Scope.Thread)
    public abstract static class BenchmarkState {
        /** javakdb connection object used for local serialization/deserialization. */
        c connection;

        /**
         * Creates the javakdb connection object and initializes benchmark-specific data.
         *
         * @throws IOException if the benchmark data cannot be prepared
         */
        @Setup
        public final void setup() throws IOException {
            connection = new c();
            setupValues();
        }

        /**
         * Initializes the values required by a concrete benchmark state.
         *
         * @throws IOException if the benchmark values cannot be prepared
         */
        protected abstract void setupValues() throws IOException;
    }

    /**
     * Base state for serialization benchmarks.
     *
     * @param <T> Java type serialized by the benchmark
     */
    public abstract static class SerializeState<T> extends BenchmarkState {
        /** Values serialized by each benchmark invocation. */
        T values;

        /** {@inheritDoc} */
        @Override
        protected final void setupValues() {
            values = createValues();
        }

        /**
         * Creates the Java values serialized by this benchmark state.
         *
         * @return values to serialize
         */
        protected abstract T createValues();
    }

    /**
     * Base state for deserialization benchmarks.
     *
     * <p>The Java values are serialized once during JMH setup. Timed benchmark
     * invocations therefore measure deserialization rather than preparation or
     * serialization of the input buffer.</p>
     *
     * @param <T> Java type used to create the serialized input
     */
    public abstract static class DeserializeState<T> extends BenchmarkState {
        /** Serialized IPC message deserialized by each benchmark invocation. */
        byte[] values;

        /** {@inheritDoc} */
        @Override
        protected final void setupValues() throws IOException {
            values = connection.serialize(0, createValues(), false);
        }

        /**
         * Creates the Java values from which the deserialization input is prepared.
         *
         * @return values to serialize during setup
         */
        protected abstract T createValues();
    }

    /** Serialization state for the boxed short atoms benchmark. */
    public static class SerializeShortAtomsState extends SerializeState<Object[]> {
        /** {@inheritDoc} */
        @Override
        protected Object[] createValues() {
            return createShortAtoms();
        }
    }

    /** Serialization state for the boxed integer atoms benchmark. */
    public static class SerializeIntAtomsState extends SerializeState<Object[]> {
        /** {@inheritDoc} */
        @Override
        protected Object[] createValues() {
            return createIntAtoms();
        }
    }

    /** Serialization state for the boxed long atoms benchmark. */
    public static class SerializeLongAtomsState extends SerializeState<Object[]> {
        /** {@inheritDoc} */
        @Override
        protected Object[] createValues() {
            return createLongAtoms();
        }
    }

    /** Serialization state for the short vector benchmark. */
    public static class SerializeShortsState extends SerializeState<short[]> {
        /** {@inheritDoc} */
        @Override
        protected short[] createValues() {
            return createShorts();
        }
    }

    /** Serialization state for the integer vector benchmark. */
    public static class SerializeIntsState extends SerializeState<int[]> {
        /** {@inheritDoc} */
        @Override
        protected int[] createValues() {
            return createInts();
        }
    }

    /** Serialization state for the long vector benchmark. */
    public static class SerializeLongsState extends SerializeState<long[]> {
        /** {@inheritDoc} */
        @Override
        protected long[] createValues() {
            return createLongs();
        }
    }

    /** Serialization state for the real vector benchmark. */
    public static class SerializeFloatsState extends SerializeState<float[]> {
        /** {@inheritDoc} */
        @Override
        protected float[] createValues() {
            return createFloats();
        }
    }

    /** Serialization state for the float vector benchmark. */
    public static class SerializeDoublesState extends SerializeState<double[]> {
        /** {@inheritDoc} */
        @Override
        protected double[] createValues() {
            return createDoubles();
        }
    }

    /** Serialization state for the minute vector benchmark. */
    public static class SerializeMinutesState extends SerializeState<c.Minute[]> {
        /** {@inheritDoc} */
        @Override
        protected c.Minute[] createValues() {
            return createMinutes();
        }
    }

    /** Serialization state for the timestamp vector benchmark. */
    public static class SerializeInstantsState extends SerializeState<Instant[]> {
        /** {@inheritDoc} */
        @Override
        protected Instant[] createValues() {
            return createInstants();
        }
    }

    /** Serialization state for the boolean vector benchmark. */
    public static class SerializeBooleansState extends SerializeState<boolean[]> {
        /** {@inheritDoc} */
        @Override
        protected boolean[] createValues() {
            return createBooleans();
        }
    }

    /** Serialization state for the date vector benchmark. */
    public static class SerializeLocalDatesState extends SerializeState<LocalDate[]> {
        /** {@inheritDoc} */
        @Override
        protected LocalDate[] createValues() {
            return createLocalDates();
        }
    }

    /** Serialization state for the datetime vector benchmark. */
    public static class SerializeLocalDateTimesState extends SerializeState<LocalDateTime[]> {
        /** {@inheritDoc} */
        @Override
        protected LocalDateTime[] createValues() {
            return createLocalDateTimes();
        }
    }

    /** Serialization state for the GUID vector benchmark. */
    public static class SerializeUUIDsState extends SerializeState<UUID[]> {
        /** {@inheritDoc} */
        @Override
        protected UUID[] createValues() {
            return createUUIDs();
        }
    }

    /** Serialization state for the symbol/string values benchmark. */
    public static class SerializeStringsState extends SerializeState<String[]> {
        /** {@inheritDoc} */
        @Override
        protected String[] createValues() {
            return createStrings();
        }
    }

    /** Serialization state for the time vector benchmark. */
    public static class SerializeLocalTimesState extends SerializeState<LocalTime[]> {
        /** {@inheritDoc} */
        @Override
        protected LocalTime[] createValues() {
            return createLocalTimes();
        }
    }

    /** Serialization state for the character vector benchmark. */
    public static class SerializeCharsState extends SerializeState<char[]> {
        /** {@inheritDoc} */
        @Override
        protected char[] createValues() {
            return createChars();
        }
    }

    /** Serialization state for the quote batch benchmark. */
    public static class SerializeQuoteBatchState extends SerializeState<Object[]> {
        /** {@inheritDoc} */
        @Override
        protected Object[] createValues() {
            return createQuoteBatch();
        }
    }

    /** Serialization state for the byte vector benchmark. */
    public static class SerializeBytesState extends SerializeState<byte[]> {
        /** {@inheritDoc} */
        @Override
        protected byte[] createValues() {
            return createBytes();
        }
    }

    /** Deserialization state for the boxed short atoms benchmark. */
    public static class DeserializeShortAtomsState extends DeserializeState<Object[]> {
        /** {@inheritDoc} */
        @Override
        protected Object[] createValues() {
            return createShortAtoms();
        }
    }

    /** Deserialization state for the boxed integer atoms benchmark. */
    public static class DeserializeIntAtomsState extends DeserializeState<Object[]> {
        /** {@inheritDoc} */
        @Override
        protected Object[] createValues() {
            return createIntAtoms();
        }
    }

    /** Deserialization state for the boxed long atoms benchmark. */
    public static class DeserializeLongAtomsState extends DeserializeState<Object[]> {
        /** {@inheritDoc} */
        @Override
        protected Object[] createValues() {
            return createLongAtoms();
        }
    }

    /** Deserialization state for the short vector benchmark. */
    public static class DeserializeShortsState extends DeserializeState<short[]> {
        /** {@inheritDoc} */
        @Override
        protected short[] createValues() {
            return createShorts();
        }
    }

    /** Deserialization state for the integer vector benchmark. */
    public static class DeserializeIntsState extends DeserializeState<int[]> {
        /** {@inheritDoc} */
        @Override
        protected int[] createValues() {
            return createInts();
        }
    }

    /** Deserialization state for the long vector benchmark. */
    public static class DeserializeLongsState extends DeserializeState<long[]> {
        /** {@inheritDoc} */
        @Override
        protected long[] createValues() {
            return createLongs();
        }
    }

    /** Deserialization state for the real vector benchmark. */
    public static class DeserializeFloatsState extends DeserializeState<float[]> {
        /** {@inheritDoc} */
        @Override
        protected float[] createValues() {
            return createFloats();
        }
    }

    /** Deserialization state for the float vector benchmark. */
    public static class DeserializeDoublesState extends DeserializeState<double[]> {
        /** {@inheritDoc} */
        @Override
        protected double[] createValues() {
            return createDoubles();
        }
    }

    /** Deserialization state for the minute vector benchmark. */
    public static class DeserializeMinutesState extends DeserializeState<c.Minute[]> {
        /** {@inheritDoc} */
        @Override
        protected c.Minute[] createValues() {
            return createMinutes();
        }
    }

    /** Deserialization state for the timestamp vector benchmark. */
    public static class DeserializeInstantsState extends DeserializeState<Instant[]> {
        /** {@inheritDoc} */
        @Override
        protected Instant[] createValues() {
            return createInstants();
        }
    }

    /** Deserialization state for the boolean vector benchmark. */
    public static class DeserializeBooleansState extends DeserializeState<boolean[]> {
        /** {@inheritDoc} */
        @Override
        protected boolean[] createValues() {
            return createBooleans();
        }
    }

    /** Deserialization state for the date vector benchmark. */
    public static class DeserializeLocalDatesState extends DeserializeState<LocalDate[]> {
        /** {@inheritDoc} */
        @Override
        protected LocalDate[] createValues() {
            return createLocalDates();
        }
    }

    /** Deserialization state for the datetime vector benchmark. */
    public static class DeserializeLocalDateTimesState extends DeserializeState<LocalDateTime[]> {
        /** {@inheritDoc} */
        @Override
        protected LocalDateTime[] createValues() {
            return createLocalDateTimes();
        }
    }

    /** Deserialization state for the GUID vector benchmark. */
    public static class DeserializeUUIDsState extends DeserializeState<UUID[]> {
        /** {@inheritDoc} */
        @Override
        protected UUID[] createValues() {
            return createUUIDs();
        }
    }

    /** Deserialization state for the symbol/string values benchmark. */
    public static class DeserializeStringsState extends DeserializeState<String[]> {
        /** {@inheritDoc} */
        @Override
        protected String[] createValues() {
            return createStrings();
        }
    }

    /** Deserialization state for the time vector benchmark. */
    public static class DeserializeLocalTimesState extends DeserializeState<LocalTime[]> {
        /** {@inheritDoc} */
        @Override
        protected LocalTime[] createValues() {
            return createLocalTimes();
        }
    }

    /** Deserialization state for the character vector benchmark. */
    public static class DeserializeCharsState extends DeserializeState<char[]> {
        /** {@inheritDoc} */
        @Override
        protected char[] createValues() {
            return createChars();
        }
    }

    /** Deserialization state for the quote batch benchmark. */
    public static class DeserializeQuoteBatchState extends DeserializeState<Object[]> {
        /** {@inheritDoc} */
        @Override
        protected Object[] createValues() {
            return createQuoteBatch();
        }
    }

    /** Deserialization state for the byte vector benchmark. */
    public static class DeserializeBytesState extends DeserializeState<byte[]> {
        /** {@inheritDoc} */
        @Override
        protected byte[] createValues() {
            return createBytes();
        }
    }

    /**
     * Measures serialization of the prepared boxed short atoms values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeShortAtoms(SerializeShortAtomsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures serialization of the prepared boxed integer atoms values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeIntAtoms(SerializeIntAtomsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures serialization of the prepared boxed long atoms values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeLongAtoms(SerializeLongAtomsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures serialization of the prepared short vector values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeShorts(SerializeShortsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures serialization of the prepared integer vector values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeInts(SerializeIntsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures serialization of the prepared long vector values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeLongs(SerializeLongsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures serialization of the prepared real vector values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeFloats(SerializeFloatsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures serialization of the prepared float vector values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeDoubles(SerializeDoublesState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures serialization of the prepared minute vector values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeMinutes(SerializeMinutesState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures serialization of the prepared timestamp vector values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeInstants(SerializeInstantsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures serialization of the prepared boolean vector values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeBooleans(SerializeBooleansState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures serialization of the prepared date vector values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeLocalDates(SerializeLocalDatesState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures serialization of the prepared datetime vector values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeLocalDateTimes(SerializeLocalDateTimesState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures serialization of the prepared GUID vector values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeUUIDs(SerializeUUIDsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures serialization of the prepared symbol/string values values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeStrings(SerializeStringsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures serialization of the prepared time vector values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeLocalTimes(SerializeLocalTimesState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures serialization of the prepared character vector values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeChars(SerializeCharsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures serialization of the prepared quote batch values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeQuoteBatch(SerializeQuoteBatchState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures serialization of the prepared byte vector values.
     *
     * @param state thread-local benchmark state containing the values to serialize
     * @return the serialized kdb+ IPC message
     * @throws IOException if serialization fails
     */
    @Benchmark
    public byte[] serializeBytes(SerializeBytesState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    /**
     * Measures deserialization of a prepared boxed short atoms IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeShortAtoms(DeserializeShortAtomsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    /**
     * Measures deserialization of a prepared boxed integer atoms IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeIntAtoms(DeserializeIntAtomsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    /**
     * Measures deserialization of a prepared boxed long atoms IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeLongAtoms(DeserializeLongAtomsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    /**
     * Measures deserialization of a prepared short vector IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeShorts(DeserializeShortsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    /**
     * Measures deserialization of a prepared integer vector IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeInts(DeserializeIntsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    /**
     * Measures deserialization of a prepared long vector IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeLongs(DeserializeLongsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    /**
     * Measures deserialization of a prepared real vector IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeFloats(DeserializeFloatsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    /**
     * Measures deserialization of a prepared float vector IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeDoubles(DeserializeDoublesState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    /**
     * Measures deserialization of a prepared minute vector IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeMinutes(DeserializeMinutesState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    /**
     * Measures deserialization of a prepared timestamp vector IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeInstants(DeserializeInstantsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    /**
     * Measures deserialization of a prepared boolean vector IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeBooleans(DeserializeBooleansState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    /**
     * Measures deserialization of a prepared date vector IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeLocalDates(DeserializeLocalDatesState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    /**
     * Measures deserialization of a prepared datetime vector IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeLocalDateTimes(DeserializeLocalDateTimesState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    /**
     * Measures deserialization of a prepared GUID vector IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeUUIDs(DeserializeUUIDsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    /**
     * Measures deserialization of a prepared symbol/string values IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeStrings(DeserializeStringsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    /**
     * Measures deserialization of a prepared time vector IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeLocalTimes(DeserializeLocalTimesState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    /**
     * Measures deserialization of a prepared character vector IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeChars(DeserializeCharsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    /**
     * Measures deserialization of a prepared quote batch IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeQuoteBatch(DeserializeQuoteBatchState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    /**
     * Measures deserialization of a prepared byte vector IPC message.
     *
     * @param state thread-local benchmark state containing the serialized message
     * @return the deserialized Java value
     * @throws Exception if deserialization fails
     */
    @Benchmark
    public Object deserializeBytes(DeserializeBytesState state) throws Exception {
        return state.connection.deserialize(state.values);
    }
}
