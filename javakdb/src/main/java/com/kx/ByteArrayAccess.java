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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Java 8 implementation of the serialization byte-array write primitives.
 *
 * <p>On Java 9 and newer this class is replaced by the implementation in
 * {@code META-INF/versions/9}, which uses VarHandle.</p>
 */
final class ByteArrayAccess{
  private ByteArrayAccess(){}

  static void putShortBE(byte[] b,int p,short v){
    b[p]=(byte)(v>>8);
    b[p+1]=(byte)v;
  }

  static void putIntBE(byte[] b,int p,int v){
    putShortBE(b,p,(short)(v>>16));
    putShortBE(b,p+2,(short)v);
  }

  static void putLongBE(byte[] b,int p,long v){
    putIntBE(b,p,(int)(v>>32));
    putIntBE(b,p+4,(int)v);
  }

  static void putShortsBE(byte[] b,int p,short[] a){
    ByteBuffer.wrap(b,p,a.length*2).order(ByteOrder.BIG_ENDIAN).asShortBuffer().put(a);
  }

  static void putIntsBE(byte[] b,int p,int[] a){
    ByteBuffer.wrap(b,p,a.length*4).order(ByteOrder.BIG_ENDIAN).asIntBuffer().put(a);
  }

  static void putLongsBE(byte[] b,int p,long[] a){
    ByteBuffer.wrap(b,p,a.length*8).order(ByteOrder.BIG_ENDIAN).asLongBuffer().put(a);
  }

  static void putFloatsBE(byte[] b,int p,float[] a){
    for(float v:a){
      putIntBE(b,p,Float.floatToIntBits(v));
      p+=4;
   }
  }

  static void putDoublesBE(byte[] b,int p,double[] a){
    for(double v:a){
      putLongBE(b,p,Double.doubleToLongBits(v));
      p+=8;
    }
  }
}

