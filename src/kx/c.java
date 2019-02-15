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
package kx;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Connector class for interfacing with a kdb+ process. This class is essentially a serializer/deserializer of java types
 * to/from the kdb+ ipc wire format, enabling remote method invocation in kdb+ via tcp/ip.
 * <p>
 * To begin with, a connection may be established to a listening kdb+ process via the constructor
 * 
 * <code>c connection=new c("localhost",5000);</code>
 * </p>
 * <p>
 *  There are then 3 methods available for interacting with the connection:
 *   <ol>
 *     <li>Sending a sync message using k() <br>
 *       <code>Object result=connection.k("functionName",args);</code>
 * 
 *     <li>Sending an async message using ks()<br>
 *       <code>connection.ks("functionName",args); </code>
 *     
 *     <li>Awaiting an incoming async message using k()<br>
 *       <code>Object object=connection.k();</code>. <br> 
 *       When the connection is no longer required, it may be closed via connection.close();
 *   </ol>
 */
public class c{
  /**
   * Encoding specifies the character encoding to use when [de]-serializing strings.
   */
  private static String encoding="ISO-8859-1";

  /** Stream for printing kdb+ objects. Defaults to System.out */
  private static PrintStream out=System.out;
  /**
   *  {@code sync}  tracks how many response messages the remote is expecting
   */
  private int sync=0;

  /**
   * Sets character encoding for serialising/deserialising strings.
   * 
   * @param encoding The name of a supported
   *                 <a href="../lang/package-summary.html#charenc">
   *                 character encoding</a>
   * @throws UnsupportedEncodingException
   *                If the named encoding is not supported
   */
  public static void setEncoding(String encoding) throws UnsupportedEncodingException{
    c.encoding=encoding;
    out=new PrintStream(System.out,true,encoding);
  }
  /**
   * {@code s} is the socket used to communicate with the remote kdb+ process.
   */
  public Socket s;
  /**
   * {@code i} is the {@code DataInputStream} of the socket used to read data from the remote kdb+ process.
   */
  DataInputStream i;
  /**
   * {@code o} is the outputStream of the socket used to write data to the remote kdb+ process.
   */
  OutputStream o;
  /**
   * {@code b} is the buffer used to store the incoming message bytes from the remote prior to de-serialization
   */
  byte[] b;
  /**
   * {@code j} is the current position of the de-serializer within the read buffer b
   */
  int j;
  /**
   * {@code B} is the buffer used to store the outgoing message bytes when serializing an object
   */
  byte[] B;
  /**
   * {@code J} is the current position the serializer within the write buffer B
   */
  int J;
  /**
  * {@code vt} indicates the ipc version to encode with
  */
  int vt;
  /**
   * Marks whether the message being deserialized was encoded little or big endian.
   */
  boolean a;
  /**
   * Indicates whether the current connection is to a local interface. Tested when considering whether to compress an outgoing message.
   */
  boolean l;
  /**
   * Indicates whether messages should be candidates for compressing before sending.
   */
  boolean zip;
  /**
   * Sets whether or not to consider compression on outgoing messages.
   * 
   * @param b true if to use a compression. Default is false.
   * 
   * @see <a href="https://code.kx.com/q/ref/ipc/#compression">IPC compression</a>
   */
  public void zip(boolean b){
    zip=b;
  }
  /**
   * Prepare socket for kdb+ ipc comms
   * @param x socket to setup
   * @throws IOException an I/O error occurs.
   */
  void io(Socket x) throws IOException{
    s=x;
    s.setTcpNoDelay(true);
    InetAddress addr=s.getInetAddress();
    l=addr.isAnyLocalAddress()||addr.isLoopbackAddress();
    i=new DataInputStream(s.getInputStream());
    o=s.getOutputStream();
    s.setKeepAlive(true);
  }

  /** 
   * Closes the current connection to the remote process. 
   * 
   * @throws IOException if an I/O error occurs when closing this socket.
   */
  public void close() throws IOException{
    if(null!=s){
      s.close();
      s=null;
    }
    if(null!=i){
      i.close();
      i=null;
    }
    if(null!=o){
      o.close();
      o=null;
    }
  }

  /** {@code IAuthenticate} describes interface to authenticate incoming connection based on authentication string */
  public interface IAuthenticate{
    /**
     * Checks authentication string provided to allow/reject connection. 
     * @see <a href="https://code.kx.com/q/ref/dotz/#zpw-validate-user">.z.pw</a>
     * 
     * @param s String containing username:password for authentication
     * 
     * @return true if credentials accepted. 
     */
    public boolean authenticate(String s);
  }

  /**
   * Accepts and authenticates incoming connections using kdb+ protocol.
   * 
   * @param s {@link ServerSocket} to accept connections on using kdb+ IPC protocol.
   * @param a {@link IAuthenticate} instance to authenticate incoming connections. 
   *          Accepts all incoming connections if {@code null}.
   * 
   * @throws IOException if access is denied or an I/O error occurs.
   * 
   */
  public c(ServerSocket s,IAuthenticate a) throws IOException{
    io(s.accept());
    int n=i.read(b=new byte[99]);
    if(a!=null&&!a.authenticate(new String(b,0,n>1?n-2:0))){
      close();
      throw new IOException("access");
    }
    vt=n>1?b[n-2]:0;
    b[0]=(byte)(vt<'\3'?vt:'\3');
    o.write(b,0,1);
  }

