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
      while(true){
        Object[] msg=c.readMsg();
        switch((byte)msg[0]){
          case 0:System.out.println("Discarding async message");break;
          case 1:c.kr(msg[1]);break; // sync request, echo msg back to remote
          default:System.err.println("Unrecognized msg type: "+msg[0]);break;
        }
      }
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
