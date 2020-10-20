package kx.examples;
import kx.c;
public class QueryResponse{
  public static void main(String[] args){
    c c=null;
    try{
      c=new c("localhost",5010,System.getProperty("user.name")+":mypassword");
      Object result=c.k("2+3");
      System.out.println("result is "+result);
    }catch(Exception e){
      e.printStackTrace();
    }finally{
      try{if(c!=null)c.close();}catch(java.io.IOException e){}
    }
  }
}