  /** 
   * c#c(ServerSocket, IAuthenticate) without authentication. 
   * 
   * @param s {@link ServerSocket} to accept connections on using kdb+ IPC protocol.
   * 
   * @throws IOException an I/O error occurs.
   */
  public c(ServerSocket s) throws IOException{
    this(s,null);
  }
  /**
   * Initializes a new {@link c} instance.
   * 
   * @param host Host of remote q process
   * @param port Port of remote q process
   * @param usernamepassword Username and password as "username:password" for remote authorization
   * 
   * @throws KException if access denied
   * @throws IOException if an I/O error occurs.
   */
  public c(String host,int port,String usernamepassword) throws KException,IOException{
    this(host,port,usernamepassword,false);
  }

  /**
   * Initializes a new {@link c} instance.
   * 
   * @param host Host of remote q process
   * @param port Port of remote q process
   * @param usernamepassword Username and password as "username:password" for remote authorization
   * @param useTLS whether to use TLS to encrypt the connection
   * 
   * @throws KException if access denied
   * @throws IOException if an I/O error occurs.
   */
  public c(String host,int port,String usernamepassword,boolean useTLS) throws KException,IOException{
    B=new byte[2+ns(usernamepassword)];
    s=new Socket(host,port);
    if(useTLS){
      try{
        s=((SSLSocketFactory)SSLSocketFactory.getDefault()).createSocket(s,host,port,true);
        ((SSLSocket)s).startHandshake();
      }
      catch(Exception e){
        s.close();
        throw e;
      }
    }
    io(s);
    J=0;
    w(usernamepassword+"\3");
    o.write(B);
    if(1!=i.read(B,0,1)){
      close();
      throw new KException("access");
    }
    vt=Math.min(B[0],3);
  }
  /**
   * Initializes a new {@link c} instance.
   * 
   * @param host Host of remote q process
   * @param port Port of remote q process
   * 
   * @throws KException if access denied
   * @throws IOException if an I/O error occurs.
   */
  public c(String host,int port) throws KException,IOException{
    this(host,port,System.getProperty("user.name"));
  }
  /** Initializes a new {@link c} instance for the purposes of serialization only.  */
  public c(){
    vt='\3';
    l=false;
    i=new DataInputStream(new InputStream(){
      @Override 
      public int read()throws IOException{
        throw new UnsupportedOperationException("nyi");
      }});
    o=new OutputStream(){
      @Override 
      public void write(int b)throws IOException{
        throw new UnsupportedOperationException("nyi");
      }};
  }

  /** {@code Month} represents kdb+ month type. */
  public static class Month implements Comparable<Month>{
    /** Number of months since Jan 2000 */
    public int i;
    public Month(int x){
      i=x;
    }
    @Override
    public String toString(){
      int m=i+24000, y=m/12;
      return i==ni?"":i2(y/100)+i2(y%100)+"-"+i2(1+m%12);
    }
    @Override
    public boolean equals(final Object o){
      return (o instanceof Month)?((Month)o).i==i:false;
    }
    @Override
    public int hashCode(){
      return i;
    }
    @Override
    public int compareTo(Month m){
      return i-m.i;
    }
  }

  /** {@code Minute} represents kdb+ minute type. */
  public static class Minute implements Comparable<Minute>{
    /** Number of minutes passed. */
    public int i;
    public Minute(int x){
      i=x;
    }
    @Override
    public String toString(){
      return i==ni?"":i2(i/60)+":"+i2(i%60);
    }
    @Override
    public boolean equals(final Object o){
      return (o instanceof Minute)?((Minute)o).i==i:false;
    }
    @Override
    public int hashCode(){
      return i;
    }
    @Override
    public int compareTo(Minute m){
      return i-m.i;
    }
  }

  /** {@code Second} represents kdb+ second type. */
  public static class Second implements Comparable<Second>{
    /** Number of seconds passed. */
    public int i;
    public Second(int x){
      i=x;
    }
    @Override
    public String toString(){
      return i==ni?"":new Minute(i/60).toString()+':'+i2(i%60);
    }
    @Override
    public boolean equals(final Object o){
      return (o instanceof Second)?((Second)o).i==i:false;
    }
    @Override
    public int hashCode(){
      return i;
    }
    @Override
    public int compareTo(Second s){
      return i-s.i;
    }
  }

