package kx.examples;
import java.net.*;
import kx.c;
public class Server{
  private static class ServerC extends c{
    ServerC(ServerSocket s)throws java.io.IOException{super(s);}
    public void w(int i,Object o)throws java.io.IOException{super.w(i,o);}
  };
  public static void main(String[] args){// example echo server for a single client
    int port=5010;
    ServerC c=null;
    try{
      c=new ServerC(new ServerSocket(port));
      while(true)
        c.w(2,c.k());
    }
    catch(Exception e){
      e.printStackTrace();
    }
    finally{
      if(c!=null)
        try{c.close();}catch(java.io.IOException e){}
    }
  }
}
