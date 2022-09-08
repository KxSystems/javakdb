package com.kx.examples;
import java.net.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.kx.c;
/**
 * Creates a Java apps that listens on TCP port 5010, which a KDB+ process 
 * can communicate with. It will echo back sync messages and discard async messages. 
 */
public class Server{
  private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
  private Server(){}

  private static class ServerC extends c implements AutoCloseable{
    ServerC(ServerSocket s)throws java.io.IOException{super(s);}
    @Override
    public void w(int i,Object o)throws java.io.IOException{super.w(i,o);}
    @Override
    public void close() {
      try {
      super.close();
    } catch (Exception e)
    {
      // do nothing
    }
    }
  }
  /**
   * Run example echo server for a single client
   * @param args not used
   */
  public static void main(String[] args){
    int port=5010;
    try (ServerC c = new ServerC(new ServerSocket(port))) {
      boolean ok = true;
      while(ok){
        Object[] msg=c.readMsg();
        switch((byte)msg[0]){
          case 0:LOGGER.log(Level.SEVERE,"Discarding async message");break;
          case 1:c.kr(msg[1]);break; // sync request, echo msg back to remote
          default:LOGGER.log(Level.SEVERE,"Unrecognized msg type: {0}. Quitting....",msg[0]);ok=false;break;
        }
      }
    }
    catch(Exception e){
      LOGGER.log(Level.SEVERE,e.toString());
    }
  }
}
