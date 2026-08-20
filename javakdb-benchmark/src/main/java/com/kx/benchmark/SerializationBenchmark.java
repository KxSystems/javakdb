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
import java.util.UUID;
import java.time.Instant;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(3)
public class SerializationBenchmark {

    private static final int SIZE = 100_000;

    private static Object[] createShortAtoms() {
        Object[] values = new Object[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = Short.valueOf((short)i);
        }
        return values;
    }

    private static Object[] createIntAtoms() {
        Object[] values = new Object[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = Integer.valueOf(i);
        }
        return values;
    }

    private static Object[] createLongAtoms() {
        Object[] values = new Object[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = Long.valueOf(i);
        }
        return values;
    }

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

    private static float[] createFloats() {
        float[] values = new float[SIZE];
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

    private static Instant[] createInstants() {
        Instant[] values = new Instant[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = Instant.ofEpochMilli(1);
        }
        return values;
    }

    private static boolean[] createBooleans() {
        boolean[] values = new boolean[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = (i%2==0);
        }
        return values;
    }

    private static LocalDate[] createLocalDates() {
        LocalDate[] values = new LocalDate[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = LocalDate.of(2001,10,10);
        }
        return values;
    }

    private static LocalDateTime[] createLocalDateTimes() {
        LocalDateTime[] values = new LocalDateTime[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = LocalDateTime.of(1990,1,1,10,30,59,1000000);
        }
        return values;
    }

    private static UUID[] createUUIDs() {
        UUID[] values = new UUID[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = new UUID(6666666,7777777);;
        }
        return values;
    }

    private static String[] createStrings() {
        String[] values = new String[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = new String("ABCD");
        }
        return values;
    }

    private static LocalTime[] createLocalTimes() {
        LocalTime[] values = new LocalTime[SIZE];
        for (int i = 0; i < values.length; ++i) {
            values[i] = LocalTime.of(23, 12, 34, 567_000_000);;
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

    private static Object[] createQuoteBatch() {
        int batchSize=100;
        String[]syms=new String[]{"ABC","DEF","GHI","JKL"}; // symbols to randomly choose from
        c.Timespan[] time=new c.Timespan[batchSize];
        String[] sym=new String[batchSize];
        double[] price=new double[batchSize];
        long[] size=new long[batchSize];
        for(int i=0;i<batchSize;i++){
            time[i]=new c.Timespan();
            sym[i]=syms[i%syms.length]; // choose a random symbol
            price[i]=i;
            size[i]=i*10L;
        }
        return new Object[]{time,sym,price,size,price,size};
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

    public static class SerializeShortAtomsState extends SerializeState<Object[]> {
        @Override
        protected Object[] createValues() {
            return createShortAtoms();
        }
    }

    public static class SerializeIntAtomsState extends SerializeState<Object[]> {
        @Override
        protected Object[] createValues() {
            return createIntAtoms();
        }
    }

    public static class SerializeLongAtomsState extends SerializeState<Object[]> {
        @Override
        protected Object[] createValues() {
            return createLongAtoms();
        }
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

    public static class SerializeFloatsState extends SerializeState<float[]> {
        @Override
        protected float[] createValues() {
            return createFloats();
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

    public static class SerializeInstantsState extends SerializeState<Instant[]> {
        @Override
        protected Instant[] createValues() {
            return createInstants();
        }
    }

    public static class SerializeBooleansState extends SerializeState<boolean[]> {
        @Override
        protected boolean[] createValues() {
            return createBooleans();
        }
    }

    public static class SerializeLocalDatesState extends SerializeState<LocalDate[]> {
        @Override
        protected LocalDate[] createValues() {
            return createLocalDates();
        }
    }

    public static class SerializeLocalDateTimesState extends SerializeState<LocalDateTime[]> {
        @Override
        protected LocalDateTime[] createValues() {
            return createLocalDateTimes();
        }
    }

    public static class SerializeUUIDsState extends SerializeState<UUID[]> {
        @Override
        protected UUID[] createValues() {
            return createUUIDs();
        }
    }

    public static class SerializeStringsState extends SerializeState<String[]> {
        @Override
        protected String[] createValues() {
            return createStrings();
        }
    }

    public static class SerializeLocalTimesState extends SerializeState<LocalTime[]> {
        @Override
        protected LocalTime[] createValues() {
            return createLocalTimes();
        }
    }

    public static class SerializeCharsState extends SerializeState<char[]> {
        @Override
        protected char[] createValues() {
            return createChars();
        }
    }

    public static class SerializeQuoteBatchState extends SerializeState<Object[]> {
        @Override
        protected Object[] createValues() {
            return createQuoteBatch();
        }
    }

    public static class SerializeBytesState extends SerializeState<byte[]> {
        @Override
        protected byte[] createValues() {
            return createBytes();
        }
    }

    public static class DeserializeShortAtomsState extends DeserializeState<Object[]> {
        @Override
        protected Object[] createValues() {
            return createShortAtoms();
        }
    }

    public static class DeserializeIntAtomsState extends DeserializeState<Object[]> {
        @Override
        protected Object[] createValues() {
            return createIntAtoms();
        }
    }

    public static class DeserializeLongAtomsState extends DeserializeState<Object[]> {
        @Override
        protected Object[] createValues() {
            return createLongAtoms();
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

    public static class DeserializeFloatsState extends DeserializeState<float[]> {
        @Override
        protected float[] createValues() {
            return createFloats();
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

    public static class DeserializeInstantsState extends DeserializeState<Instant[]> {
        @Override
        protected Instant[] createValues() {
            return createInstants();
        }
    }

    public static class DeserializeBooleansState extends DeserializeState<boolean[]> {
        @Override
        protected boolean[] createValues() {
            return createBooleans();
        }
    }

    public static class DeserializeLocalDatesState extends DeserializeState<LocalDate[]> {
        @Override
        protected LocalDate[] createValues() {
            return createLocalDates();
        }
    }

    public static class DeserializeLocalDateTimesState extends DeserializeState<LocalDateTime[]> {
        @Override
        protected LocalDateTime[] createValues() {
            return createLocalDateTimes();
        }
    }

    public static class DeserializeUUIDsState extends DeserializeState<UUID[]> {
        @Override
        protected UUID[] createValues() {
            return createUUIDs();
        }
    }

    public static class DeserializeStringsState extends DeserializeState<String[]> {
        @Override
        protected String[] createValues() {
            return createStrings();
        }
    }

    public static class DeserializeLocalTimesState extends DeserializeState<LocalTime[]> {
        @Override
        protected LocalTime[] createValues() {
            return createLocalTimes();
        }
    }

    public static class DeserializeCharsState extends DeserializeState<char[]> {
        @Override
        protected char[] createValues() {
            return createChars();
        }
    }

    public static class DeserializeQuoteBatchState extends DeserializeState<Object[]> {
        @Override
        protected Object[] createValues() {
            return createQuoteBatch();
        }
    }

    public static class DeserializeBytesState extends DeserializeState<byte[]> {
        @Override
        protected byte[] createValues() {
            return createBytes();
        }
    }

    @Benchmark
    public byte[] serializeShortAtoms(SerializeShortAtomsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    @Benchmark
    public byte[] serializeIntAtoms(SerializeIntAtomsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    @Benchmark
    public byte[] serializeLongAtoms(SerializeLongAtomsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
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
    public byte[] serializeFloats(SerializeFloatsState state) throws IOException {
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
    public byte[] serializeInstants(SerializeInstantsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    @Benchmark
    public byte[] serializeBooleans(SerializeBooleansState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    @Benchmark
    public byte[] serializeLocalDates(SerializeLocalDatesState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    @Benchmark
    public byte[] serializeLocalDateTimes(SerializeLocalDateTimesState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    @Benchmark
    public byte[] serializeUUIDs(SerializeUUIDsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    @Benchmark
    public byte[] serializeStrings(SerializeStringsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    @Benchmark
    public byte[] serializeLocalTimes(SerializeLocalTimesState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    @Benchmark
    public byte[] serializeChars(SerializeCharsState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    @Benchmark
    public byte[] serializeQuoteBatch(SerializeQuoteBatchState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    @Benchmark
    public byte[] serializeBytes(SerializeBytesState state) throws IOException {
        return state.connection.serialize(0, state.values, false);
    }

    @Benchmark
    public Object deserializeShortAtoms(DeserializeShortAtomsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    @Benchmark
    public Object deserializeIntAtoms(DeserializeIntAtomsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    @Benchmark
    public Object deserializeLongAtoms(DeserializeLongAtomsState state) throws Exception {
        return state.connection.deserialize(state.values);
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
    public Object deserializeFloats(DeserializeFloatsState state) throws Exception {
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
    public Object deserializeInstants(DeserializeInstantsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    @Benchmark
    public Object deserializeBooleans(DeserializeBooleansState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    @Benchmark
    public Object deserializeLocalDates(DeserializeLocalDatesState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    @Benchmark
    public Object deserializeLocalDateTimes(DeserializeLocalDateTimesState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    @Benchmark
    public Object deserializeUUIDs(DeserializeUUIDsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    @Benchmark
    public Object deserializeStrings(DeserializeStringsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    @Benchmark
    public Object deserializeLocalTimes(DeserializeLocalTimesState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    @Benchmark
    public Object deserializeChars(DeserializeCharsState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    @Benchmark
    public Object deserializeQuoteBatch(DeserializeQuoteBatchState state) throws Exception {
        return state.connection.deserialize(state.values);
    }

    @Benchmark
    public Object deserializeBytes(DeserializeBytesState state) throws Exception {
        return state.connection.deserialize(state.values);
    }
}
