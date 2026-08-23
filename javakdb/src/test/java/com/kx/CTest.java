package com.kx;

import static org.junit.Assert.assertTrue;

import java.util.UUID;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.time.LocalTime;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Unit test for c.java.
 */
public class CTest
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
        Assert.assertEquals(Instant.MIN,c.NULL[12]);
        Assert.assertEquals(new c.Month(Integer.MIN_VALUE),c.NULL[13]);
        Assert.assertEquals(LocalDate.MIN,c.NULL[14]);
        Assert.assertEquals(LocalDateTime.MIN,c.NULL[15]);
        Assert.assertEquals(new c.Timespan(Long.MIN_VALUE),c.NULL[16]);
        Assert.assertEquals(new c.Minute(Integer.MIN_VALUE),c.NULL[17]);
        Assert.assertEquals(new c.Second(Integer.MIN_VALUE),c.NULL[18]);
        Assert.assertEquals(com.kx.c.LOCAL_TIME_NULL,c.NULL[19]);
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
        Assert.assertEquals(Instant.MIN, c.NULL('p'));
        Assert.assertEquals(new c.Month(Integer.MIN_VALUE), c.NULL('m'));
        Assert.assertEquals(LocalDate.MIN, c.NULL('d'));
        Assert.assertEquals(LocalDateTime.MIN, c.NULL('z'));
        Assert.assertEquals(new c.Timespan(Long.MIN_VALUE), c.NULL('n'));
        Assert.assertEquals(new c.Minute(Integer.MIN_VALUE), c.NULL('u'));
        Assert.assertEquals(new c.Second(Integer.MIN_VALUE), c.NULL('v'));
        Assert.assertEquals(com.kx.c.LOCAL_TIME_NULL, c.NULL('t'));
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
        assertTrue( c.qn(Instant.MIN));
        assertTrue( c.qn(new c.Month(Integer.MIN_VALUE)));
        assertTrue( c.qn(LocalDate.MIN));
        assertTrue( c.qn(LocalDateTime.MIN));
        assertTrue( c.qn(new c.Timespan(Long.MIN_VALUE)));
        assertTrue( c.qn(new c.Minute(Integer.MIN_VALUE)));
        assertTrue( c.qn(new c.Second(Integer.MIN_VALUE)));
        assertTrue( c.qn(com.kx.c.LOCAL_TIME_NULL) );
        assertTrue( c.qn(com.kx.c.NULL('g')));
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
        Assert.assertEquals(-14, c.t(LocalDate.MIN));
        Assert.assertEquals(-19, c.t(com.kx.c.LOCAL_TIME_NULL));
        Assert.assertEquals(-12, c.t(Instant.MIN));
        Assert.assertEquals(-15, c.t(LocalDateTime.MIN));
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
        Assert.assertEquals(14, c.t(new LocalDate[2]));
        Assert.assertEquals(19, c.t(new LocalTime[2]));
        Assert.assertEquals(12, c.t(new Instant[2]));
        Assert.assertEquals(15, c.t(new LocalDateTime[2]));
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
        com.kx.c c=new com.kx.c();
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
        com.kx.c c=new com.kx.c();
        UUID input=new UUID(6666666,7777777);
        try{
            Assert.assertEquals(input,(UUID)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(UUID)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=new UUID(0,0);
        try{
            Assert.assertEquals(input,(UUID)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(UUID)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        c.ipcVersion=2;
        try{
            c.serialize(1,input,false);
            Assert.fail("Expected a RuntimeException to be thrown");
        } catch(RuntimeException e) {
            // expected
        }
        catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeByte()
    {
        com.kx.c c=new com.kx.c();
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
        com.kx.c c=new com.kx.c();
        Short input=Short.valueOf((short)1);
        try{
            Assert.assertEquals(input,(Short)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Short)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=Short.MIN_VALUE;
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
        com.kx.c c=new com.kx.c();
        Integer input=Integer.valueOf(77);
        try{
            Assert.assertEquals(input,(Integer)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Integer)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=Integer.MIN_VALUE;
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
        com.kx.c c=new com.kx.c();
        Long input=Long.valueOf(77);
        try{
            Assert.assertEquals(input,(Long)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Long)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=Long.MIN_VALUE;
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
        com.kx.c c=new com.kx.c();
        Float input=Float.valueOf((float)77.7);
        try{
            Assert.assertEquals(input,(Float)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Float)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=Float.valueOf((float)Double.NaN);
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
        com.kx.c c=new com.kx.c();
        Double input=Double.valueOf(77.7);
        try{
            Assert.assertEquals(input,(Double)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Double)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=Double.NaN;
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
        com.kx.c c=new com.kx.c();
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
        com.kx.c c=new com.kx.c();
        String input=new String("hello");
        try{
            Assert.assertEquals(input,(String)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(String)c.deserialize(c.serialize(1,input,true)));
            input="";
            Assert.assertEquals(input,(String)c.deserialize(c.serialize(1,input,true)));
            com.kx.c.setEncoding("US-ASCII");
            Assert.assertEquals(input,(String)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeLocalDate()
    {
        com.kx.c c=new com.kx.c();
        LocalDate input=LocalDate.ofEpochDay(Integer.MAX_VALUE);
        try{
            Assert.assertEquals(input,(LocalDate)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(LocalDate)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=LocalDate.MIN;
        try{
            Assert.assertEquals(input,(LocalDate)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(LocalDate)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        try{
            Assert.assertEquals(com.kx.c.NULL[14],(LocalDate)c.deserialize(c.serialize(1,com.kx.c.NULL[14],true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=LocalDate.ofEpochDay(Integer.MAX_VALUE+1);
        try{
            c.serialize(1,input,true);
            Assert.fail("Expected a RuntimeException to be thrown");
        } catch(RuntimeException e) {
            // expected
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=LocalDate.ofEpochDay(Integer.MIN_VALUE-1L-com.kx.c.DAYS_BETWEEN_1970_2000);
        try{
            c.serialize(1,input,true);
            Assert.fail("Expected a RuntimeException to be thrown");
        } catch(RuntimeException e) {
            // expected
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeTime()
    {
        com.kx.c c=new com.kx.c();
        LocalTime input=LocalTime.of(12,10,1,1000000*5);
        try{
            Assert.assertEquals(input,(LocalTime)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(LocalTime)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=com.kx.c.LOCAL_TIME_NULL;
        try{
            Assert.assertEquals(input,(LocalTime)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(LocalTime)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testDeserializeTimeBeyond24Hours() throws c.KException, UnsupportedEncodingException {
        // Serialized payload: 43:12:34.567 => 155,554,567ms
        byte[] incomingMsg = new byte[]{1, 2, 0, 0, 13, 0, 0, 0, -19, 7, -109, 69, 9};
        com.kx.c c = new com.kx.c();
        LocalTime actual = (LocalTime) c.deserialize(incomingMsg);
        LocalTime expected = LocalTime.of(19, 12, 34, 567_000_000);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeTimeNegative() throws c.KException, UnsupportedEncodingException {
        // Serialized payload: -23:12:34.567 -> -83,554,567ms
        byte[] payload = new byte[]{1, 2, 0, 0, 13, 0, 0, 0, -19, -7, 14, 5, -5};
        com.kx.c c = new com.kx.c();
        LocalTime actual = (LocalTime) c.deserialize(payload);
        LocalTime expected = LocalTime.of(23, 12, 34, 567_000_000);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeDeserializeInstant()
    {
        com.kx.c c=new com.kx.c();
        Instant input=Instant.ofEpochSecond(55,1212);
        try{
            Assert.assertEquals(input,(Instant)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Instant)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=Instant.ofEpochMilli(86400000L*10957L+10);
        try{
            Assert.assertEquals(input,(Instant)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Instant)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=Instant.MIN;
        try{
            Assert.assertEquals(input,(Instant)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(Instant)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        c.ipcVersion=0;
        try{
            c.serialize(1,input,false);
            Assert.fail("Expected a RuntimeException to be thrown");
        } catch(RuntimeException e) {
            // expected
        }
        catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeUtilDate()
    {
        com.kx.c c=new com.kx.c();
        try{
            LocalDateTime input = LocalDateTime.of(1990,1,1,10,30,59,1000000);
            Assert.assertEquals(input,(LocalDateTime)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(LocalDateTime)c.deserialize(c.serialize(1,input,true)));

            input = LocalDateTime.of(1990,1,1,10,30,59,1900000);
            LocalDateTime output = LocalDateTime.of(1990,1,1,10,30,59,1000000);
            Assert.assertEquals(output,(LocalDateTime)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(output,(LocalDateTime)c.deserialize(c.serialize(1,input,true)));

            input = LocalDateTime.of(1990,1,1,10,30,59,1);
            output = LocalDateTime.of(1990,1,1,10,30,59,0);
            Assert.assertEquals(output,(LocalDateTime)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(output,(LocalDateTime)c.deserialize(c.serialize(1,input,true)));

            input = LocalDateTime.of(1990,1,1,10,30,59,500000);
            Assert.assertEquals(output,(LocalDateTime)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(output,(LocalDateTime)c.deserialize(c.serialize(1,input,true)));

            input = LocalDateTime.of(1990,1,1,10,30,59,999999);
            Assert.assertEquals(output,(LocalDateTime)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(output,(LocalDateTime)c.deserialize(c.serialize(1,input,true)));

            Assert.assertEquals(com.kx.c.NULL[15],(LocalDateTime)c.deserialize(c.serialize(1,com.kx.c.NULL[15],true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeTimespan()
    {
        com.kx.c c=new com.kx.c();
        com.kx.c.Timespan input=new com.kx.c.Timespan(java.util.TimeZone.getDefault());
        try{
            Assert.assertEquals(input,(com.kx.c.Timespan)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(com.kx.c.Timespan)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=new com.kx.c.Timespan(Long.MIN_VALUE);
        try{
            Assert.assertEquals(input,(com.kx.c.Timespan)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(com.kx.c.Timespan)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        c.ipcVersion=0;
        try{
            c.serialize(1,input,false);
            Assert.fail("Expected a RuntimeException to be thrown");
        } catch(RuntimeException e) {
            // expected
        }
        catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeMonth()
    {
        com.kx.c c=new com.kx.c();
        com.kx.c.Month input=new com.kx.c.Month(55);
        try{
            Assert.assertEquals(input,(com.kx.c.Month)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(com.kx.c.Month)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=new com.kx.c.Month(Integer.MIN_VALUE);
        try{
            Assert.assertEquals(input,(com.kx.c.Month)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(com.kx.c.Month)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeMinute()
    {
        com.kx.c c=new com.kx.c();
        com.kx.c.Minute input=new com.kx.c.Minute(55);
        try{
            Assert.assertEquals(input,(com.kx.c.Minute)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(com.kx.c.Minute)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=new com.kx.c.Minute(Integer.MIN_VALUE);
        try{
            Assert.assertEquals(input,(com.kx.c.Minute)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(com.kx.c.Minute)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeSecond()
    {
        com.kx.c c=new com.kx.c();
        com.kx.c.Second input=new com.kx.c.Second(55);
        try{
            Assert.assertEquals(input,(com.kx.c.Second)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(com.kx.c.Second)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        input=new com.kx.c.Second(Integer.MIN_VALUE);
        try{
            Assert.assertEquals(input,(com.kx.c.Second)c.deserialize(c.serialize(1,input,false)));
            Assert.assertEquals(input,(com.kx.c.Second)c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeObjectArray()
    {
        com.kx.c c=new com.kx.c();
        long[] al=new long[100];
        int[] ai=new int[100];
        short[] as=new short[190];
        for(short i=0;i<100;i++){
            al[i]=i;
            ai[i]=i;
            as[i]=i;
        }
        Object[]input=new Object[]{Long.valueOf(77),Integer.valueOf(22),al,ai,as,Integer.valueOf(2)};
        try{
            assertTrue(Arrays.deepEquals(input,(Object[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.deepEquals(input,(Object[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeGenericNull()
    {
        com.kx.c c=new com.kx.c();
        try{
            // null maps to the kdb+ generic null (::), whose type number is 101
            Assert.assertEquals(101,com.kx.c.t(null));
            // a bare null serializes as the kdb+ generic null (::) and round-trips back to null
            Assert.assertNull(c.deserialize(c.serialize(1,null,false)));
            // a null element within a general list (as received from kdb+ for e.g. (1;::;3)) round-trips
            Object[] input={Long.valueOf(1),null,Integer.valueOf(3)};
            Assert.assertArrayEquals(input,(Object[])c.deserialize(c.serialize(1,input,false)));
            Assert.assertArrayEquals(input,(Object[])c.deserialize(c.serialize(1,input,true)));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeBoolArray()
    {
        com.kx.c c=new com.kx.c();
        boolean[]input=new boolean[500];
        for(int i=0;i<input.length;i++)
            input[i]=(i%2==0);
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
        com.kx.c c=new com.kx.c();
        UUID[]input=new UUID[500];
        input[0]=new UUID(0,0);
        for(int i=1;i<input.length;i++)
            input[i]=UUID.randomUUID();
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
        com.kx.c c=new com.kx.c();
        byte[]input=new byte[500];
        for(int i=0;i<input.length;i++)
            input[i]=(byte)(i%100);
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
        com.kx.c c=new com.kx.c();
        short[]input=new short[500];
        input[0]=Short.MIN_VALUE;
        for(int i=1;i<input.length;i++)
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
        com.kx.c c=new com.kx.c();
        int[]input=new int[50000];
        input[0]=Integer.MIN_VALUE;
        for(int i=1;i<input.length;i++)
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
        com.kx.c c=new com.kx.c();
        long[]input=new long[5000];
        input[0]=Long.MIN_VALUE;
        for(int i=1;i<input.length;i++)
            input[i]=i;
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
        com.kx.c c=new com.kx.c();
        float[]input=new float[500];
        input[0]=Float.valueOf((float)Double.NaN);
        for(int i=1;i<input.length;i++)
            input[i]=((float)i)+0.001f;
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
        com.kx.c c=new com.kx.c();
        double[]input=new double[500];
        input[0]=Double.NaN;
        for(int i=1;i<input.length;i++)
            input[i]=((double)i)+0.0001d;
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
        com.kx.c c=new com.kx.c();
        char[]input=new char[50];
        for(int i=0;i<input.length;i++)
            input[i]=(char)(i%100);
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
        com.kx.c c=new com.kx.c();
        String[]input=new String[50];
        input[0]="";
        for(int i=1;i<input.length;i++)
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
        com.kx.c c=new com.kx.c();
        LocalDate[]input=new LocalDate[50];
        input[0]=LocalDate.MIN;
        for(int i=1;i<input.length;i++)
            input[i]=LocalDate.ofEpochDay(Integer.MAX_VALUE);
        try{
            assertTrue(Arrays.equals(input,(LocalDate[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(LocalDate[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeTimeArray()
    {
        com.kx.c c=new com.kx.c();
        LocalTime[]input=new LocalTime[50];
        input[0]=com.kx.c.LOCAL_TIME_NULL;
        for(int i=1;i<input.length;i++)
            input[i]=LocalDateTime.ofInstant(Instant.ofEpochMilli(1),ZoneId.of("UTC")).toLocalTime();
        try{
            assertTrue(Arrays.equals(input,(LocalTime[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(LocalTime[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeInstantArray()
    {
        com.kx.c c=new com.kx.c();
        Instant[]input=new Instant[50];
        input[0]=Instant.MIN;
        for(int i=1;i<input.length;i++)
            input[i]=Instant.ofEpochMilli(1);
        try{
            assertTrue(Arrays.equals(input,(Instant[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(Instant[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeUtilDateArray()
    {
        com.kx.c c=new com.kx.c();
        LocalDateTime[]input=new LocalDateTime[50];
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            input[0]=LocalDateTime.MIN;
            for(int i=1;i<input.length;i++)
                input[i]=LocalDate.parse("1990-01-01", formatter).atStartOfDay();
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        try{
            assertTrue(Arrays.equals(input,(LocalDateTime[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(LocalDateTime[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeTimespanArray()
    {
        com.kx.c c=new com.kx.c();
        com.kx.c.Timespan[]input=new com.kx.c.Timespan[50];
        input[0]=new com.kx.c.Timespan(Long.MIN_VALUE);
        for(int i=1;i<input.length;i++)
            input[i]=new com.kx.c.Timespan(1);
        try{
            assertTrue(Arrays.equals(input,(com.kx.c.Timespan[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(com.kx.c.Timespan[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeMonthArray()
    {
        com.kx.c c=new com.kx.c();
        com.kx.c.Month[]input=new com.kx.c.Month[50];
        input[0]=new com.kx.c.Month(Integer.MIN_VALUE);
        for(int i=1;i<input.length;i++)
            input[i]=new com.kx.c.Month(1);
        try{
            assertTrue(Arrays.equals(input,(com.kx.c.Month[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(com.kx.c.Month[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeMinuteArray()
    {
        com.kx.c c=new com.kx.c();
        com.kx.c.Minute[]input=new com.kx.c.Minute[50];
        input[0]=new com.kx.c.Minute(Integer.MIN_VALUE);
        for(int i=1;i<input.length;i++)
            input[i]=new com.kx.c.Minute(1);
        try{
            assertTrue(Arrays.equals(input,(com.kx.c.Minute[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(com.kx.c.Minute[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeSecondArray()
    {
        com.kx.c c=new com.kx.c();
        com.kx.c.Second[]input=new com.kx.c.Second[50];
        input[0]=new com.kx.c.Second(Integer.MIN_VALUE);
        for(int i=1;i<input.length;i++)
            input[i]=new com.kx.c.Second(i);
        try{
            assertTrue(Arrays.equals(input,(com.kx.c.Second[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(com.kx.c.Second[])c.deserialize(c.serialize(1,input,true))));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeDict()
    {
        String[] x = new String[] {"Key1","Key2"};
        String[] y = new String[] {"Value1","Value2"};
        c.Dict dict = new c.Dict(x, y);
        com.kx.c c=new com.kx.c();
        try{
            c.Dict dict2=(c.Dict)c.deserialize(c.serialize(1,dict,false));
            assertTrue(Arrays.equals(x,(String[])dict2.x));
            assertTrue(Arrays.equals(y,(String[])dict2.y));
            dict2=(c.Dict)c.deserialize(c.serialize(1,dict,true));
            assertTrue(Arrays.equals(x,(String[])dict2.x));
            assertTrue(Arrays.equals(y,(String[])dict2.y));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSerializeDeserializeFlip()
    {
        String[] x = new String[] {"Key1"};
        String[][] y = new String[][] {{"Value1","Value2"}};
        c.Flip flip = new c.Flip(new c.Dict(x, y));
        com.kx.c c=new com.kx.c();
        try{
            c.Flip flip2=(c.Flip)c.deserialize(c.serialize(1,flip,false));
            assertTrue(Arrays.equals(x, flip2.x));
            assertTrue(Arrays.equals(y[0],(String[])flip2.y[0]));
            flip2=(c.Flip)c.deserialize(c.serialize(1,flip,true));
            assertTrue(Arrays.equals(x, flip2.x));
            assertTrue(Arrays.equals(y[0],(String[])flip2.y[0]));
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
        byte[] compressedBools = {(byte)0x01, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x26, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xde, (byte)0x07, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0xd0, (byte)0x07, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x01, (byte)0xff, (byte)0x00, (byte)0xff, (byte)0x00, (byte)0xff, (byte)0x00, (byte)0xff, (byte)0x00, (byte)0xff, (byte)0x00, (byte)0xff, (byte)0x00, (byte)0xff, (byte)0x00, (byte)0xff, (byte)0x00, (byte)0xc5};
        com.kx.c c=new com.kx.c();
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
        com.kx.c c=new com.kx.c();
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
        com.kx.c c=new com.kx.c();
        try{
            Object res = c.deserialize(buff);
            Assert.assertEquals(Long.valueOf(22),res);
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testDeserializeUnsupportedType()
    {
        // A kdb+ enumerated (type 20) — or any unhandled — vector must fail with a clear error rather
        // than silently returning null and leaving the read buffer misaligned (which would corrupt the
        // remainder of any containing list/dict/table).
        byte[] buff = {(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00, (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x16, (byte)0x14,(byte)0x00, (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x02, (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x64, (byte)0x00,(byte)0x00,(byte)0x00,(byte)0xC8};
        com.kx.c c=new com.kx.c();
        try {
            c.deserialize(buff);
            Assert.fail("Expected a RuntimeException for the unsupported type");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage()!=null && e.getMessage().contains("20"));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testDeserializeEmptyTable()
    {
        // response from executing '([] name:(); iq:())'
        byte[] buff = {(byte)0x01, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x2b, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x62, (byte)0x00, (byte)0x63, (byte)0x0b, (byte)0x00, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x6e, (byte)0x61, (byte)0x6d, (byte)0x65, (byte)0x00, (byte)0x69, (byte)0x71, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};
        com.kx.c c=new com.kx.c();
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
    public void testDeserializeSplayedTableReference()
    {
        // response from executing 'get `:test/ set ([] a:til 10)'
        // A splayed (on-disk) table is transmitted as table(98) -> dict(99) whose value is the
        // file-path symbol `:test/ (type -11) rather than the column data, so it cannot be
        // represented as an in-memory Flip. This should surface as a clear IllegalArgumentException
        // rather than an opaque ClassCastException (issue #51).
        byte[] buff = {(byte)0x01, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x1b, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x62, (byte)0x00, (byte)0x63, (byte)0x0b, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x61, (byte)0x00, (byte)0xf5, (byte)0x3a, (byte)0x74, (byte)0x65, (byte)0x73, (byte)0x74, (byte)0x2f, (byte)0x00};
        com.kx.c c=new com.kx.c();
        try {
            c.deserialize(buff);
            Assert.fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains(":test/"));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testFlipFromDictWithNonColumnValues()
    {
        // The same guard also protects against a Dict that was simply constructed with values that are
        // not column arrays (i.e. a caller bug, not a splayed table); the message describes the actual
        // condition rather than assuming a specific cause.
        c.Dict dict = new c.Dict(new String[]{"a"}, "not-a-column-list");
        try {
            new c.Flip(dict);
            Assert.fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Object[]"));
        }
    }

    @Test
    public void testDeserializeUnterminatedSymbol()
    {
        // A symbol whose bytes run to the end of the message with no null terminator must surface as a
        // clear "malformed message" error rather than an opaque ArrayIndexOutOfBoundsException.
        byte[] buff = {(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00, (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0c, (byte)0xf5, (byte)0x61,(byte)0x62,(byte)0x63};
        com.kx.c c=new com.kx.c();
        try {
            c.deserialize(buff);
            Assert.fail("Expected a RuntimeException for the unterminated symbol");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage()!=null && e.getMessage().toLowerCase().contains("malformed"));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testMonthToString()
    {
        c.Month mon = new c.Month(22);
        Assert.assertEquals("2001-11", mon.toString());
        mon = new c.Month(-1);
        Assert.assertEquals("1999-12", mon.toString());
        // 0Wm (Integer.MAX_VALUE) must not overflow i+24000 into a garbage year
        mon = new c.Month(Integer.MAX_VALUE);
        Assert.assertEquals("178958970-08", mon.toString());
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
        Assert.assertEquals(1,mon1.compareTo(mon3));
        // must not overflow: 0Wm (MAX) is greater than 0Nm (MIN)
        Assert.assertEquals(1,new c.Month(Integer.MAX_VALUE).compareTo(new c.Month(Integer.MIN_VALUE)));
        Assert.assertEquals(-1,new c.Month(Integer.MIN_VALUE).compareTo(new c.Month(Integer.MAX_VALUE)));
    }

    @Test
    public void testMinuteToString()
    {
        c.Minute mon = new c.Minute(22);
        Assert.assertEquals("00:22", mon.toString());
        mon = new c.Minute(1500);
        Assert.assertEquals("25:00", mon.toString()); // minute can exceed 24h
        mon = new c.Minute(-30);
        Assert.assertEquals("-00:30", mon.toString());
        mon = new c.Minute(-90);
        Assert.assertEquals("-01:30", mon.toString());
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
        Assert.assertEquals(1,mon1.compareTo(mon3));
        // must not overflow: 0Wu (MAX) is greater than 0Nu (MIN)
        Assert.assertEquals(1,new c.Minute(Integer.MAX_VALUE).compareTo(new c.Minute(Integer.MIN_VALUE)));
        Assert.assertEquals(-1,new c.Minute(Integer.MIN_VALUE).compareTo(new c.Minute(Integer.MAX_VALUE)));
    }

    @Test
    public void testSecondToString()
    {
        c.Second mon = new c.Second(22);
        Assert.assertEquals("00:00:22", mon.toString());
        mon = new c.Second(90000);
        Assert.assertEquals("25:00:00", mon.toString()); // second can exceed 24h
        mon = new c.Second(-30);
        Assert.assertEquals("-00:00:30", mon.toString());
        mon = new c.Second(-3661);
        Assert.assertEquals("-01:01:01", mon.toString());
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
        Assert.assertEquals(1,mon1.compareTo(mon3));
        // must not overflow: 0Wv (MAX) is greater than 0Nv (MIN)
        Assert.assertEquals(1,new c.Second(Integer.MAX_VALUE).compareTo(new c.Second(Integer.MIN_VALUE)));
        Assert.assertEquals(-1,new c.Second(Integer.MIN_VALUE).compareTo(new c.Second(Integer.MAX_VALUE)));
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
    public void testTemporalToStringLocaleIndependent()
    {
        // The canonical kdb+ textual form of temporal types must always use ASCII digits,
        // independent of the JVM default locale (e.g. Arabic-Indic digit locales).
        java.util.Locale previous = java.util.Locale.getDefault();
        try {
            java.util.Locale.setDefault(java.util.Locale.forLanguageTag("ar-EG"));
            Assert.assertEquals("2001-11", new c.Month(22).toString());
            Assert.assertEquals("00:22", new c.Minute(22).toString());
            Assert.assertEquals("00:00:22", new c.Second(22).toString());
            Assert.assertEquals("00:00:00.000000022", new c.Timespan(22).toString());
        } finally {
            java.util.Locale.setDefault(previous);
        }
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
        com.kx.c c=new com.kx.c();
        Assert.assertEquals(null,c.getMsgHandler());
    }

    @Test
    public void testClose(){
        com.kx.c c=new com.kx.c();
        try {
            c.close();
            c.close();
        } catch (Exception e){
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testCloseClosesRemainingResourcesWhenOneThrows() throws Exception {
        com.kx.c c=new com.kx.c();
        final boolean[] outClosed={false};
        // inStream.close() fails; the later outStream must still be closed and both fields nulled
        c.inStream=new java.io.DataInputStream(new java.io.InputStream(){
            public int read(){ return -1; }
            @Override public void close() throws java.io.IOException { throw new java.io.IOException("boom"); }
        });
        c.outStream=new java.io.OutputStream(){
            public void write(int b){/*not needed for test*/}
            @Override public void close(){ outClosed[0]=true; }
        };
        try {
            c.close();
            Assert.fail("Expected the close failure to propagate");
        } catch (java.io.IOException e) {
            assertTrue(e.getMessage().contains("boom"));
        }
        assertTrue("outStream must be closed even though inStream.close() threw", outClosed[0]);
        Assert.assertNull(c.inStream);
        Assert.assertNull(c.outStream);
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
        com.kx.c c=new com.kx.c();
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
        com.kx.c c=new com.kx.c();
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
        com.kx.c c=new com.kx.c();
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
        com.kx.c c=new com.kx.c();
        Assert.assertEquals(null,c.getMsgHandler());
        c.setMsgHandler(msgHandler);
        Assert.assertEquals(msgHandler,c.getMsgHandler());
    }

    private static int messageLength(byte[] bytes,int offset){
      if(bytes[offset]==1)  /*if little endian */
        return  (bytes[offset+4]&0xff)
             | ((bytes[offset+5]&0xff)<<8)
             | ((bytes[offset+6]&0xff)<<16)
             | ((bytes[offset+7]&0xff)<<24);
      return ((bytes[offset+4]&0xff)<<24)
             | ((bytes[offset+5]&0xff)<<16)
             | ((bytes[offset+6]&0xff)<<8)
             |  (bytes[offset+7]&0xff);
    }

    private static void assertFrameTypes(byte[] bytes, int... expectedTypes) {
        int offset = 0;
        for (int expectedType : expectedTypes) {
            Assert.assertTrue("missing IPC frame header", offset + 8 <= bytes.length);
            int length = messageLength(bytes, offset);
            Assert.assertTrue("invalid IPC frame length", length >= 8 && offset + length <= bytes.length);
            Assert.assertEquals(expectedType, bytes[offset + 1] & 0xff);
            offset += length;
        }
        Assert.assertEquals("unexpected trailing IPC data", bytes.length, offset);
    }

    private static byte[] frameAt(byte[] bytes, int wantedIndex) {
        int offset = 0;
        int index = 0;
        while (offset < bytes.length) {
            int length = messageLength(bytes, offset);
            if (index == wantedIndex) {
                return Arrays.copyOfRange(bytes, offset, offset + length);
            }
            offset += length;
            index++;
        }
        throw new AssertionError("frame " + wantedIndex + " not found");
    }

    private static byte[] concat(byte[]... arrays) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (byte[] array : arrays) {
            out.write(array, 0, array.length);
        }
        return out.toByteArray();
    }

    private static String repeat(char value, int count) {
        char[] chars = new char[count];
        Arrays.fill(chars, value);
        return new String(chars);
    }

    private static void assertEncodingFailure(
            String name,
            Class<? extends Throwable> expectedCause) throws Exception {
        try {
            c.setEncoding(name);
            Assert.fail("Expected UnsupportedEncodingException for " + name);
        } catch (UnsupportedEncodingException e) {
            Assert.assertEquals(name, e.getMessage());
            Assert.assertTrue(
                    "unexpected cause: " + e.getCause(),
                    expectedCause.isInstance(e.getCause()));
        }
    }

    private static Thread startHandshakeClient(
            final int port,
            final String credentials,
            final int expectedCapability,
            final AtomicReference<Throwable> failure) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (Socket socket = new Socket(InetAddress.getLoopbackAddress(), port)) {
                    socket.setSoTimeout(5000);
                    byte[] handshake = (credentials + "\3\0").getBytes(StandardCharsets.ISO_8859_1);
                    socket.getOutputStream().write(handshake);
                    socket.getOutputStream().flush();
                    if (expectedCapability >= 0) {
                        Assert.assertEquals(expectedCapability, socket.getInputStream().read());
                    }
                } catch (Throwable t) {
                    failure.set(t);
                }
            }
        }, "javakdb-test-client");
        thread.setDaemon(true);
        thread.start();
        return thread;
    }

    private static void assertThreadSucceeded(
            Thread thread,
            AtomicReference<Throwable> failure) throws InterruptedException {
        thread.join(6000);
        Assert.assertFalse("client thread did not finish", thread.isAlive());
        if (failure.get() != null) {
            throw new AssertionError(failure.get());
        }
    }

    @Test
    public void testSetEncodingRejectsIllegalAndUnsupportedCharsets() throws Exception {
        try {
            assertEncodingFailure("bad charset", IllegalCharsetNameException.class);
            assertEncodingFailure("X-KX-NOT-A-REAL-CHARSET", UnsupportedCharsetException.class);
        } finally {
            // c.encoding is static, so restore the library default for other tests.
            c.setEncoding("ISO-8859-1");
        }
    }

    @Test
    public void testCompressionFallbackAndLoopbackSkip() throws Exception {
        c codec = new c();

        // >2000 bytes, compression requested, non-loopback. Random bytes should make
        // compress() abandon the compressed buffer and return the original frame.
        byte[] incompressible = new byte[5000];
        new Random(123456789L).nextBytes(incompressible);
        byte[] fallback = codec.serialize(0, incompressible, true);
        Assert.assertEquals(0, fallback[2]);
        Assert.assertArrayEquals(incompressible, (byte[])codec.deserialize(fallback));

        // Highly compressible data must nevertheless remain uncompressed on loopback.
        c loopback = new c();
        loopback.isLoopback = true;
        byte[] repetitive = new byte[5000];
        byte[] skipped = loopback.serialize(0, repetitive, true);
        Assert.assertEquals(0, skipped[2]);
        Assert.assertArrayEquals(repetitive, (byte[])loopback.deserialize(skipped));
    }

    @Test
    public void testKOverloadsCanCollectResponseAsynchronously() throws Exception {
        c client = new c();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        client.outStream = out;
        client.setCollectResponseAsync(true);

        Assert.assertNull(client.k("1+1"));
        Assert.assertNull(client.k("f", 1));
        Assert.assertNull(client.k("f", 1, 2));
        Assert.assertNull(client.k("f", 1, 2, 3));
        Assert.assertNull(client.k("f", 1, 2, 3, 4));
        Assert.assertNull(client.k("f", 1, 2, 3, 4, 5));

        assertFrameTypes(out.toByteArray(), 1, 1, 1, 1, 1, 1);
    }

    @Test
    public void testKsOverloadsAndZipWriteCompleteAsyncFrames() throws Exception {
        c client = new c();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        client.outStream = out;

        client.ks("1+1");
        client.ks(Integer.valueOf(1));
        client.ks("f", 1);
        client.ks("f", 1, 2);
        client.ks("f", 1, 2, 3);
        client.ks("f", 1, 2, 3, 4);
        client.ks("f", 1, 2, 3, 4, 5);
        assertFrameTypes(out.toByteArray(), 0, 0, 0, 0, 0, 0, 0);

        out.reset();
        client.zip(true);
        byte[] input = new byte[5000];
        client.ks(input);

        byte[] compressed = out.toByteArray();
        Assert.assertEquals(1, compressed[2]);
        Assert.assertArrayEquals(input, (byte[])client.deserialize(compressed));
    }

    @Test
    public void testKReturnsResponseWithoutLiveQProcess() throws Exception {
        c codec = new c();
        byte[] response = codec.serialize(2, Integer.valueOf(42), false);

        c client = new c();
        client.inStream = new DataInputStream(new ByteArrayInputStream(response));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        client.outStream = out;

        Assert.assertEquals(Integer.valueOf(42), client.k("6*7"));
        assertFrameTypes(out.toByteArray(), 1);
    }

    @Test
    public void testDefaultHandlerRespondsToSyncWhileWaitingForResponse() throws Exception {
        c codec = new c();
        byte[] incomingSync = codec.serialize(1, "request".toCharArray(), false);
        byte[] finalResponse = codec.serialize(2, Integer.valueOf(42), false);

        c client = new c();
        client.inStream = new DataInputStream(
                new ByteArrayInputStream(concat(incomingSync, finalResponse)));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        client.outStream = out;
        client.setMsgHandler(new c.MsgHandler() { });

        Assert.assertEquals(Integer.valueOf(42), client.k("6*7"));

        // First frame is our sync request; second is the default handler's error reply.
        byte[] written = out.toByteArray();
        assertFrameTypes(written, 1, 2);
        try {
            codec.deserialize(frameAt(written, 1));
            Assert.fail("Expected default MsgHandler to send a kdb+ error object");
        } catch (c.KException e) {
            Assert.assertEquals("unable to process sync requests", e.getMessage());
        }
    }

    @Test
    public void testReceiveOnlyKTracksSyncAndKrWritesResponse() throws Exception {
        c codec = new c();
        byte[] incomingSync = codec.serialize(1, Integer.valueOf(7), false);

        c server = new c();
        server.inStream = new DataInputStream(new ByteArrayInputStream(incomingSync));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        server.outStream = out;

        Assert.assertEquals(Integer.valueOf(7), server.k());
        server.kr(Integer.valueOf(8));

        byte[] response = out.toByteArray();
        assertFrameTypes(response, 2);
        Assert.assertEquals(Integer.valueOf(8), codec.deserialize(response));

        try {
            server.kr(Integer.valueOf(9));
            Assert.fail("Expected kr() to reject a response with no pending sync request");
        } catch (IOException e) {
            Assert.assertEquals("Unexpected response msg", e.getMessage());
        }

        try {
            server.ke("boom");
            Assert.fail("Expected ke() to reject an error with no pending sync request");
        } catch (IOException e) {
            Assert.assertEquals("Unexpected error msg", e.getMessage());
        }
    }

    @Test
    public void testServerSocketAcceptsAuthenticationLongerThanOldFixedBuffer() throws Exception {
        final String credentials = repeat('u', 220) + ":" + repeat('p', 220);

        try (ServerSocket server = new ServerSocket(0, 1, InetAddress.getLoopbackAddress())) {
            AtomicReference<Throwable> failure = new AtomicReference<Throwable>();
            Thread peer = startHandshakeClient(server.getLocalPort(), credentials, 3, failure);
            c connection = null;
            try {
                connection = new c(server, new c.IAuthenticate() {
                    @Override
                    public boolean authenticate(String supplied) {
                        return credentials.equals(supplied);
                    }
                });
                Assert.assertEquals(3, connection.ipcVersion);
                Assert.assertTrue(connection.isLoopback);
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
            assertThreadSucceeded(peer, failure);
        }
    }

    @Test
    public void testServerSocketChannelAcceptsLongAuthenticationHandshake() throws Exception {
        final String credentials = repeat('u', 220) + ":" + repeat('p', 220);

        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            server.bind(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0));
            int port = ((InetSocketAddress)server.getLocalAddress()).getPort();

            AtomicReference<Throwable> failure = new AtomicReference<Throwable>();
            Thread peer = startHandshakeClient(port, credentials, 3, failure);
            c connection = null;
            try {
                // Exercise the one-argument overload as well as the channel implementation.
                connection = new c(server);
                Assert.assertEquals(3, connection.ipcVersion);
                Assert.assertTrue(connection.isLoopback);
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
            assertThreadSucceeded(peer, failure);
        }
    }

    @Test
    public void testServerRejectsFailedAuthentication() throws Exception {
        final String credentials = "user:wrong";

        try (ServerSocket server = new ServerSocket(0, 1, InetAddress.getLoopbackAddress())) {
            AtomicReference<Throwable> failure = new AtomicReference<Throwable>();
            Thread peer = startHandshakeClient(server.getLocalPort(), credentials, -1, failure);

            try {
                new c(server, new c.IAuthenticate() {
                    @Override
                    public boolean authenticate(String supplied) {
                        return false;
                    }
                });
                Assert.fail("Expected authentication failure");
            } catch (IOException e) {
                Assert.assertEquals("access", e.getMessage());
            }

            assertThreadSucceeded(peer, failure);
        }
    }

    @Test
    public void testClosePreservesFirstFailureAndSuppressesLaterFailures() throws Exception {
        c client = new c();
        client.s = new Socket() {
            @Override
            public void close() throws IOException {
                throw new IOException("socket");
            }
        };
        client.inStream = new DataInputStream(new InputStream() {
            @Override
            public int read() {
                return -1;
            }

            @Override
            public void close() throws IOException {
                throw new IOException("input");
            }
        });
        client.outStream = new OutputStream() {
            @Override
            public void write(int b) {/*not needed for test*/}

            @Override
            public void close() throws IOException {
                throw new IOException("output");
            }
        };

        try {
            client.close();
            Assert.fail("Expected close failure");
        } catch (IOException e) {
            Assert.assertEquals("socket", e.getMessage());
            Assert.assertEquals(2, e.getSuppressed().length);
            Assert.assertEquals("input", e.getSuppressed()[0].getMessage());
            Assert.assertEquals("output", e.getSuppressed()[1].getMessage());
        }

        Assert.assertNull(client.s);
        Assert.assertNull(client.inStream);
        Assert.assertNull(client.outStream);
    }

    @Test
    public void testSerializeUsesLittleEndian() throws Exception{
        c co=new c();
        Assert.assertArrayEquals(
            new byte[]{
                1,0,0,0,
                13,0,0,0,
                -6,
                1,0,0,0
            },
            co.serialize(0,Integer.valueOf(1),false)
        );
    }

    private interface ConnectionFactory {
        c connect(int port) throws Exception;
    }

    private static Thread startKdbServer(
            final ServerSocket server,
            final String expectedCredentials,
            final int response,
            final AtomicReference<Throwable> failure) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (Socket peer = server.accept()) {
                    peer.setSoTimeout(5000);
                    ByteArrayOutputStream handshake = new ByteArrayOutputStream();
                    int value;
                    while ((value = peer.getInputStream().read()) != -1) {
                        handshake.write(value);
                        if (value == 0) {
                            break;
                        }
                    }
                    Assert.assertArrayEquals(
                            (expectedCredentials + "\3\0").getBytes(StandardCharsets.ISO_8859_1),
                            handshake.toByteArray());
                    if (response >= 0) {
                        peer.getOutputStream().write(response);
                        peer.getOutputStream().flush();
                    }
                } catch (Throwable t) {
                    failure.set(t);
                }
            }
        }, "javakdb-constructor-test-server");
        thread.setDaemon(true);
        thread.start();
        return thread;
    }

    private static c connect(
            String credentials,
            int response,
            ConnectionFactory factory,
            AtomicReference<Throwable> failure,
            AtomicReference<Thread> serverThread) throws Exception {
        ServerSocket server = new ServerSocket(0, 1, InetAddress.getLoopbackAddress());
        Thread thread = startKdbServer(server, credentials, response, failure);
        serverThread.set(thread);
        try {
            return factory.connect(server.getLocalPort());
        } finally {
            server.close();
        }
    }

    private static void assertServerSucceeded(
            AtomicReference<Thread> serverThread,
            AtomicReference<Throwable> failure) throws InterruptedException {
        Thread thread = serverThread.get();
        thread.join(6000);
        Assert.assertFalse("server thread did not finish", thread.isAlive());
        if (failure.get() != null) {
            throw new AssertionError(failure.get());
        }
    }

    private static Thread startKdbClient(
            final int port,
            final String credentials,
            final int capability,
            final int expectedResponse,
            final AtomicReference<Throwable> failure) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (Socket peer = new Socket(InetAddress.getLoopbackAddress(), port)) {
                    peer.setSoTimeout(5000);
                    peer.getOutputStream().write(
                            (credentials + (char)capability + "\0")
                                    .getBytes(StandardCharsets.ISO_8859_1));
                    peer.getOutputStream().flush();
                    Assert.assertEquals(expectedResponse, peer.getInputStream().read());
                } catch (Throwable t) {
                    failure.set(t);
                }
            }
        }, "javakdb-constructor-test-client");
        thread.setDaemon(true);
        thread.start();
        return thread;
    }

    @Test
    public void noArgumentConstructorCreatesSerializationOnlyInstance() throws Exception {
        c connection = new c();

        Assert.assertEquals(3, connection.ipcVersion);
        Assert.assertFalse(connection.isLoopback);
        Assert.assertNotNull(connection.inStream);
        Assert.assertNotNull(connection.outStream);
        Assert.assertNull(connection.s);

        try {
            connection.inStream.read();
            Assert.fail("Expected serialization-only input to reject reads");
        } catch (UnsupportedOperationException expected) {
            Assert.assertEquals("nyi", expected.getMessage());
        }
        try {
            connection.outStream.write(1);
            Assert.fail("Expected serialization-only output to reject writes");
        } catch (UnsupportedOperationException expected) {
            Assert.assertEquals("nyi", expected.getMessage());
        }
    }

    @Test
    public void tcpConstructorSendsHandshakeAndCapsServerCapability() throws Exception {
        final String credentials = "test-user:test-password";
        final AtomicReference<Throwable> failure = new AtomicReference<Throwable>();
        final AtomicReference<Thread> serverThread = new AtomicReference<Thread>();
        c connection = connect(credentials, 9, new ConnectionFactory() {
            @Override
            public c connect(int port) throws Exception {
                return new c(InetAddress.getLoopbackAddress().getHostAddress(), port, credentials);
            }
        }, failure, serverThread);
        try {
            Assert.assertEquals(3, connection.ipcVersion);
            Assert.assertTrue(connection.isLoopback);
            Assert.assertEquals(0, connection.s.getSoTimeout());
        } finally {
            connection.close();
        }
        assertServerSucceeded(serverThread, failure);
    }

    @Test
    public void tcpBooleanAndTimeoutOverloadsDelegateTheirArguments() throws Exception {
        final String credentials = "user:password";
        final AtomicReference<Throwable> failure = new AtomicReference<Throwable>();
        final AtomicReference<Thread> serverThread = new AtomicReference<Thread>();
        c connection = connect(credentials, 2, new ConnectionFactory() {
            @Override
            public c connect(int port) throws Exception {
                return new c(InetAddress.getLoopbackAddress().getHostAddress(), port,
                        credentials, false, 1234);
            }
        }, failure, serverThread);
        try {
            Assert.assertEquals(2, connection.ipcVersion);
            Assert.assertEquals(1234, connection.s.getSoTimeout());
        } finally {
            connection.close();
        }
        assertServerSucceeded(serverThread, failure);

        final AtomicReference<Throwable> overloadFailure = new AtomicReference<Throwable>();
        final AtomicReference<Thread> overloadThread = new AtomicReference<Thread>();
        c overload = connect(credentials, 3, new ConnectionFactory() {
            @Override
            public c connect(int port) throws Exception {
                return new c(InetAddress.getLoopbackAddress().getHostAddress(), port,
                        credentials, false);
            }
        }, overloadFailure, overloadThread);
        overload.close();
        assertServerSucceeded(overloadThread, overloadFailure);
    }

    @Test
    public void hostAndPortConstructorUsesUserNameProperty() throws Exception {
        final String previous = System.getProperty("user.name");
        final String credentials = "property-user:property-password";
        final AtomicReference<Throwable> failure = new AtomicReference<Throwable>();
        final AtomicReference<Thread> serverThread = new AtomicReference<Thread>();
        System.setProperty("user.name", credentials);
        c connection = null;
        try {
            connection = connect(credentials, 3, new ConnectionFactory() {
                @Override
                public c connect(int port) throws Exception {
                    return new c(InetAddress.getLoopbackAddress().getHostAddress(), port);
                }
            }, failure, serverThread);
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (previous == null) {
                System.clearProperty("user.name");
            } else {
                System.setProperty("user.name", previous);
            }
        }
        assertServerSucceeded(serverThread, failure);
    }

    @Test
    public void tcpConstructorClosesConnectionWhenHandshakeIsRejected() throws Exception {
        final String credentials = "rejected:user";
        final AtomicReference<Throwable> failure = new AtomicReference<Throwable>();
        final AtomicReference<Thread> serverThread = new AtomicReference<Thread>();
        try {
            connect(credentials, -1, new ConnectionFactory() {
                @Override
                public c connect(int port) throws Exception {
                    return new c(InetAddress.getLoopbackAddress().getHostAddress(), port,
                            credentials);
                }
            }, failure, serverThread);
            Assert.fail("Expected access failure");
        } catch (c.KException expected) {
            Assert.assertEquals("access", expected.getMessage());
        }
        assertServerSucceeded(serverThread, failure);
    }

    @Test
    public void serverSocketConvenienceConstructorAcceptsAndCapsCapability() throws Exception {
        final AtomicReference<Throwable> failure = new AtomicReference<Throwable>();
        try (ServerSocket server = new ServerSocket(
                0, 1, InetAddress.getLoopbackAddress())) {
            Thread client = startKdbClient(
                    server.getLocalPort(), "server:user", 9, 3, failure);
            c connection = null;
            try {
                connection = new c(server);
                Assert.assertEquals(9, connection.ipcVersion);
                Assert.assertTrue(connection.isLoopback);
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
            client.join(6000);
            Assert.assertFalse("client thread did not finish", client.isAlive());
            if (failure.get() != null) {
                throw new AssertionError(failure.get());
            }
        }
    }
}
