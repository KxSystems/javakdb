package com.kx.examples;
import com.kx.c;
import java.security.SecureRandom;
import java.util.logging.Logger;
import java.util.logging.Level;
/**
 * Example of creating an update function remotely (to capture table inserts), 
 * along with table creation and population of the table. 
 * Table population has an example of single row inserts (lower latency) and 
 * bulk inserts (better throughput and resource utilization).
 */
public class Feed{
  private static final Logger LOGGER = Logger.getLogger(Feed.class.getName());
  private static final String QFUNC = ".u.upd";
  private static final String TABLENAME = "mytable";
  private Feed(){}
  /**
   * Example of 10 single row inserts to a table
   * @param kconn Connection that will be sent the inserts
   * @throws java.io.IOException when issue with KDB+ comms
   */
  static void rowInserts(c kconn) throws java.io.IOException{
    // Single row insert - not as efficient as bulk insert
    LOGGER.log(Level.INFO,"Populating 'mytable' on kdb server with 10 rows...");
    for(int i=0;i<10;i++){
      // Assumes a remote schema of mytable:([]time:`timespan$();sym:`symbol$();price:`float$();size:`long$())
      Object[] row={new c.Timespan(),"SYMBOL",Double.valueOf(93.5),Long.valueOf(300L)};
      kconn.ks(QFUNC,TABLENAME,row);
    }
  }

  /**
   * Example of bulk inserts to a table to improve throughput
   * @param kconn Connection that will be sent the inserts
   * @throws java.io.IOException when issue with KDB+ comms
   * @throws c.KException if request evaluation resulted in an error
   */
  static void bulkInserts(c kconn) throws java.io.IOException, c.KException{
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
    LOGGER.log(Level.INFO,"Populating 'mytable' with a 10 row bulk insert (without column names)...");
    kconn.ks(QFUNC,TABLENAME,new Object[]{time,sym,price,size});
    // if we did want to supply a flip, it can be done as
    LOGGER.log(Level.INFO,"Populating 'mytable' with a 10 row bulk insert (using Flip with column names)...");
    kconn.ks(QFUNC,TABLENAME,new c.Flip(new c.Dict(new String[]{"time","sym","price","size"},new Object[]{time,sym,price,size})));
    kconn.k(""); // sync chase ensures the remote has processed all msgs
  }

  /**
   * Run example tick feed
   * Requires a KDB+ server running on port 5010 on your machine i.e. q -p 5010
   * Depends on a .u.upd function being defined and a table name 'mytable' pre-existing
   * @param args not used
   */
  public static void main(String[] args){
    c c=null;
    try{
      c=new c("localhost",5010,System.getProperty("user.name")+":mypassword");
      rowInserts(c);
      bulkInserts(c);
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
