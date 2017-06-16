package kx.examples;
import kx.c;
public class Feed{
  public static void main(String[] args){// example tick feed
    c c=null;
    try{
      c=new c("localhost",5010,System.getProperty("user.name"));
      for(int i=0;i<10;i++){
        // Assumes a remote schema of mytable:([]time:`timespan$();sym:`symbol$();price:`float$();size:`long$())
        Object[]x={new c.Timespan(),"SYMBOL",new Double(93.5),new Integer(300)};
        c.ks(".u.upd","mytable",x);
      }
      c.k(""); // sync chase ensures the remote has processed all msgs
    }
    catch(Exception e){
      e.printStackTrace();
    }finally{
      try{c.close();}catch(java.io.IOException e){}
    }
  }
}
