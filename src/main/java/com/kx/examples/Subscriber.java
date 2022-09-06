package com.kx.examples;

import java.util.logging.Logger;
import java.util.logging.Level;
import com.kx.c;
/**
 * Example app that subscribes to real-time updates from a table that 
 * is maintained in KDB+.
 */
public class Subscriber{
  private static final Logger LOGGER = Logger.getLogger(Subscriber.class.getName());
  private Subscriber(){}

  /**
   * Run example tick subscriber
   * Requires a KDB+ server running on port 5010 on your machine. 
   * The KDB+ instance must have the .u.sub function defined
   * @param args not used
   */
  public static void main(String[] args){
    c c=null;
    try{
      c=new c("localhost",5010,System.getProperty("user.name")+":mypassword");
      c.ks(".u.sub","mytable","MSFT");
      boolean ok = true;
      while(ok){
        try {
          LOGGER.log(Level.INFO,"Received {0}",c.k());
        }
        catch(Exception e){
          LOGGER.log(Level.SEVERE,"Exception receiving msg {0}",e.toString());
          ok=false;
        }
      }
    }catch(Exception e){
      LOGGER.log(Level.SEVERE,e.toString());
    }finally{
      if(c!=null)
        try{c.close();}catch(java.io.IOException e){
          // ingnore exception
        }
    }
  }
}
