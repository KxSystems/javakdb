package kx;

import static org.junit.Assert.assertTrue;

import java.util.UUID;
import java.util.Arrays;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import org.junit.Test;
import org.junit.Assert;

/**
 * Unit test for c.java.
 */
public class cTest
{
    @Test
    public void testGetNullValuesFromArray()
    {
        Assert.assertNull(c.NULL[0]);
        Assert.assertEquals(false,c.NULL[1]);
        Assert.assertEquals(new UUID(0,0),c.NULL[2]);
        Assert.assertNull(c.NULL[3]);
        Assert.assertEquals(Byte.valueOf((byte)0),c.NULL[4]);
        Assert.assertEquals(Short.MIN_VALUE,c.NULL[5]);
        Assert.assertEquals(Integer.MIN_VALUE,c.NULL[6]);
        Assert.assertEquals(Long.MIN_VALUE,c.NULL[7]);
        Assert.assertEquals(Float.valueOf((float)Double.NaN),c.NULL[8]);
        Assert.assertEquals(Double.NaN,c.NULL[9]);
        Assert.assertEquals(' ',c.NULL[10]);
        Assert.assertEquals("",c.NULL[11]);
        Assert.assertEquals(new Timestamp(Long.MIN_VALUE),c.NULL[12]);
        Assert.assertEquals(new c.Month(Integer.MIN_VALUE),c.NULL[13]);
        Assert.assertEquals(new Date(Long.MIN_VALUE),c.NULL[14]);
        Assert.assertEquals(new java.util.Date(Long.MIN_VALUE),c.NULL[15]);
        Assert.assertEquals(new c.Timespan(Long.MIN_VALUE),c.NULL[16]);
        Assert.assertEquals(new c.Minute(Integer.MIN_VALUE),c.NULL[17]);
        Assert.assertEquals(new c.Second(Integer.MIN_VALUE),c.NULL[18]);
        Assert.assertEquals(new Time(Long.MIN_VALUE),c.NULL[19]);
    }

    @Test
    public void testGetNullValues()
    {
        Assert.assertNull(c.NULL(' '));
        Assert.assertEquals(false, c.NULL('b'));
        Assert.assertEquals(new UUID(0,0), c.NULL('g'));
        Assert.assertEquals(Byte.valueOf((byte)0), c.NULL('x'));
        Assert.assertEquals(Short.MIN_VALUE, c.NULL('h'));
        Assert.assertEquals(Integer.MIN_VALUE, c.NULL('i'));
        Assert.assertEquals(Long.MIN_VALUE, c.NULL('j'));
        Assert.assertEquals(Float.valueOf((float)Double.NaN), c.NULL('e'));
        Assert.assertEquals(Double.NaN, c.NULL('f'));
        Assert.assertEquals(' ', c.NULL('c'));
        Assert.assertEquals("", c.NULL('s'));
        Assert.assertEquals(new Timestamp(Long.MIN_VALUE), c.NULL('p'));
        Assert.assertEquals(new c.Month(Integer.MIN_VALUE), c.NULL('m'));
        Assert.assertEquals(new Date(Long.MIN_VALUE), c.NULL('d'));
        Assert.assertEquals(new java.util.Date(Long.MIN_VALUE), c.NULL('z'));
        Assert.assertEquals(new c.Timespan(Long.MIN_VALUE), c.NULL('n'));
        Assert.assertEquals(new c.Minute(Integer.MIN_VALUE), c.NULL('u'));
        Assert.assertEquals(new c.Second(Integer.MIN_VALUE), c.NULL('v'));
        Assert.assertEquals(new Time(Long.MIN_VALUE), c.NULL('t'));
    }

