package kx.examples;
import kx.c;
public class Subscriber{
  public static void main(String[] args){// example tick subscriber
    c c=null;
    try{
      c=new c("localhost",5010,System.getProperty("user.name")+":mypassword");
      c.ks(".u.sub","mytable","MSFT");
      while(true)
        System.out.println("Received "+c.k());
    }catch(Exception e){
      e.printStackTrace();
    }finally{
      try{c.close();}catch(java.io.IOException e){}
    }
  }
}
