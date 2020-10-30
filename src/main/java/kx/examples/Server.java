package kx.examples;
import java.net.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import kx.c;
public class Server{
  private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

  private static class ServerC extends c implements AutoCloseable{
    ServerC(ServerSocket s)throws java.io.IOException{super(s);}
    @Override
    public void w(int i,Object o)throws java.io.IOException{super.w(i,o);}
    public void close() {
      try {
      super.close();
    } catch (Exception e)
    {
      // do nothing
    }
    }
  }
  public static void main(String[] args){// example echo server for a single client
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