    @Test
    public void testIncorrectNullType()
    {
        try {
            c.NULL('a');
            Assert.fail("Expected an ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            // do nothing
        }
    }

    @Test
    public void testValueIsNull()
    {
        assertTrue( c.qn("") );
        Assert.assertEquals(false, c.qn(" "));
        assertTrue( c.qn(new Timestamp(Long.MIN_VALUE)));
        assertTrue( c.qn(new c.Month(Integer.MIN_VALUE)));
        assertTrue( c.qn(new Date(Long.MIN_VALUE)));
        assertTrue( c.qn(new java.util.Date(Long.MIN_VALUE)));
        assertTrue( c.qn(new c.Timespan(Long.MIN_VALUE)));
        assertTrue( c.qn(new c.Minute(Integer.MIN_VALUE)));
        assertTrue( c.qn(new c.Second(Integer.MIN_VALUE)));
        assertTrue( c.qn(new Time(Long.MIN_VALUE)) );
    }

    @Test
    public void testValueIsNotNull()
    {
        Assert.assertEquals(false, c.qn(" "));
        Assert.assertEquals(false, c.qn(new StringBuffer()));
    }

    @Test
    public void testGetAtomType()
    {
        Assert.assertEquals(-1, c.t(Boolean.FALSE));
        Assert.assertEquals(-2, c.t(new UUID(0,0)));
        Assert.assertEquals(-4, c.t(Byte.valueOf("1")));
        Assert.assertEquals(-5, c.t(Short.valueOf("1")));
        Assert.assertEquals(-6, c.t(Integer.valueOf(1111)));
        Assert.assertEquals(-7, c.t(Long.valueOf(1111)));
        Assert.assertEquals(-8, c.t(Float.valueOf(1.2f)));
        Assert.assertEquals(-9, c.t(Double.valueOf(1.2)));
        Assert.assertEquals(-10, c.t(Character.valueOf(' ')));
        Assert.assertEquals(-11, c.t(""));
        Assert.assertEquals(-14, c.t(new Date(Long.MIN_VALUE)));
        Assert.assertEquals(-19, c.t(new Time(Long.MIN_VALUE)));
        Assert.assertEquals(-12, c.t(new Timestamp(Long.MIN_VALUE)));
        Assert.assertEquals(-15, c.t(new java.util.Date(Long.MIN_VALUE)));
        Assert.assertEquals(-16, c.t(new c.Timespan(Long.MIN_VALUE)));
        Assert.assertEquals(-13, c.t(new c.Month(Integer.MIN_VALUE)));
        Assert.assertEquals(-17, c.t(new c.Minute(Integer.MIN_VALUE)));
        Assert.assertEquals(-18, c.t(new c.Second(Integer.MIN_VALUE)));
    }

    @Test
    public void testGetType()
    {
        Assert.assertEquals(1, c.t(new boolean[2]));
        Assert.assertEquals(2, c.t(new UUID[2]));
        Assert.assertEquals(4, c.t(new byte[2]));
        Assert.assertEquals(5, c.t(new short[2]));
        Assert.assertEquals(6, c.t(new int[2]));
        Assert.assertEquals(7, c.t(new long[2]));
        Assert.assertEquals(8, c.t(new float[2]));
        Assert.assertEquals(9, c.t(new double[2]));
        Assert.assertEquals(10, c.t(new char[2]));
        Assert.assertEquals(11, c.t(new String[2]));
        Assert.assertEquals(14, c.t(new Date[2]));
        Assert.assertEquals(19, c.t(new Time[2]));
        Assert.assertEquals(12, c.t(new Timestamp[2]));
        Assert.assertEquals(15, c.t(new java.util.Date[2]));
        Assert.assertEquals(16, c.t(new c.Timespan[2]));
        Assert.assertEquals(13, c.t(new c.Month[2]));
        Assert.assertEquals(17, c.t(new c.Minute[2]));
        Assert.assertEquals(18, c.t(new c.Second[2]));
        c.Dict dict = new c.Dict(new String[] {"Key"}, new String[][] {{"Value1","Value2","Value3"}});
        Assert.assertEquals(98, c.t(new c.Flip(dict)));
        Assert.assertEquals(99, c.t(dict));
    }

    @Test
    public void testGetUnknownType()
    {
        Assert.assertEquals(0, c.t(new StringBuffer()));
    }

    @Test
    public void testDictConstructor()
    {
        String[] x = new String[] {"Key"};
        String[][] y = new String[][] {{"Value1","Value2","Value3"}};
        c.Dict dict = new c.Dict(x, y);
        Assert.assertEquals(x, dict.x);
        Assert.assertEquals(y, dict.y);
    }

    @Test
    public void testFlipConstructor()
    {
        String[] x = new String[] {"Key"};
        String[][] y = new String[][] {{"Value1","Value2","Value3"}};
        c.Dict dict = new c.Dict(x, y);
        c.Flip flip = new c.Flip(dict);
        Assert.assertArrayEquals(x, flip.x);
        Assert.assertArrayEquals(y, flip.y);
    }

    @Test
    public void testFlipColumnPosition()
    {
        String[] x = new String[] {"Key"};
        String[][] y = new String[][] {{"Value1","Value2","Value3"}};
        c.Dict dict = new c.Dict(x, y);
        c.Flip flip = new c.Flip(dict);
        Assert.assertEquals(y[0], flip.at("Key"));
    }

    @Test
    public void testFlipRemoveKeyWithFlip()
    {
        try {
            String[] x = new String[] {"Key"};
            String[][] y = new String[][] {{"Value1","Value2","Value3"}};
            c.Dict dict = new c.Dict(x, y);
            c.Flip flip = new c.Flip(dict);
            c.Flip newflip = c.td(flip);
            Assert.assertEquals(flip, newflip);
        } catch (Exception e) {
            Assert.fail(e.toString());
        }

        try {
            String[] x = new String[] {"Key"};
            String[][] y = new String[][] {{"Value1","Value2","Value3"}};
            c.Dict dict = new c.Dict(x, y);
            c.Flip flip = new c.Flip(dict);
            c.Dict dictOfFlips = new c.Dict(flip, flip);
            c.Flip newflip = c.td(dictOfFlips);
            Assert.assertArrayEquals(new String[] {"Key","Key"}, newflip.x);
            Assert.assertArrayEquals(new String[][] {{"Value1","Value2","Value3"},{"Value1","Value2","Value3"}}, newflip.y);
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testFlipUnknownColumn()
    {
        String[] x = new String[] {"Key"};
        String[][] y = new String[][] {{"Value1","Value2","Value3"}};
        c.Dict dict = new c.Dict(x, y);
        c.Flip flip = new c.Flip(dict);
        try {
            flip.at("RUBBISH");
            Assert.fail("Expected an ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            // do nothing
        }
    }

    @Test
    public void testSerializeDeserializeBool()
    {
        kx.c c=new kx.c();
        Boolean input=Boolean.valueOf(true);
        try{
            Assert.assertEquals(input,(Boolean)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Boolean)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeUUID()
    {
        kx.c c=new kx.c();
        UUID input=new UUID(0,0);
        try{
            Assert.assertEquals(input,(UUID)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(UUID)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeByte()
    {
        kx.c c=new kx.c();
        Byte input=Byte.valueOf((byte)1);
        try{
            Assert.assertEquals(input,(Byte)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Byte)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeShort()
    {
        kx.c c=new kx.c();
        Short input=Short.valueOf((short)1);
        try{
            Assert.assertEquals(input,(Short)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Short)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeInteger()
    {
        kx.c c=new kx.c();
        Integer input=Integer.valueOf(77);
        try{
            Assert.assertEquals(input,(Integer)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Integer)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeLong()
    {
        kx.c c=new kx.c();
        Long input=Long.valueOf(77);
        try{
            Assert.assertEquals(input,(Long)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Long)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeFloat()
    {
        kx.c c=new kx.c();
        Float input=Float.valueOf((float)77.7);
        try{
            Assert.assertEquals(input,(Float)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Float)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeDouble()
    {
        kx.c c=new kx.c();
        Double input=Double.valueOf(77.7);
        try{
            Assert.assertEquals(input,(Double)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Double)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeCharacter()
    {
        kx.c c=new kx.c();
        Character input=Character.valueOf('a');
        try{
            Assert.assertEquals(input,(Character)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Character)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeString()
    {
        kx.c c=new kx.c();
        String input=new String("hello");
        try{
            Assert.assertEquals(input,(String)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(String)c.deserialize(c.serialize(1,input,true)));
            input="";
            Assert.assertEquals(input,(String)c.deserialize(c.serialize(1,input,true)));
            c.setEncoding("US-ASCII");
            Assert.assertEquals(input,(String)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeDate()
    {
        kx.c c=new kx.c();
        Date input=new Date(86400000000000L);
        try{
            Assert.assertEquals(input,(Date)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Date)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=new Date(Long.MIN_VALUE);
        try{
            Assert.assertEquals(input,(Date)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Date)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeTime()
    {
        kx.c c=new kx.c();
        Time input=new Time(55);
        try{
            Assert.assertEquals(input,(Time)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Time)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=new Time(Long.MIN_VALUE);
        try{
            Assert.assertEquals(input,(Time)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Time)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeTimestamp()
    {
        kx.c c=new kx.c();
        Timestamp input=new Timestamp(55);
        try{
            Assert.assertEquals(input,(Timestamp)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Timestamp)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=new Timestamp(Long.MIN_VALUE);
        try{
            Assert.assertEquals(input,(Timestamp)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Timestamp)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeUtilDate()
    {
        kx.c c=new kx.c();
        try{
            java.util.Date input=new java.text.SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990");
            Assert.assertEquals(input,(java.util.Date)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(java.util.Date)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeTimespan()
    {
        kx.c c=new kx.c();
        kx.c.Timespan input=new kx.c.Timespan(java.util.TimeZone.getDefault());
        try{
            Assert.assertEquals(input,(kx.c.Timespan)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(kx.c.Timespan)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeMonth()
    {
        kx.c c=new kx.c();
        kx.c.Month input=new kx.c.Month(55);
        try{
            Assert.assertEquals(input,(kx.c.Month)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(kx.c.Month)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeMinute()
    {
        kx.c c=new kx.c();
        kx.c.Minute input=new kx.c.Minute(55);
        try{
            Assert.assertEquals(input,(kx.c.Minute)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(kx.c.Minute)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeSecond()
    {
        kx.c c=new kx.c();
        kx.c.Second input=new kx.c.Second(55);
        try{
            Assert.assertEquals(input,(kx.c.Second)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(kx.c.Second)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeBoolArray()
    {
        kx.c c=new kx.c();
        boolean[]input=new boolean[500];
        try{
            assertTrue(Arrays.equals(input,(boolean[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(boolean[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeUUIDArray()
    {
        kx.c c=new kx.c();
        UUID[]input=new UUID[500];
        for(int i=0;i<input.length;i++)
            input[i]=new UUID(0,0);
        try{
            assertTrue(Arrays.equals(input,(UUID[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(UUID[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeByteArray()
    {
        kx.c c=new kx.c();
        byte[]input=new byte[500];
        try{
            assertTrue(Arrays.equals(input,(byte[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(byte[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeShortArray()
    {
        kx.c c=new kx.c();
        short[]input=new short[500];
        for(int i=0;i<input.length;i++)
            input[i]=(short)i;
        try{
            assertTrue(Arrays.equals(input,(short[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(short[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeIntArray()
    {
        kx.c c=new kx.c();
        int[]input=new int[50000];
        for(int i=0;i<input.length;i++)
            input[i]=i;
        try{
            assertTrue(Arrays.equals(input,(int[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(int[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeLongArray()
    {
        kx.c c=new kx.c();
        long[]input=new long[5000];
        for(int i=0;i<input.length;i++)
            input[i]=i%10;
        try{
            assertTrue(Arrays.equals(input,(long[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(long[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeFloatArray()
    {
        kx.c c=new kx.c();
        float[]input=new float[500];
        try{
            assertTrue(Arrays.equals(input,(float[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(float[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeDoubleArray()
    {
        kx.c c=new kx.c();
        double[]input=new double[500];
        try{
            assertTrue(Arrays.equals(input,(double[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(double[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeCharArray()
    {
        kx.c c=new kx.c();
        char[]input=new char[50];
        try{
            assertTrue(Arrays.equals(input,(char[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(char[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeStringArray()
    {
        kx.c c=new kx.c();
        String[]input=new String[50];
        for(int i=0;i<input.length;i++)
            input[i]="hello";
        try{
            assertTrue(Arrays.equals(input,(String[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(String[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeDateArray()
    {
        kx.c c=new kx.c();
        Date[]input=new Date[50];
        for(int i=0;i<input.length;i++)
            input[i]=new Date(86400000000000L);
        try{
            assertTrue(Arrays.equals(input,(Date[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(Date[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeTimeArray()
    {
        kx.c c=new kx.c();
        Time[]input=new Time[50];
        for(int i=0;i<input.length;i++)
            input[i]=new Time(1);
        try{
            assertTrue(Arrays.equals(input,(Time[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(Time[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeTimestampArray()
    {
        kx.c c=new kx.c();
        Timestamp[]input=new Timestamp[50];
        for(int i=0;i<input.length;i++)
            input[i]=new Timestamp(1);
        try{
            assertTrue(Arrays.equals(input,(Timestamp[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(Timestamp[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeUtilDateArray()
    {
        kx.c c=new kx.c();
        java.util.Date[]input=new java.util.Date[50];
        try{
            for(int i=0;i<input.length;i++)
                input[i]=new java.text.SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990");
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        try{
            assertTrue(Arrays.equals(input,(java.util.Date[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(java.util.Date[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeTimespanArray()
    {
        kx.c c=new kx.c();
        kx.c.Timespan[]input=new kx.c.Timespan[50];
        for(int i=0;i<input.length;i++)
            input[i]=new kx.c.Timespan(1);
        try{
            assertTrue(Arrays.equals(input,(kx.c.Timespan[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(kx.c.Timespan[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeMonthArray()
    {
        kx.c c=new kx.c();
        kx.c.Month[]input=new kx.c.Month[50];
        for(int i=0;i<input.length;i++)
            input[i]=new kx.c.Month(1);
        try{
            assertTrue(Arrays.equals(input,(kx.c.Month[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(kx.c.Month[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeMinuteArray()
    {
        kx.c c=new kx.c();
        kx.c.Minute[]input=new kx.c.Minute[50];
        for(int i=0;i<input.length;i++)
            input[i]=new kx.c.Minute(1);
        try{
            assertTrue(Arrays.equals(input,(kx.c.Minute[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(kx.c.Minute[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeSecondArray()
    {
        kx.c c=new kx.c();
        kx.c.Second[]input=new kx.c.Second[50];
        for(int i=0;i<input.length;i++)
            input[i]=new kx.c.Second(1);
        try{
            assertTrue(Arrays.equals(input,(kx.c.Second[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(kx.c.Second[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testCompressBoolList()
    {
        boolean[] data = new boolean[2000];
        for(int i=0;i<data.length;i++)
            data[i]=true;
        byte[] compressedBools = {(byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x26, (byte)0x00, (byte)0x00, (byte)0x07, (byte)0xde, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x07, (byte)0xd0, (byte)0x01, (byte)0x01, (byte)0xff, (byte)0x00, (byte)0xff, (byte)0x00, (byte)0xff, (byte)0x00, (byte)0xff, (byte)0x00, (byte)0xff, (byte)0x00, (byte)0xff, (byte)0x00, (byte)0xff, (byte)0x00, (byte)0xff, (byte)0x00, (byte)0xc5};
        kx.c c=new kx.c();
        try{
            byte[] compressed = c.serialize(0,data,true);
            assertTrue(Arrays.equals(compressed,compressedBools));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testDeserializeLittleEndInteger()
    {
        byte[] buff = {(byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x0d, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xfa, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00};
        kx.c c=new kx.c();
        try{
            Object res = c.deserialize(buff);
            Assert.assertEquals(Integer.valueOf(1),res);
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testDeserializeLittleEndLong()
    {
        byte[] buff = {(byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x11, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xf9, (byte)0x16, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};
        kx.c c=new kx.c();
        try{
            Object res = c.deserialize(buff);
            Assert.assertEquals(Long.valueOf(22),res);
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testDeserializeEmptyTable()
    {
        // response from executing '([] name:(); iq:())'
        byte[] buff = {(byte)0x01, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x2b, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x62, (byte)0x00, (byte)0x63, (byte)0x0b, (byte)0x00, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x6e, (byte)0x61, (byte)0x6d, (byte)0x65, (byte)0x00, (byte)0x69, (byte)0x71, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};
        kx.c c=new kx.c();
        try{
            Object res = c.deserialize(buff);

            String[] x = new String[] {"name", "iq"};
            String[][] y = new String[][] {{},{}};
            c.Dict dict = new c.Dict(x, y);
            c.Flip flip = new c.Flip(dict);
            Assert.assertArrayEquals(((c.Flip)res).x, flip.x);
            Assert.assertArrayEquals(((c.Flip)res).y, flip.y);
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testMonthToString()
    {
        c.Month mon = new c.Month(22);
        Assert.assertEquals("2001-11", mon.toString());
        mon = new c.Month(Integer.MIN_VALUE);
        Assert.assertEquals("", mon.toString());
    }

    @Test
    public void testMonthEquals()
    {
        c.Month mon1 = new c.Month(22);
        c.Month mon2 = new c.Month(22);
        c.Month mon3 = new c.Month(1);
        Assert.assertEquals(mon1,mon1);
        Assert.assertEquals(mon1,mon2);
        Assert.assertNotEquals(mon1,mon3);
        Assert.assertNotEquals(mon1,"test");
    }

    @Test
    public void testMonthHashCode()
    {
        c.Month mon1 = new c.Month(22);
        c.Month mon2 = new c.Month(22);
        c.Month mon3 = new c.Month(1);
        Assert.assertEquals(mon1.hashCode(),mon1.hashCode());
        Assert.assertEquals(mon1.hashCode(),mon2.hashCode());
        Assert.assertNotEquals(mon1.hashCode(),mon3.hashCode());
    }

    @Test
    public void testMonthCompareTo()
    {
        c.Month mon1 = new c.Month(22);
        c.Month mon2 = new c.Month(22);
        c.Month mon3 = new c.Month(1);
        Assert.assertEquals(0,mon1.compareTo(mon1));
        Assert.assertEquals(0,mon1.compareTo(mon2));
        Assert.assertEquals(21,mon1.compareTo(mon3));
    }

    @Test
    public void testMinuteToString()
    {
        c.Minute mon = new c.Minute(22);
        Assert.assertEquals("00:22", mon.toString());
        mon = new c.Minute(Integer.MIN_VALUE);
        Assert.assertEquals("", mon.toString());
    }

    @Test
    public void testMinuteEquals()
    {
        c.Minute mon1 = new c.Minute(22);
        c.Minute mon2 = new c.Minute(22);
        c.Minute mon3 = new c.Minute(1);
        Assert.assertEquals(mon1,mon1);
        Assert.assertEquals(mon1,mon2);
        Assert.assertNotEquals(mon1,mon3);
        Assert.assertNotEquals(mon1,"test");
    }

    @Test
    public void testMinuteHashCode()
    {
        c.Minute mon1 = new c.Minute(22);
        c.Minute mon2 = new c.Minute(22);
        c.Minute mon3 = new c.Minute(1);
        Assert.assertEquals(mon1.hashCode(),mon1.hashCode());
        Assert.assertEquals(mon1.hashCode(),mon2.hashCode());
        Assert.assertNotEquals(mon1.hashCode(),mon3.hashCode());
    }

    @Test
    public void testMinuteCompareTo()
    {
        c.Minute mon1 = new c.Minute(22);
        c.Minute mon2 = new c.Minute(22);
        c.Minute mon3 = new c.Minute(1);
        Assert.assertEquals(0,mon1.compareTo(mon1));
        Assert.assertEquals(0,mon1.compareTo(mon2));
        Assert.assertEquals(21,mon1.compareTo(mon3));
    }

    @Test
    public void testSecondToString()
    {
        c.Second mon = new c.Second(22);
        Assert.assertEquals("00:00:22", mon.toString());
        mon = new c.Second(Integer.MIN_VALUE);
        Assert.assertEquals("", mon.toString());
    }

    @Test
    public void testSecondEquals()
    {
        c.Second mon1 = new c.Second(22);
        c.Second mon2 = new c.Second(22);
        c.Second mon3 = new c.Second(1);
        Assert.assertEquals(mon1,mon1);
        Assert.assertEquals(mon1,mon2);
        Assert.assertNotEquals(mon1,mon3);
        Assert.assertNotEquals(mon1,"test");
    }

    @Test
    public void testSecondHashCode()
    {
        c.Second mon1 = new c.Second(22);
        c.Second mon2 = new c.Second(22);
        c.Second mon3 = new c.Second(1);
        Assert.assertEquals(mon1.hashCode(),mon1.hashCode());
        Assert.assertEquals(mon1.hashCode(),mon2.hashCode());
        Assert.assertNotEquals(mon1.hashCode(),mon3.hashCode());
    }

    @Test
    public void testSecondCompareTo()
    {
        c.Second mon1 = new c.Second(22);
        c.Second mon2 = new c.Second(22);
        c.Second mon3 = new c.Second(1);
        Assert.assertEquals(0,mon1.compareTo(mon1));
        Assert.assertEquals(0,mon1.compareTo(mon2));
        Assert.assertEquals(21,mon1.compareTo(mon3));
    }

    @Test
    public void testTimespanToString()
    {
        c.Timespan mon = new c.Timespan(22);
        Assert.assertEquals("00:00:00.000000022", mon.toString());
        mon = new c.Timespan(-22);
        Assert.assertEquals("-00:00:00.000000022", mon.toString());
        mon = new c.Timespan(0);
        Assert.assertEquals("00:00:00.000000000", mon.toString());
        mon = new c.Timespan(86400000000000L);
        Assert.assertEquals("1D00:00:00.000000000", mon.toString());
        mon = new c.Timespan(Long.MIN_VALUE);
        Assert.assertEquals("", mon.toString());
    }

    @Test
    public void testTimespanEquals()
    {
        c.Timespan mon1 = new c.Timespan(22);
        c.Timespan mon2 = new c.Timespan(22);
        c.Timespan mon3 = new c.Timespan();
        Assert.assertEquals(mon1,mon1);
        Assert.assertEquals(mon1,mon2);
        Assert.assertNotEquals(mon1,mon3);
        Assert.assertNotEquals(mon1,"test");
    }

    @Test
    public void testTimespanHashCode()
    {
        c.Timespan mon1 = new c.Timespan(22);
        c.Timespan mon2 = new c.Timespan(22);
        c.Timespan mon3 = new c.Timespan();
        Assert.assertEquals(mon1.hashCode(),mon1.hashCode());
        Assert.assertEquals(mon1.hashCode(),mon2.hashCode());
        Assert.assertNotEquals(mon1.hashCode(),mon3.hashCode());
    }

    @Test
    public void testTimespanCompareTo()
    {
        c.Timespan mon1 = new c.Timespan(22);
        c.Timespan mon2 = new c.Timespan(22);
        c.Timespan mon3 = new c.Timespan(1);
        c.Timespan mon4 = new c.Timespan(-1);
        Assert.assertEquals(0,mon1.compareTo(mon1));
        Assert.assertEquals(0,mon1.compareTo(mon2));
        Assert.assertEquals(1,mon1.compareTo(mon3));
        Assert.assertEquals(-1,mon4.compareTo(mon1));
    }

    @Test
    public void testSerializeStringLen()
    {
        try {
            Assert.assertEquals(0,c.ns(null));
        } catch (Exception e){
            Assert.fail(e.toString());
        }
        try {
            Assert.assertEquals(2,c.ns("hi"));
        } catch (Exception e){
            Assert.fail(e.toString());
        }
        try {
            char[] ch = {'g', 'o', (char)0, 'd', ' ', 'm', 'o', 'r', 'n', 'i', 'n', 'g'};
            String str = new String(ch);
            Assert.assertEquals(2,c.ns(str));
        } catch (Exception e){
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testGetMsgHandler(){
        kx.c c=new kx.c();
        Assert.assertEquals(null,c.getMsgHandler());
    }

    @Test
    public void testClose(){
        kx.c c=new kx.c();
        try {
            c.close();
            c.close();
        } catch (Exception e){
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testLogToStdOut(){
        try {
            kx.c.tm();
            kx.c.tm();
            kx.c.O(true);
            kx.c.O(0);
            kx.c.O(0L);
            kx.c.O(0.0);
            kx.c.O("");
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testGetObjectAtIndex(){
        String[] x = new String[] {"Key"};
        Object found = c.at(x,0);
        Assert.assertEquals(x[0],found);
    }

    @Test
    public void testGetNullObjectAtIndex(){
        String[] x = new String[] {""};
        Object found = c.at(x,0);
        Assert.assertEquals(null,found);
    }

    @Test
    public void testSetObjectAtIndex(){
        String[] x = new String[] {"Key"};
        c.set(x,0,"Value");
        Assert.assertArrayEquals(new String[]{"Value"},x);
    }

    @Test
    public void testSetNullObjectAtIndex(){
        String[] x = new String[] {"Key"};
        c.set(x,0,null);
        Assert.assertArrayEquals(new String[]{""},x);
    }

    @Test
    public void testBytesRequiredForDict(){
        c.Dict dict = new c.Dict(new String[] {"Key"}, new String[][] {{"Value1","Value2","Value3"}});
        kx.c c=new kx.c();
        try {
            Assert.assertEquals(44,c.nx(dict));
        } catch (Exception e){
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testBytesRequiredForFlip(){
        c.Dict dict = new c.Dict(new String[] {"Key"}, new String[][] {{"Value1","Value2","Value3"}});
        c.Flip flip = new c.Flip(dict);
        kx.c c=new kx.c();
        try {
            Assert.assertEquals(46,c.nx(flip));
        } catch (Exception e){
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testElementsInObject(){
        try {
            char[] ch = {'g', 'o'};
            Assert.assertEquals(2,c.n(ch));
            int[] ints = {1,2};
            Assert.assertEquals(2,c.n(ints));
            c.Dict dict = new c.Dict(new String[] {"Key"}, new String[][] {{"Value1","Value2","Value3"}});
            Assert.assertEquals(1,c.n(dict));
            c.Flip flip = new c.Flip(dict);
            Assert.assertEquals(3,c.n(flip));
        } catch (Exception e){
            Assert.fail(e.toString());
        }
    }

    class DefaultMsgHandler implements c.MsgHandler
    {
    }

    @Test
    public void testDefaultMsgHandler(){
        DefaultMsgHandler msgHandler = new DefaultMsgHandler();
        kx.c c=new kx.c();
        try {
            msgHandler.processMsg(c,(byte)0,"test");
        } catch (Exception e){
            Assert.fail(e.toString());
        }
        try {
            msgHandler.processMsg(c,(byte)6,"test");
            Assert.fail("Expected an IOException to be thrown");
        } catch (Exception e){
            // do nothing, exception expected
        }
    }

    @Test
    public void testSetMsgHandler(){
        DefaultMsgHandler msgHandler = new DefaultMsgHandler();
        kx.c c=new kx.c();
        Assert.assertEquals(null,c.getMsgHandler());
        c.setMsgHandler(msgHandler);
        Assert.assertEquals(msgHandler,c.getMsgHandler());
    }
}
