/*
 * Copyright (c) 1998-2017 Kx Systems Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.kx;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;

/** Java 9+ VarHandle implementation used from the multi-release jar. */
final class ByteArrayAccess{
  private static final VarHandle SHORT_BE=MethodHandles.byteArrayViewVarHandle(short[].class,ByteOrder.BIG_ENDIAN);
  private static final VarHandle SHORT_LE=MethodHandles.byteArrayViewVarHandle(short[].class,ByteOrder.LITTLE_ENDIAN);
  private static final VarHandle INT_BE=MethodHandles.byteArrayViewVarHandle(int[].class,ByteOrder.BIG_ENDIAN);
  private static final VarHandle INT_LE=MethodHandles.byteArrayViewVarHandle(int[].class,ByteOrder.LITTLE_ENDIAN);
  private static final VarHandle LONG_BE=MethodHandles.byteArrayViewVarHandle(long[].class,ByteOrder.BIG_ENDIAN);
  private static final VarHandle LONG_LE=MethodHandles.byteArrayViewVarHandle(long[].class,ByteOrder.LITTLE_ENDIAN);
  private static final VarHandle FLOAT_BE=MethodHandles.byteArrayViewVarHandle(float[].class,ByteOrder.BIG_ENDIAN);
  private static final VarHandle FLOAT_LE=MethodHandles.byteArrayViewVarHandle(float[].class,ByteOrder.LITTLE_ENDIAN);
  private static final VarHandle DOUBLE_BE=MethodHandles.byteArrayViewVarHandle(double[].class,ByteOrder.BIG_ENDIAN);
  private static final VarHandle DOUBLE_LE=MethodHandles.byteArrayViewVarHandle(double[].class,ByteOrder.LITTLE_ENDIAN);

  private ByteArrayAccess(){}

  static short getShortBE(byte[] b,int p){
    return (short)SHORT_BE.get(b,p);
  }

  static short getShortLE(byte[] b,int p){
    return (short)SHORT_LE.get(b,p);
  }

  static int getIntBE(byte[] b,int p){
    return (int)INT_BE.get(b,p);
  }

  static int getIntLE(byte[] b,int p){
    return (int)INT_LE.get(b,p);
  }

  static long getLongBE(byte[] b,int p){
    return (long)LONG_BE.get(b,p);
  }

  static long getLongLE(byte[] b,int p){
    return (long)LONG_LE.get(b,p);
  }

  static void getShorts(byte[] b,int p,short[] dst,boolean littleEndian){
    if(littleEndian){
      for(int i=0;i<dst.length;i++,p+=2)
        dst[i]=(short)SHORT_LE.get(b,p);
    }else{
      for(int i=0;i<dst.length;i++,p+=2)
        dst[i]=(short)SHORT_BE.get(b,p);
    }
  }

  static void getInts(byte[] b,int p,int[] dst,boolean littleEndian){
    if(littleEndian){
      for(int i=0;i<dst.length;i++,p+=4)
        dst[i]=(int)INT_LE.get(b,p);
    }else{
      for(int i=0;i<dst.length;i++,p+=4)
        dst[i]=(int)INT_BE.get(b,p);
    }
  }

  static void getLongs(byte[] b,int p,long[] dst,boolean littleEndian){
    if(littleEndian){
      for(int i=0;i<dst.length;i++,p+=8)
        dst[i]=(long)LONG_LE.get(b,p);
    }else{
      for(int i=0;i<dst.length;i++,p+=8)
        dst[i]=(long)LONG_BE.get(b,p);
    }
  }

  static void getFloats(byte[] b,int p,float[] dst,boolean littleEndian){
    if(littleEndian){
      for(int i=0;i<dst.length;i++,p+=4)
        dst[i]=(float)FLOAT_LE.get(b,p);
    }else{
      for(int i=0;i<dst.length;i++,p+=4)
        dst[i]=(float)FLOAT_BE.get(b,p);
    }
  }

  static void getDoubles(byte[] b,int p,double[] dst,boolean littleEndian){
    if(littleEndian){
      for(int i=0;i<dst.length;i++,p+=8)
        dst[i]=(double)DOUBLE_LE.get(b,p);
    }else{
      for(int i=0;i<dst.length;i++,p+=8)
        dst[i]=(double)DOUBLE_BE.get(b,p);
    }
  }

  static void putShortBE(byte[] b,int p,short v){
    SHORT_BE.set(b,p,v);
  }

  static void putIntBE(byte[] b,int p,int v){
    INT_BE.set(b,p,v);
  }

  static void putLongBE(byte[] b,int p,long v){
    LONG_BE.set(b,p,v);
  }

  static void putShortsBE(byte[] b,int p,short[] a){
    for(short v:a){
      SHORT_BE.set(b,p,v);
      p+=2;
    }
  }

  static void putIntsBE(byte[] b,int p,int[] a){
    for(int v:a){
      INT_BE.set(b,p,v);
      p+=4;
    }
  }

  static void putLongsBE(byte[] b,int p,long[] a){
    for(long v:a){
      LONG_BE.set(b,p,v);
      p+=8;
    }
  }

  static void putFloatsBE(byte[] b,int p,float[] a){
    for(float v:a){
      INT_BE.set(b,p,Float.floatToIntBits(v));
      p+=4;
    }
  }

  static void putDoublesBE(byte[] b,int p,double[] a){
    for(double v:a){
      LONG_BE.set(b,p,Double.doubleToLongBits(v));
      p+=8;
    }
  }
}
