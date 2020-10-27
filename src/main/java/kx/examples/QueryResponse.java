package kx.examples;
import java.util.logging.Logger;
import java.util.logging.Level;
import kx.c;
public class QueryResponse{
  private static final Logger LOGGER = Logger.getLogger(QueryResponse.class.getName());
  public static void main(String[] args){
    c c=null;
    try{
      c=new c("localhost",5010,System.getProperty("user.name")+":mypassword");
      Object result=c.k("2+3");
      LOGGER.log(Level.INFO,"result is {0}",result);
    }catch(Exception e){
      LOGGER.log(Level.SEVERE,e.toString());
    }finally{
      try{if(c!=null)c.close();}catch(java.io.IOException e){
        // ingnore exception
      }
    }
  }
}
