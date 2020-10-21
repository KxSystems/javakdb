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
        assertTrue( c.NULL[0]==null );
        assertTrue( c.NULL[1].equals(false) );
        assertTrue( c.NULL[2].equals(new UUID(0,0)) );
        assertTrue( c.NULL[3]==null );
        assertTrue( c.NULL[4].equals(new Byte((byte)0) ));
        assertTrue( c.NULL[5].equals(Short.MIN_VALUE) );
        assertTrue( c.NULL[6].equals(Integer.MIN_VALUE) );
        assertTrue( c.NULL[7].equals(Long.MIN_VALUE) );
        assertTrue( c.NULL[8].equals(new Float(Double.NaN)) );
        assertTrue( c.NULL[9].equals(Double.NaN) );
        assertTrue( c.NULL[10].equals(' ') );
        assertTrue( c.NULL[11].equals("") );
        assertTrue( c.NULL[12].equals(new Timestamp(Long.MIN_VALUE)) );
        assertTrue( c.NULL[13].equals(new c.Month(Integer.MIN_VALUE)) );
        assertTrue( c.NULL[14].equals(new Date(Long.MIN_VALUE)) );
        assertTrue( c.NULL[15].equals(new java.util.Date(Long.MIN_VALUE)) );
        assertTrue( c.NULL[16].equals(new c.Timespan(Long.MIN_VALUE)) );
        assertTrue( c.NULL[17].equals(new c.Minute(Integer.MIN_VALUE)) );
        assertTrue( c.NULL[18].equals(new c.Second(Integer.MIN_VALUE)) );
        assertTrue( c.NULL[19].equals(new Time(Long.MIN_VALUE)) );
    }

    @Test
    public void testGetNullValues()
    {
        assertTrue( c.NULL(' ')==null);
        assertTrue( c.NULL('b').equals(false));
        assertTrue( c.NULL('g').equals(new UUID(0,0)));
        assertTrue( c.NULL('x').equals(new Byte((byte)0) ));
        assertTrue( c.NULL('h').equals(Short.MIN_VALUE));
        assertTrue( c.NULL('i').equals(Integer.MIN_VALUE));
        assertTrue( c.NULL('j').equals(Long.MIN_VALUE));
        assertTrue( c.NULL('e').equals(new Float(Double.NaN)));
        assertTrue( c.NULL('f').equals(Double.NaN));
        assertTrue( c.NULL('c').equals(' '));
        assertTrue( c.NULL('s').equals(""));
        assertTrue( c.NULL('p').equals(new Timestamp(Long.MIN_VALUE)));
        assertTrue( c.NULL('m').equals(new c.Month(Integer.MIN_VALUE)) );
        assertTrue( c.NULL('d').equals(new Date(Long.MIN_VALUE)));
        assertTrue( c.NULL('z').equals(new java.util.Date(Long.MIN_VALUE)));
        assertTrue( c.NULL('n').equals(new c.Timespan(Long.MIN_VALUE)));
        assertTrue( c.NULL('u').equals(new c.Minute(Integer.MIN_VALUE)));
        assertTrue( c.NULL('v').equals(new c.Second(Integer.MIN_VALUE)));
        assertTrue( c.NULL('t').equals(new Time(Long.MIN_VALUE)));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testIncorrectNullType()
    {
        assertTrue( c.NULL('a').equals(Integer.MIN_VALUE));
    }

    @Test
    public void testValueIsNull()
    {
        assertTrue( c.qn("") );
        assertTrue( c.qn(" ")==false );
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
        assertTrue( c.qn(" ")==false);
        assertTrue( c.qn(new StringBuffer())==false);
    }

    @Test
    public void testGetAtomType()
    {
        assertTrue( c.t(Boolean.FALSE)==-1);
        assertTrue( c.t(new UUID(0,0))==-2);
        assertTrue( c.t(Byte.valueOf("1"))==-4);
        assertTrue( c.t(Short.valueOf("1"))==-5);
        assertTrue( c.t(Integer.valueOf(1111))==-6);
        assertTrue( c.t(Long.valueOf(1111))==-7);
        assertTrue( c.t(Float.valueOf(1.2f))==-8);
        assertTrue( c.t(Double.valueOf(1.2))==-9);
        assertTrue( c.t(Character.valueOf(' '))==-10);
        assertTrue( c.t("")==-11);
        assertTrue( c.t(new Date(Long.MIN_VALUE))==-14);
        assertTrue( c.t(new Time(Long.MIN_VALUE))==-19);
        assertTrue( c.t(new Timestamp(Long.MIN_VALUE))==-12);
        assertTrue( c.t(new java.util.Date(Long.MIN_VALUE))==-15);
        assertTrue( c.t(new c.Timespan(Long.MIN_VALUE))==-16);
        assertTrue( c.t(new c.Month(Integer.MIN_VALUE))==-13);
        assertTrue( c.t(new c.Minute(Integer.MIN_VALUE))==-17);
        assertTrue( c.t(new c.Second(Integer.MIN_VALUE))==-18);
    }

    @Test
    public void testGetType()
    {
        assertTrue( c.t(new boolean[2])==1);
        assertTrue( c.t(new UUID[2])==2);
        assertTrue( c.t(new byte[2])==4);
        assertTrue( c.t(new short[2])==5);
        assertTrue( c.t(new int[2])==6);
        assertTrue( c.t(new long[2])==7);
        assertTrue( c.t(new float[2])==8);
        assertTrue( c.t(new double[2])==9);
        assertTrue( c.t(new char[2])==10);
        assertTrue( c.t(new String[2])==11);
        assertTrue( c.t(new Date[2])==14);
        assertTrue( c.t(new Time[2])==19);
        assertTrue( c.t(new Timestamp[2])==12);
        assertTrue( c.t(new java.util.Date[2])==15);
        assertTrue( c.t(new c.Timespan[2])==16);
        assertTrue( c.t(new c.Month[2])==13);
        assertTrue( c.t(new c.Minute[2])==17);
        assertTrue( c.t(new c.Second[2])==18);
        c.Dict dict = new c.Dict(new String[] {"Key"}, new String[][] {{"Value1","Value2","Value3"}});
        assertTrue( c.t(new c.Flip(dict))==98);
        assertTrue( c.t(dict)==99);
    }

    @Test
    public void testGetUnknownType()
    {
        assertTrue( c.t(new StringBuffer())==0);
    }

    @Test
    public void testDictConstructor()
    {
        String[] x = new String[] {"Key"};
        String[][] y = new String[][] {{"Value1","Value2","Value3"}};
        c.Dict dict = new c.Dict(x, y);
        assertTrue(dict.x.equals(x));
        assertTrue(dict.y.equals(y));
    }

    @Test
    public void testFlipConstructor()
    {
        String[] x = new String[] {"Key"};
        String[][] y = new String[][] {{"Value1","Value2","Value3"}};
        c.Dict dict = new c.Dict(x, y);
        c.Flip flip = new c.Flip(dict);
        assertTrue(flip.x.equals(x));
        assertTrue(flip.y.equals(y));
    }

    @Test
    public void testFlipColumnPosition()
    {
        String[] x = new String[] {"Key"};
        String[][] y = new String[][] {{"Value1","Value2","Value3"}};
        c.Dict dict = new c.Dict(x, y);
        c.Flip flip = new c.Flip(dict);
        assertTrue(flip.at("Key").equals(y[0]));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testFlipUnknownColumn()
    {
        String[] x = new String[] {"Key"};
        String[][] y = new String[][] {{"Value1","Value2","Value3"}};
        c.Dict dict = new c.Dict(x, y);
        c.Flip flip = new c.Flip(dict);
        flip.at("RUBBISH");
    }

    @Test
    public void testSerializeDeserialize()
    {
        kx.c c=new kx.c();
        int[]input=new int[50000];
        for(int i=0;i<input.length;i++)
            input[i]=i%10;
        try{
            assertTrue(Arrays.equals(input,(int[])c.deserialize(c.serialize(1,input,false))));
            assertTrue(Arrays.equals(input,(int[])c.deserialize(c.serialize(1,input,true))));
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
}