  /** {@code Timespan} represents kdb+ timestamp type. */
  public static class Timespan implements Comparable<Timespan>{
    /** Number of nanoseconds passed. */
    public long j;
    public Timespan(long x){
      j=x;
    }
    /** Constructs {@code Timespan} using time since midnight and default timezone. */
    public Timespan(){
      this(TimeZone.getDefault());
    }
    /** 
     * Constructs {@code Timespan} using time since midnight and default timezone. 
     * 
     * @param tz {@code TimeZone} to use for deriving midnight.
     */
    public Timespan(TimeZone tz){
      Calendar c=Calendar.getInstance(tz);
      long now=c.getTimeInMillis();
      c.set(Calendar.HOUR_OF_DAY,0);
      c.set(Calendar.MINUTE,0);
      c.set(Calendar.SECOND,0);
      c.set(Calendar.MILLISECOND,0);
      j=(now-c.getTimeInMillis())*1000000L;
    }
    @Override
    public String toString(){
      if(j==nj)
        return "";
      String s=j<0?"-":"";
      long jj=j<0?-j:j;
      int d=((int)(jj/86400000000000L));
      if(d!=0)
        s+=d+"D";
      return s+i2((int)((jj%86400000000000L)/3600000000000L))+":"+i2((int)((jj%3600000000000L)/60000000000L))+":"+i2((int)((jj%60000000000L)/1000000000L))+"."+i9((int)(jj%1000000000L));
    }
    @Override
    public int compareTo(Timespan t){
      return j>t.j?1:j<t.j?-1:0;
    }
    @Override
    public boolean equals(final Object o){
      return (o instanceof Timespan)?((Timespan)o).j==j:false;
    }
    @Override
    public int hashCode(){
      return (int)(j^(j>>>32));
    }
  }
  /**
   * {@code Dict} represents the kdb+ dictionary type.
   */
  public static class Dict{
    /** Dict keys */
    public Object x;
    /** Dict values */
    public Object y;
    public Dict(Object X,Object Y){
      x=X;
      y=Y;
    }
  }
  /**
   * {@code Flip} represents a kdb+ table.
   */
  public static class Flip{
    /** Array of column names. */
    public String[] x;
    /** Array of arrays of the column values. */
    public Object[] y;
    public Flip(Dict X){
      x=(String[])X.x;
      y=(Object[])X.y;
    }
    public Object at(String s){
      return y[find(x,s)];
    }
  }
  /**
   * {@code KException} is used to indicate there was an error generated by the remote process during the processing of a sync message or if the connection failed due to access credentials. 
   * Network errors are reported as IOException.
   */
  public static class KException extends Exception{
    private static final long serialVersionUID = 1554689726734674152L; // autogenerated by serialver kx.c.KException
    KException(String s){
      super(s);
    }
  }
  private void compress(){
    byte i=0;
    boolean g;
    int j=J, f=0, h0=0, h=0;
    byte[] y=B;
    B=new byte[y.length/2];
    int c=12, d=c, e=B.length, p=0, q, r, s0=0, s=8, t=J, a[]=new int[256];
    System.arraycopy(y,0,B,0,4);
    B[2]=1;
    J=8;
    w(j);
    for(;s<t;i*=2){
      if(0==i){
        if(d>e-17){
          J=j;
          B=y;
          return;
        }
        i=1;
        B[c]=(byte)f;
        c=d++;
        f=0;
      }
      g=(s>t-3)||(0==(p=a[h=0xFF&(y[s]^y[s+1])]))||(0!=(y[s]^y[p]));
      if(0<s0){
        a[h0]=s0;
        s0=0;
      }
      if(g){
        h0=h;
        s0=s;
        B[d++]=y[s++];
      }else{
        a[h]=s;
        f|=i;
        p+=2;
        r=s+=2;
        q=Math.min(s+255,t);
        for(;y[p]==y[s]&&++s<q;)
          ++p;
        B[d++]=(byte)h;
        B[d++]=(byte)(s-r);
      }
    }
    B[c]=(byte)f;
    J=4;
    w(d);
    J=d;
    y=null;
    B=Arrays.copyOf(B,J);
  }
  private void uncompress(){
    int n=0, r=0, f=0, s=8, p=s;
    short i=0;
    byte[] dst=new byte[ri()];
    int d=j;
    int[] aa=new int[256];
    while(s<dst.length){
      if(i==0){
        f=0xff&(int)b[d++];
        i=1;
      }
      if((f&i)!=0){
        r=aa[0xff&(int)b[d++]];
        dst[s++]=dst[r++];
        dst[s++]=dst[r++];
        n=0xff&(int)b[d++];
        for(int m=0;m<n;m++)
          dst[s+m]=dst[r+m];
      }else
        dst[s++]=b[d++];
      while(p<s-1)
        aa[(0xff&(int)dst[p])^(0xff&(int)dst[p+1])]=p++;
      if((f&i)!=0)
        p=s+=n;
      i*=2;
      if(i==256)
        i=0;
    }
    b=dst;
    j=8;
  }
  void w(byte x){
    B[J++]=x;
  }
  /** null integer, i.e. 0Ni */
  static int ni=Integer.MIN_VALUE; 
  /** null long, i.e. 0N */
  static long nj=Long.MIN_VALUE;
  /** null float, i.e. 0Nf or 0n */
  static double nf=Double.NaN;
  boolean rb(){
    return 1==b[j++];
  }
  void w(boolean x){
    w((byte)(x?1:0));
  }
  char rc(){
    return (char)(b[j++]&0xff);
  }
  void w(char c){
    w((byte)c);
  }
  short rh(){
    int x=b[j++], y=b[j++];
    return (short)(a?x&0xff|y<<8:x<<8|y&0xff);
  }
  void w(short h){
    w((byte)(h>>8));
    w((byte)h);
  }
  int ri(){
    int x=rh(), y=rh();
    return a?x&0xffff|y<<16:x<<16|y&0xffff;
  }
  void w(int i){
    w((short)(i>>16));
    w((short)i);
  }
  UUID rg(){
    boolean oa=a;
    a=false;
    UUID g=new UUID(rj(),rj());
    a=oa;
    return g;
  }
  void w(UUID uuid){
    if(vt<3)
      throw new RuntimeException("Guid not valid pre kdb+3.0");
    w(uuid.getMostSignificantBits());
    w(uuid.getLeastSignificantBits());
  }
  long rj(){
    int x=ri(), y=ri();
    return a?x&0xffffffffL|(long)y<<32:(long)x<<32|y&0xffffffffL;
  }
  void w(long j){
    w((int)(j>>32));
    w((int)j);
  }
  float re(){
    return Float.intBitsToFloat(ri());
  }
  void w(float e){
    w(Float.floatToIntBits(e));
  }
  double rf(){
    return Double.longBitsToDouble(rj());
  }
  void w(double f){
    w(Double.doubleToLongBits(f));
  }
  Month rm(){
    return new Month(ri());
  }
  void w(Month m){
    w(m.i);
  }
  Minute ru(){
    return new Minute(ri());
  }
  void w(Minute u){
    w(u.i);
  }
  Second rv(){
    return new Second(ri());
  }
  void w(Second v){
    w(v.i);
  }
  Timespan rn(){
    return new Timespan(rj());
  }
  void w(Timespan n){
    if(vt<1)
      throw new RuntimeException("Timespan not valid pre kdb+2.6");
    w(n.j);
  }

