package kx.examples;
import kx.c;
import java.util.concurrent.ThreadLocalRandom;
public class Feed{
  public static void main(String[] args){// example tick feed
    c c=null;
    try{
      c=new c("localhost",5010,System.getProperty("user.name")+":mypassword");
      // Single row insert - not as efficient as bulk insert
      for(int i=0;i<10;i++){
        // Assumes a remote schema of mytable:([]time:`timespan$();sym:`symbol$();price:`float$();size:`long$())
        Object[] row={new c.Timespan(),"SYMBOL",new Double(93.5),new Long(300)};
        c.ks(".u.upd","mytable",row);
      }
      // Bulk row insert - more efficient
      String[]syms=new String[]{"ABC","DEF","GHI","JKL"}; // symbols to randomly choose from
      // Allocate one array per column
      c.Timespan[] time=new c.Timespan[10];
      String[] sym=new String[10];
      double[] price=new double[10];
      long[] size=new long[10];
      // populate the arrays with sample data
      for(int i=0;i<10;i++){
        time[i]=new c.Timespan();
        sym[i]=syms[ThreadLocalRandom.current().nextInt(0,syms.length)]; // choose a random symbol
        price[i]=i;
        size[i]=i*10;
      }
      // Note that we don't need to supply a flip with columns names for .u.upd.
      // Just the column data in the correct order is sufficient.
      c.ks(".u.upd","mytable",new Object[]{time,sym,price,size});
      // if we did want to supply a flip, it can be done as
      c.ks(".u.upd","mytable",new c.Flip(new c.Dict(new String[]{"time","sym","price","size"},new Object[]{time,sym,price,size})));
      c.k(""); // sync chase ensures the remote has processed all msgs
    }
    catch(Exception e){
      e.printStackTrace();
    }finally{
      try{c.close();}catch(java.io.IOException e){}
    }
  }
}
