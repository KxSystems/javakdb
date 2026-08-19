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
  private static final VarHandle INT_BE=MethodHandles.byteArrayViewVarHandle(int[].class,ByteOrder.BIG_ENDIAN);
  private static final VarHandle LONG_BE=MethodHandles.byteArrayViewVarHandle(long[].class,ByteOrder.BIG_ENDIAN);

  private ByteArrayAccess(){}

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