  /** {@code Timezone} to use for temporal types serialisation. */
  public TimeZone tz=TimeZone.getDefault();
  static long k=86400000L*10957, n=1000000000L;
  long o(long x){
    return tz.getOffset(x);
  }
  long lg(long x){
    return x+o(x);
  }
  long gl(long x){
    return x-o(x-o(x));
  }
  Date rd(){
    int i=ri();
    return new Date(i==ni?nj:gl(k+86400000L*i));
  }
  void w(Date d){
    long j=d.getTime();
    w(j==nj?ni:(int)(lg(j)/86400000-10957));
  }
  Time rt(){
    int i=ri();
    return new Time(i==ni?nj:gl(i));
  }
  void w(Time t){
    long j=t.getTime();
    w(j==nj?ni:(int)(lg(j)%86400000));
  }
  java.util.Date rz(){
    double f=rf();
    return new java.util.Date(Double.isNaN(f)?nj:gl(k+Math.round(8.64e7*f)));
  }
  void w(java.util.Date z){
    long j=z.getTime();
    w(j==nj?nf:(lg(j)-k)/8.64e7);
  }
  Timestamp rp(){
    long j=rj(), d=j<0?(j+1)/n-1:j/n;
    Timestamp p=new Timestamp(j==nj?j:gl(k+1000*d));
    if(j!=nj)
      p.setNanos((int)(j-n*d));
    return p;
  }
  void w(Timestamp p){
    long j=p.getTime();
    if(vt<1)
      throw new RuntimeException("Timestamp not valid pre kdb+2.6");
    w(j==nj?j:1000000*(lg(j)-k)+p.getNanos()%1000000);
  }
  String rs() throws UnsupportedEncodingException{
    int i=j;
    for(;b[j++]!=0;);
    return (i==j-1)?"":new String(b,i,j-1-i,encoding);
  }
  void w(String s) throws UnsupportedEncodingException{
    int i=0, n;
    if(s!=null){
      n=ns(s);
      byte[] b=s.getBytes(encoding);
      for(;i<n;)
        w(b[i++]);
    }
    B[J++]=0;
  }
  /** 
   * Deserializes the contents of the incoming message buffer {@code b}. 
   * 
   * @return deserialised object
   * @throws UnsupportedEncodingException If the named charset is not supported
   */
  Object r() throws UnsupportedEncodingException{
    int i=0, n, t=b[j++];
    if(t<0)
      switch(t){
        case -1:
          return rb();
        case (-2):
          return rg();
        case -4:
          return b[j++];
        case -5:
          return rh();
        case -6:
          return ri();
        case -7:
          return rj();
        case -8:
          return re();
        case -9:
          return rf();
        case -10:
          return rc();
        case -11:
          return rs();
        case -12:
          return rp();
        case -13:
          return rm();
        case -14:
          return rd();
        case -15:
          return rz();
        case -16:
          return rn();
        case -17:
          return ru();
        case -18:
          return rv();
        case -19:
          return rt();
      }
    if(t>99){
      if(t==100){
        rs();
        return r();
      }
      if(t<104)
        return b[j++]==0&&t==101?null:"func";
      if(t>105)
        r();
      else
        for(n=ri();i<n;i++)
          r();
      return "func";
    }
    if(t==99)
      return new Dict(r(),r());
    j++;
    if(t==98)
      return new Flip((Dict)r());
    n=ri();
    switch(t){
      case 0:
        Object[] L=new Object[n];
        for(;i<n;i++)
          L[i]=r();
        return L;
      case 1:
        boolean[] B=new boolean[n];
        for(;i<n;i++)
          B[i]=rb();
        return B;
      case 2: {
        UUID[] G=new UUID[n];
        for(;i<n;i++)
          G[i]=rg();
        return G;
      }
      case 4:
        byte[] G=new byte[n];
        for(;i<n;i++)
          G[i]=b[j++];
        return G;
      case 5:
        short[] H=new short[n];
        for(;i<n;i++)
          H[i]=rh();
        return H;
      case 6:
        int[] I=new int[n];
        for(;i<n;i++)
          I[i]=ri();
        return I;
      case 7:
        long[] J=new long[n];
        for(;i<n;i++)
          J[i]=rj();
        return J;
      case 8:
        float[] E=new float[n];
        for(;i<n;i++)
          E[i]=re();
        return E;
      case 9:
        double[] F=new double[n];
        for(;i<n;i++)
          F[i]=rf();
        return F;
      case 10:
        char[] C=new String(b,j,n,encoding).toCharArray();
        j+=n;
        return C;
      case 11:
        String[] S=new String[n];
        for(;i<n;i++)
          S[i]=rs();
        return S;
      case 12:
        Timestamp[] P=new Timestamp[n];
        for(;i<n;i++)
          P[i]=rp();
        return P;
      case 13:
        Month[] M=new Month[n];
        for(;i<n;i++)
          M[i]=rm();
        return M;
      case 14:
        Date[] D=new Date[n];
        for(;i<n;i++)
          D[i]=rd();
        return D;
      case 15:
        java.util.Date[] Z=new java.util.Date[n];
        for(;i<n;i++)
          Z[i]=rz();
        return Z;
      case 16:
        Timespan[] N=new Timespan[n];
        for(;i<n;i++)
          N[i]=rn();
        return N;
      case 17:
        Minute[] U=new Minute[n];
        for(;i<n;i++)
          U[i]=ru();
        return U;
      case 18:
        Second[] V=new Second[n];
        for(;i<n;i++)
          V[i]=rv();
        return V;
      case 19:
        Time[] T=new Time[n];
        for(;i<n;i++)
          T[i]=rt();
        return T;
    }
    return null;
  }

//object.getClass().isArray()   t(int[]) is .5 isarray is .1 lookup .05
  /**
   *  Gets the numeric type of the supplied object used in kdb+.
   * 
   * @param x Object to get the numeric type of
   * @return kdb+ type number for an object
   */
  public static int t(Object x){
    return x instanceof Boolean?-1:x instanceof UUID?-2:x instanceof Byte?-4:x instanceof Short?-5:x instanceof Integer?-6:x instanceof Long?-7:x instanceof Float?-8:x instanceof Double?-9:x instanceof Character?-10:x instanceof String?-11
      :x instanceof Date?-14:x instanceof Time?-19:x instanceof Timestamp?-12:x instanceof java.util.Date?-15:x instanceof Timespan?-16:x instanceof Month?-13:x instanceof Minute?-17:x instanceof Second?-18
      :x instanceof boolean[]?1:x instanceof UUID[]?2:x instanceof byte[]?4:x instanceof short[]?5:x instanceof int[]?6:x instanceof long[]?7:x instanceof float[]?8:x instanceof double[]?9:x instanceof char[]?10:x instanceof String[]?11
      :x instanceof Date[]?14:x instanceof Time[]?19:x instanceof Timestamp[]?12:x instanceof java.util.Date[]?15:x instanceof Timespan[]?16:x instanceof Month[]?13:x instanceof Minute[]?17:x instanceof Second[]?18
      :x instanceof Flip?98:x instanceof Dict?99:0;
  }
  /**
   * "number of bytes from type." A helper for nx, to assist in calculating the number of bytes required to serialize a
   * particular type.
   */
  static int[] nt={0,1,16,0,1,2,4,8,4,8,1,0,8,4,4,8,8,4,4,4};
  /**
   * A helper function for nx, calculates the number of bytes which would be required to serialize the supplied string.
   * 
   * @param s String to be serialized
   * @return number of bytes required to serialise a string
   * 
   * @throws UnsupportedEncodingException  If the named charset is not supported
   */
  static int ns(String s) throws UnsupportedEncodingException{
    int i;
    if(s==null)
      return 0;
    if(-1<(i=s.indexOf('\000')))
      s=s.substring(0,i);
    return s.getBytes(encoding).length;
  }
  /**
   * A helper function for nx, returns the number of elements in the supplied object.
   * e.g. for a Dict, the number of keys
   *      for a Flip, the number of rows
   *      an array, the length of the array
   * 
   * @param x Object to be serialized
   * 
   * @return number of elements in an object.
   * 
   * @throws UnsupportedEncodingException  If the named charset is not supported
   */
  public static int n(Object x) throws UnsupportedEncodingException{
    return x instanceof Dict?n(((Dict)x).x):x instanceof Flip?n(((Flip)x).y[0]):x instanceof char[]?new String((char[])x).getBytes(encoding).length:Array.getLength(x);
  }
  /**
   * Calculates the number of bytes which would be required to serialize the supplied object.
   * 
   * @param x Object to be serialized
   * 
   * @return number of bytes required to serialise an object.
   * 
   * @throws UnsupportedEncodingException  If the named charset is not supported
   */
  public int nx(Object x) throws UnsupportedEncodingException{
    int i=0, n, t=t(x), j;
    if(t==99)
      return 1+nx(((Dict)x).x)+nx(((Dict)x).y);
    if(t==98)
      return 3+nx(((Flip)x).x)+nx(((Flip)x).y);
    if(t<0)
      return t==-11?2+ns((String)x):1+nt[-t];
    j=6;
    n=n(x);
    if(t==0||t==11)
      for(;i<n;++i)
        j+=t==0?nx(((Object[])x)[i]):1+ns(((String[])x)[i]);
    else
      j+=n*nt[t];
    return j;
  }
  void w(Object x) throws UnsupportedEncodingException{
    int i=0, n, t=t(x);
    w((byte)t);
    if(t<0)
      switch(t){
        case -1:
          w(((Boolean)x).booleanValue());
          return;
        case -2:
          w((UUID)x);
          return;
        case -4:
          w(((Byte)x).byteValue());
          return;
        case -5:
          w(((Short)x).shortValue());
          return;
        case -6:
          w(((Integer)x).intValue());
          return;
        case -7:
          w(((Long)x).longValue());
          return;
        case -8:
          w(((Float)x).floatValue());
          return;
        case -9:
          w(((Double)x).doubleValue());
          return;
        case -10:
          w(((Character)x).charValue());
          return;
        case -11:
          w((String)x);
          return;
        case -12:
          w((Timestamp)x);
          return;
        case -13:
          w((Month)x);
          return;
        case -14:
          w((Date)x);
          return;
        case -15:
          w((java.util.Date)x);
          return;
        case -16:
          w((Timespan)x);
          return;
        case -17:
          w((Minute)x);
          return;
        case -18:
          w((Second)x);
          return;
        case -19:
          w((Time)x);
          return;
      }
    if(t==99){
      Dict r=(Dict)x;
      w(r.x);
      w(r.y);
      return;
    }
    B[J++]=0;
    if(t==98){
      Flip r=(Flip)x;
      B[J++]=99;
      w(r.x);
      w(r.y);
      return;
    }
    w(n=n(x));
    if(t==10){
      byte[] b=new String((char[])x).getBytes(encoding);
      for(;i<b.length;)
        w(b[i++]);
    }else
      for(;i<n;++i)
        if(t==0)
          w(((Object[])x)[i]);
        else if(t==1)
          w(((boolean[])x)[i]);
        else if(t==2)
          w(((UUID[])x)[i]);
        else if(t==4)
          w(((byte[])x)[i]);
        else if(t==5)
          w(((short[])x)[i]);
        else if(t==6)
          w(((int[])x)[i]);
        else if(t==7)
          w(((long[])x)[i]);
        else if(t==8)
          w(((float[])x)[i]);
        else if(t==9)
          w(((double[])x)[i]);
        else if(t==11)
          w(((String[])x)[i]);
        else if(t==12)
          w(((Timestamp[])x)[i]);
        else if(t==13)
          w(((Month[])x)[i]);
        else if(t==14)
          w(((Date[])x)[i]);
        else if(t==15)
          w(((java.util.Date[])x)[i]);
        else if(t==16)
          w(((Timespan[])x)[i]);
        else if(t==17)
          w(((Minute[])x)[i]);
        else if(t==18)
          w(((Second[])x)[i]);
        else
          w(((Time[])x)[i]);
  }

