package kx.examples;
import kx.c;
import java.security.SecureRandom;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Feed{
  private static final Logger LOGGER = Logger.getLogger(Feed.class.getName());
  private static final String QFUNC = ".u.upd";
  private static final String TABLENAME = "mytable";
  public static void main(String[] args){// example tick feed
    c c=null;
    try{
      c=new c("localhost",5010,System.getProperty("user.name")+":mypassword");
      // Single row insert - not as efficient as bulk insert
      for(int i=0;i<10;i++){
        // Assumes a remote schema of mytable:([]time:`timespan$();sym:`symbol$();price:`float$();size:`long$())
        Object[] row={new c.Timespan(),"SYMBOL",Double.valueOf(93.5),Long.valueOf(300L)};
        c.ks(QFUNC,TABLENAME,row);
      }
      // Bulk row insert - more efficient
      String[]syms=new String[]{"ABC","DEF","GHI","JKL"}; // symbols to randomly choose from
      // Allocate one array per column
      c.Timespan[] time=new c.Timespan[10];
      String[] sym=new String[10];
      double[] price=new double[10];
      long[] size=new long[10];
      SecureRandom random = new SecureRandom();
      // populate the arrays with sample data
      for(int i=0;i<10;i++){
        time[i]=new c.Timespan();
        sym[i]=syms[random.nextInt(syms.length)]; // choose a random symbol
        price[i]=i;
        size[i]=i*10L;
      }
      // Note that we don't need to supply a flip with columns names for .u.upd.
      // Just the column data in the correct order is sufficient.
      c.ks(QFUNC,TABLENAME,new Object[]{time,sym,price,size});
      // if we did want to supply a flip, it can be done as
      c.ks(QFUNC,TABLENAME,new c.Flip(new c.Dict(new String[]{"time","sym","price","size"},new Object[]{time,sym,price,size})));
      c.k(""); // sync chase ensures the remote has processed all msgs
    }
    catch(Exception e){
      LOGGER.log(Level.SEVERE,e.toString());
    }finally{
      if(c!=null)
        try{c.close();}catch(java.io.IOException e){
          // ingnore exception
        }
    }
  }
}
