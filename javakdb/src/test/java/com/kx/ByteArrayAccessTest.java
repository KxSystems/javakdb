package com.kx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the byte-array primitives used by the IPC codec.
 */
public class ByteArrayAccessTest {
    @Test
    public void testGetScalarsInBothByteOrders() {
        byte[] bytes = {0, (byte)0x81, 0x23, 0x45, 0x67, (byte)0x89, (byte)0xab, (byte)0xcd, (byte)0xef};

        Assert.assertEquals((short)0x8123, ByteArrayAccess.getShortBE(bytes, 1));
        Assert.assertEquals((short)0x2381, ByteArrayAccess.getShortLE(bytes, 1));
        Assert.assertEquals(0x81234567, ByteArrayAccess.getIntBE(bytes, 1));
        Assert.assertEquals(0x67452381, ByteArrayAccess.getIntLE(bytes, 1));
        Assert.assertEquals(0x8123456789abcdefL, ByteArrayAccess.getLongBE(bytes, 1));
        Assert.assertEquals(0xefcdab8967452381L, ByteArrayAccess.getLongLE(bytes, 1));
    }

    @Test
    public void testPutScalarsAtUnalignedOffsets() {
        short shortValue = (short)0x8123;
        int intValue = 0x89abcdef;
        long longValue = 0x8123456789abcdefL;
        byte[] actual = new byte[24];

        ByteArrayAccess.putShortLE(actual, 1, shortValue);
        ByteArrayAccess.putIntLE(actual, 3, intValue);
        ByteArrayAccess.putLongLE(actual, 7, longValue);
        ByteArrayAccess.putLongBE(actual, 15, longValue);

        ByteBuffer expected = ByteBuffer.allocate(actual.length).order(ByteOrder.LITTLE_ENDIAN);
        expected.putShort(1, shortValue).putInt(3, intValue).putLong(7, longValue);
        expected.order(ByteOrder.BIG_ENDIAN).putLong(15, longValue);
        Assert.assertArrayEquals(expected.array(), actual);
    }

    @Test
    public void testGetIntegerVectorsInBothByteOrders() {
        short[] shorts = {(short)0x8123, 0x4567};
        int[] ints = {0x89abcdef, 0x01234567};
        long[] longs = {0x8123456789abcdefL, 0x0123456789abcdefL};

        for (ByteOrder order : new ByteOrder[]{ByteOrder.BIG_ENDIAN, ByteOrder.LITTLE_ENDIAN}) {
            ByteBuffer bytes = ByteBuffer.allocate(30).order(order);
            bytes.position(1);
            bytes.putShort(shorts[0]).putShort(shorts[1]);
            bytes.putInt(ints[0]).putInt(ints[1]);
            bytes.putLong(longs[0]).putLong(longs[1]);

            boolean littleEndian = order == ByteOrder.LITTLE_ENDIAN;
            short[] actualShorts = new short[shorts.length];
            int[] actualInts = new int[ints.length];
            long[] actualLongs = new long[longs.length];
            ByteArrayAccess.getShorts(bytes.array(), 1, actualShorts, littleEndian);
            ByteArrayAccess.getInts(bytes.array(), 5, actualInts, littleEndian);
            ByteArrayAccess.getLongs(bytes.array(), 13, actualLongs, littleEndian);

            Assert.assertArrayEquals(order.toString(), shorts, actualShorts);
            Assert.assertArrayEquals(order.toString(), ints, actualInts);
            Assert.assertArrayEquals(order.toString(), longs, actualLongs);
        }
    }

    @Test
    public void testGetFloatingPointVectorsInBothByteOrders() {
        int[] floatBits = {0x3fa00000, 0x80000000, 0x7fc01234};
        long[] doubleBits = {0xc004000000000000L, 0x8000000000000000L, 0x7ff8000000001234L};
        int doubleOffset = 1 + floatBits.length * Float.BYTES;

        for (ByteOrder order : new ByteOrder[]{ByteOrder.BIG_ENDIAN, ByteOrder.LITTLE_ENDIAN}) {
            ByteBuffer bytes = ByteBuffer.allocate(doubleOffset + doubleBits.length * Double.BYTES).order(order);
            bytes.position(1);
            for (int bits : floatBits) {
                bytes.putInt(bits);
            }
            for (long bits : doubleBits) {
                bytes.putLong(bits);
            }

            boolean littleEndian = order == ByteOrder.LITTLE_ENDIAN;
            float[] floats = new float[floatBits.length];
            double[] doubles = new double[doubleBits.length];
            ByteArrayAccess.getFloats(bytes.array(), 1, floats, littleEndian);
            ByteArrayAccess.getDoubles(bytes.array(), doubleOffset, doubles, littleEndian);

            for (int i = 0; i < floats.length; i++) {
                Assert.assertEquals(order.toString(), floatBits[i], Float.floatToRawIntBits(floats[i]));
            }
            for (int i = 0; i < doubles.length; i++) {
                Assert.assertEquals(order.toString(), doubleBits[i], Double.doubleToRawLongBits(doubles[i]));
            }
        }
    }

    @Test
    public void testPutVectorsAtUnalignedOffsets() {
        byte[] actual = new byte[66];
        short[] shorts = {(short)0x8123, 0x4567};
        int[] ints = {0x89abcdef, 0x01234567};
        long[] longs = {0x8123456789abcdefL, 0x0123456789abcdefL};
        float[] floats = {1.25f, -0.0f, Float.intBitsToFloat(0x7fc01234)};
        double[] doubles = {-2.5, -0.0, Double.longBitsToDouble(0x7ff8000000001234L)};

        ByteArrayAccess.putShortsLE(actual, 1, shorts);
        ByteArrayAccess.putIntsLE(actual, 5, ints);
        ByteArrayAccess.putLongsLE(actual, 13, longs);
        ByteArrayAccess.putFloatsLE(actual, 29, floats);
        ByteArrayAccess.putDoublesLE(actual, 41, doubles);

        ByteBuffer expected = ByteBuffer.allocate(actual.length).order(ByteOrder.LITTLE_ENDIAN);
        expected.position(1);
        expected.putShort(shorts[0]).putShort(shorts[1]);
        expected.putInt(ints[0]).putInt(ints[1]);
        expected.putLong(longs[0]).putLong(longs[1]);
        expected.putFloat(1.25f).putFloat(-0.0f).putInt(0x7fc00000);
        expected.putDouble(-2.5).putDouble(-0.0).putLong(0x7ff8000000000000L);
        Assert.assertArrayEquals(expected.array(), actual);
    }
}