  /**
   * Serialises {@code x} object as {@code byte[]} array.
   * 
   * @param msgType type of the ipc message
   * @param x object to serialise
   * @param zip true if to attempt compress serialised output
   * @return {@code B} containing serialised representation
   * 
   * @throws IOException should not throw
   */
  public byte[] serialize(int msgType,Object x,boolean zip)throws IOException{
    int length=8+nx(x);
    synchronized(o){
      B=new byte[length];
      B[0]=0;
      B[1]=(byte)msgType;
      J=4;
      w(length);
      w(x);
      if(zip&&J>2000&&!l)
        compress();
      return B;
    }
  }

  /**
   * Deserialises {@code buffer} q ipc as an object
   * 
   * @param buffer byte[] to deserialise object from 
   * @return deserialised object
   * 
   * @throws KException if buffer contains kdb+ error object.
   * @throws UnsupportedEncodingException  If the named charset is not supported
   */
  public Object deserialize(byte[]buffer)throws KException, UnsupportedEncodingException{
    synchronized(i){
      b=buffer;
      a=b[0]==1;  // endianness of the msg 
      boolean compressed=b[2]==1;
      j=8;      
      if(compressed)
        uncompress();
      if(b[8]==-128){
        j=9;
        throw new KException(rs());
      }
      return r(); // deserialize the message
    }    
  }
  protected void w(int msgType,Object x) throws IOException{
    synchronized(o){
      byte[] buffer=serialize(msgType,x,zip);
      o.write(buffer,0,buffer.length);
    }
  }
  /**
   * Sends a response message to the remote kdb+ process. This should be called only during processing of an incoming sync message.
   * 
   * @param obj Object to send to the remote
   * 
   * @throws IOException if not expecting any response
   */
  public void kr(Object obj) throws IOException{
    if(sync==0)
      throw new IOException("Unexpected response msg");
    sync--;
    w(2,obj);
  }
  /**
   * Sends an error as a response message to the remote kdb+ process. This should be called only during processing of an incoming sync message.
   * 
   * @param text The error message text
   * 
   * @throws IOException unexpected error message
   */
  public void ke(String text) throws IOException{
    if(sync==0)
      throw new IOException("Unexpected error msg");
    sync--;
    int n=2+ns(text)+8;
    synchronized(o){
      B=new byte[n];
      B[0]=0;
      B[1]=2;
      J=4;
      w(n);
      w((byte)-128);
      w(text);
      o.write(B);
    }
  }
  /**
   * Sends an async message to the remote kdb+ process. This blocks until the serialized data has been written to the
   * socket. On return, there is no guarantee that this msg has already been processed by the remote process.
   * 
   * @param expr The expression to send
   * 
   * @throws IOException if an I/O error occurs.
   */
  public void ks(String expr) throws IOException{
    w(0,cs(expr));
  }
  /**
   * Sends an async message to the remote kdb+ process. This blocks until the serialized data has been written to the
   * socket. On return, there is no guarantee that this msg has already been processed by the remote process.
   * 
   * @param obj The object to send
   * 
   * @throws IOException if an I/O error occurs.
   */
  public void ks(Object obj) throws IOException{
    w(0,obj);
  }
  char[] cs(String s){
    return s.toCharArray();
  }
  /**
   * Sends an async message to the remote kdb+ process. This blocks until the serialized data has been written to the
   * socket. On return, there is no guarantee that this msg has already been processed by the remote process. Use this to
   * invoke a function in kdb+ which takes a single argument and does not return a value. e.g. to invoke f[x] use
   * ks("f",x); to invoke a lambda, use ks("{x}",x);
   * 
   * @param s The name of the function, or a lambda itself
   * @param x The argument to the function named in s
   * 
   * @throws IOException if an I/O error occurs.
   */
  public void ks(String s,Object x) throws IOException{
    Object[] a={cs(s),x};
    w(0,a);
  }
  /**
   * Sends an async message to the remote kdb+ process. This blocks until the serialized data has been written to the
   * socket. On return, there is no guarantee that this msg has already been processed by the remote process. Use this to
   * invoke a function in kdb+ which takes 2 arguments and does not return a value. e.g. to invoke f[x;y] use ks("f",x,y);
   * to invoke a lambda, use ks("{x+y}",x,y);
   * 
   * @param s The name of the function, or a lambda itself
   * @param x The first argument to the function named in s
   * @param y The second argument to the function named in s
   * 
   * @throws IOException if an I/O error occurs.
   */
  public void ks(String s,Object x,Object y) throws IOException{
    Object[] a={cs(s),x,y};
    w(0,a);
  }
  /**
   * Sends an async message to the remote kdb+ process. This blocks until the serialized data has been written to the
   * socket. On return, there is no guarantee that this msg has already been processed by the remote process. Use this to
   * invoke a function in kdb+ which takes 3 arguments and does not return a value. e.g. to invoke f[x;y;z] use
   * ks("f",x,y,z); to invoke a lambda, use ks("{x+y+z}",x,y,z);
   * 
   * @param s The name of the function, or a lambda itself
   * @param x The first argument to the function named in s
   * @param y The second argument to the function named in s
   * @param z The third argument to the function named in s
   * 
   * @throws IOException if an I/O error occurs.
   */
  public void ks(String s,Object x,Object y,Object z) throws IOException{
    Object[] a={cs(s),x,y,z};
    w(0,a);
  }
  /**
   * Reads an incoming message from the remote kdb+ process. This blocks until a single message has been received and
   * deserialized. This is called automatically during a sync request via k(String s,..). It can be called explicitly when
   * subscribing to a publisher.
   * 
   * @return an Object array of {messageType,deserialised object}
   * 
   * @throws KException if response contains an error
   * @throws IOException if an I/O error occurs.
   * @throws UnsupportedEncodingException If the named charset is not supported
   */
  public Object[] readMsg() throws KException,IOException,UnsupportedEncodingException{
    synchronized(i){
      i.readFully(b=new byte[8]); // read the msg header
      a=b[0]==1;  // endianness of the msg
      if(b[1]==1) // msg types are 0 - async, 1 - sync, 2 - response
        sync++;   // an incoming sync message means the remote will expect a response message
      j=4;
      b=Arrays.copyOf(b,ri());
      i.readFully(b,8,b.length-8); // read the incoming message in full
      return new Object[]{b[1],deserialize(b)};
    }
  }
  /**
   * Reads an incoming message from the remote kdb+ process. This blocks until a single message has been received and
   * deserialized. This is called automatically during a sync request via k(String s,..). It can be called explicitly when
   * subscribing to a publisher.
   * 
   * @return the deserialised object
   * 
   * @throws KException if response contains an error
   * @throws IOException if an I/O error occurs.
   * @throws UnsupportedEncodingException If the named charset is not supported
   */
  public Object k() throws KException,IOException,UnsupportedEncodingException{
    return readMsg()[1];
  }
  /**
   * MsgHandler interface for processing async or sync messages during a sync request whilst awaiting a response message
   * The default implementation
   * - discards async messages
   * - responds to sync messages with an error, otherwise the remote will continue to wait for a response
   */
  public interface MsgHandler{
    default void processMsg(c c,byte msgType,Object msg)throws IOException{
      switch(msgType){
        case 0:System.err.println("discarded unexpected incoming async msg!");break; // implicitly discard incoming async messages
        case 1:c.ke("unable to process sync requests");break; // signal to remote that we're unable to process these by default
        default:throw new IOException("Invalid message type received: "+msgType);
      }
    }
  }
  /**
   * {@code msgHandler} is used for handling incoming async and sync messages whilst awaiting a response message to a sync request
   */
  private MsgHandler msgHandler=null;
  /**
   * Stores the handler in an instance variable
   * 
   * @param handler The handler to store
   */
  public void setMsgHandler(MsgHandler handler){
    msgHandler=handler;
  }
  /**
   * Returns the current msg handler 
   */
  public MsgHandler getMsgHandler(){
    return msgHandler;
  }
  /**
   * Sends a sync message to the remote kdb+ process. This blocks until the message has been sent in full, and, if a MsgHandler
   * is set, will process any queued, incoming async or sync message in order to reach the response message.
   * 
   * @param x The object to send
   * @return deserialised response to request {@code x}
   * 
   * @throws KException if request evaluation resulted in an error
   * @throws IOException if an I/O error occurs.
   */
  public synchronized Object k(Object x) throws KException,IOException{
    w(1,x);
    while(true){
      Object[]msg=readMsg();
      if(msgHandler==null||(byte)msg[0]==(byte)2) // if there's no handler or the msg is a response msg, return it
        return msg[1];
      msgHandler.processMsg(this,(byte)msg[0],msg[1]); // process async and sync requests
    }
  }
  /**
   * Sends a sync message to the remote kdb+ process. This blocks until the message has been sent in full, and a message
   * is received from the remote; typically the received message would be the corresponding response message.
   * 
   * @param expr The expression to send
   * @return deserialised response to request {@code x}
   * 
   * @throws KException if request evaluation resulted in an error
   * @throws IOException if an I/O error occurs.
   */
  public Object k(String expr) throws KException,IOException{
    return k(cs(expr));
  }
  /**
   * Sends a sync message to the remote kdb+ process. This blocks until the message has been sent in full, and a message
   * is received from the remote; typically the received message would be the corresponding response message. Use this to
   * invoke a function in kdb+ which takes a single argument and returns a value. e.g. to invoke f[x] use k("f",x); to
   * invoke a lambda, use k("{x}",x);
   * 
   * @param s The name of the function, or a lambda itself
   * @param x The argument to the function named in s   
   * @return deserialised response to request {@code s} with params {@code x}
   * 
   * @throws KException if request evaluation resulted in an error
   * @throws IOException if an I/O error occurs.
   */
  public Object k(String s,Object x) throws KException,IOException{
    Object[] a={cs(s),x};
    return k(a);
  }
  /**
   * Sends a sync message to the remote kdb+ process. This blocks until the message has been sent in full, and a message
   * is received from the remote; typically the received message would be the corresponding response message. Use this to
   * invoke a function in kdb+ which takes arguments and returns a value. e.g. to invoke f[x;y] use k("f",x,y); to invoke
   * a lambda, use k("{x+y}",x,y);
   * 
   * @param s The name of the function, or a lambda itself
   * @param x The first argument to the function named in s
   * @param y The second argument to the function named in s
   * @return deserialised response to the request
   * 
   * @throws KException if request evaluation resulted in an error
   * @throws IOException if an I/O error occurs.
   */
  public Object k(String s,Object x,Object y) throws KException,IOException{
    Object[] a={cs(s),x,y};
    return k(a);
  }
  /**
   * Sends a sync message to the remote kdb+ process. This blocks until the message has been sent in full, and a message
   * is received from the remote; typically the received message would be the corresponding response message. Use this to
   * invoke a function in kdb+ which takes 3 arguments and returns a value. e.g. to invoke f[x;y;z] use k("f",x,y,z); to
   * invoke a lambda, use k("{x+y+z}",x,y,z);
   * 
   * @param s The name of the function, or a lambda itself
   * @param x The first argument to the function named in s
   * @param y The second argument to the function named in s
   * @param z The third argument to the function named in s
   * @return deserialised response to the request
   * 
   * @throws KException if request evaluation resulted in an error
   * @throws IOException if an I/O error occurs.
   */
  public Object k(String s,Object x,Object y,Object z) throws KException,IOException{
    Object[] a={cs(s),x,y,z};
    return k(a);
  }
  /** Array containing null object for corresponing kdb+ type number(0-19). For example {@code "".equals(NULL[11])} */
  public static Object[] NULL={null,new Boolean(false),new UUID(0,0),null,new Byte((byte)0),new Short(Short.MIN_VALUE),new Integer(ni),new Long(nj),new Float(nf),new Double(nf),new Character(' '),"",
    new Timestamp(nj),new Month(ni),new Date(nj),new java.util.Date(nj),new Timespan(nj),new Minute(ni),new Second(ni),new Time(nj)
  };
  /**
   * Gets a null object for the type indicated by the character.
   * 
   * @param c The shorthand character for the type
   * 
   * @return instance of null object of specified kdb+ type.
   */
  public static Object NULL(char c){
    return NULL[" bg xhijefcspmdznuvt".indexOf(c)];
  }
  /**
   * Tests whether an object is a null object of that type.
   * qn(NULL('j')) should return true
   * 
   * @param x The object to be tested for null
   * 
   * @return true if {@code x} is kdb+ null, false otherwise
   */
  public static boolean qn(Object x){
    int t=-t(x);
    return (t==2||t>4)&&x.equals(NULL[t]);
  }
  /**
   * Gets the object at an index of an array
   * 
   * @param x The array to index
   * @param i The offset to index at
   * @return object at index
   */
  public static Object at(Object x,int i){
    return qn(x=Array.get(x,i))?null:x;
  }
  /**
   * Sets the object at an index of an array
   * 
   * @param x The array to index
   * @param i The offset to index at
   * @param y The object to set at index i
   */
  public static void set(Object x,int i,Object y){
    Array.set(x,i,null==y?NULL[t(x)]:y);
  }
  static int find(String[] x,String y){
    int i=0;
    for(;i<x.length&&!x[i].equals(y);)
      ++i;
    return i;
  }
  /**
   * Removes the key from a keyed table. 
   * <p>
   * A keyed table(a.k.a. Flip) is a dictionary where both key and value are tables
   * themselves. For ease of processing, this method, td, table from dictionary, can be used to remove the key.
   * </p>
   * @param X A table or keyed table.
   * @return A simple table
   * 
   * @throws UnsupportedEncodingException If the named charset is not supported
   */
  public static Flip td(Object X) throws UnsupportedEncodingException{
    if(X instanceof Flip)
      return (Flip)X;
    Dict d=(Dict)X;
    Flip a=(Flip)d.x, b=(Flip)d.y;
    int m=n(a.x), n=n(b.x);
    String[] x=new String[m+n];
    System.arraycopy(a.x,0,x,0,m);
    System.arraycopy(b.x,0,x,m,n);
    Object[] y=new Object[m+n];
    System.arraycopy(a.y,0,y,0,m);
    System.arraycopy(b.y,0,y,m,n);
    return new Flip(new Dict(x,y));
  }
  /** Prints x to {@code out} stream 
   * @param x object to print
   * @return object that has been printed
   */
  public static Object O(Object x){
    out.println(x);
    return x;
  }
  /** 
   * Prints x to {@code out} stream 
   * @param x value to print
   */
  public static void O(int x){
    out.println(x);
  }
  /** 
   * Prints x to {@code out} stream 
   * @param x value to print
   */
  public static void O(boolean x){
    out.println(x);
  }
  /** 
   * Prints x to {@code out} stream 
   * @param x value to print
   */
  public static void O(long x){
    out.println(x);
  }
  /** 
   *Prints x to {@code out} stream
   * @param x value to print
   */
  public static void O(double x){
    out.println(x);
  }
  /** 
   * Current time in milliseconds 
   * @return current time in millis.
   */
  public static long t(){
    return System.currentTimeMillis();
  }
  static long t;
  public static void tm(){
    long u=t;
    t=t();
    if(u>0)
      O(t-u);
  }
  static String i2(int i){
    return new DecimalFormat("00").format(i);
  }
  static String i9(int i){
    return new DecimalFormat("000000000").format(i);
  }
}
