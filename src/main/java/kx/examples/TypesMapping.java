package kx.examples;
import kx.c;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Calendar;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Time;
import java.util.UUID;
public class TypesMapping{
  private static final Logger LOGGER = Logger.getLogger(TypesMapping.class.getName());

  static LocalDate dateNow(){
    return LocalDate.now();
  }

  static LocalTime timeNow(){
    return LocalTime.now().truncatedTo(ChronoUnit.MILLIS);

  }
  static String getKTypeAsString(short i){
    String result="("+i+")";
    String[]types={"list","boolean","guid","","byte","short","int","long","real","float","char",
                   "symbol","timestamp","month","date","datetime","timespan","minute","second","time","sym"};
    if(Math.abs(i)<types.length)
      result+=types[Math.abs(i)]+(i>0?" vector":"");
    else if(i==99)
      result+="dictionary";
    else if(i==98)
      result+="table";
    return result;
  }
  public static void main(String[] args){
    c c=null;
    try{
      c=new c("localhost",5010,System.getProperty("user.name")+":mypassword");
      Object[]vectors=new Object[]{
        new boolean[]{true},
        new UUID[]{UUID.randomUUID()},
        new byte[]{42},
        new short[]{42},
        new int[]{42},
        new long[]{42},
        new float[]{42.42f},
        new double[]{42.42d},
        new char[]{'a'},
        new String[]{"42"},
        new Instant[]{Instant.now()},
        new c.Month[]{new c.Month(11)},
        new LocalDate[]{dateNow()},
        new LocalDateTime[]{LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)}, // datetime
        new c.Timespan[]{new c.Timespan()},
        new c.Minute[]{new c.Minute(LocalDateTime.now().getMinute())},
        new c.Second[]{new c.Second(LocalDateTime.now().getSecond())},
        new LocalTime[]{timeNow()}};
      String format="|%21s|%21s|%38s|%38s|%5s|\n";
      LOGGER.log(Level.INFO,"{0}",String.format(format,"Java Type","kdb+ Type","Value Sent","kdb+ Value","Match"));
      LOGGER.log(Level.INFO,"{0}",String.format(format,"","","","","").replace(' ', '-'));
      for(Object vector:vectors){
        for(int i=0;i<2;i++){
          boolean asArray=i>0;
          Object arg=asArray?vector:Array.get(vector,0); 
          Object[]result=(Object[])c.k("{(-3!x;type x;x)}",arg); // returns a 3 element list of (stringified x; the type number of x; x)
          LOGGER.log(Level.INFO,"{0}",String.format(format,
                            arg.getClass().toString().substring(6).replace(";",""), // strip leading "class " and trailing semi colon
                            getKTypeAsString((short)result[1]),
                            asArray?Array.get(arg,0):arg,
                            new String((char[])result[0]),
                            result[2].getClass().equals(arg.getClass()) 
                            && asArray?Arrays.deepEquals(new Object[]{result[2]},new Object[]{arg}):result[2].equals(arg)));
        }
      }
      LOGGER.log(Level.INFO,"{0}",String.format(format,"","","","","").replace(' ', '-'));
      Object result=c.k("{x}",vectors);
      LOGGER.log(Level.INFO,"List Roundtrip match: {0}",Arrays.deepEquals(new Object[]{result},new Object[]{vectors}));

      c.Dict dict=new c.Dict(new String[]{"Andrew","James"},new int[]{24,35});
      result=c.k("{x}",dict);
      LOGGER.log(Level.INFO,"Dict Roundtrip match: {0}",(Arrays.deepEquals(new Object[]{dict.x},new Object[]{((c.Dict)result).x})
                        && Arrays.deepEquals(new Object[]{dict.y},new Object[]{((c.Dict)result).y})));

      c.Flip flip=new c.Flip(new c.Dict(new String[]{"time","sym","price","volume"},
                                         new Object[]{new c.Timespan[]{new c.Timespan(),new c.Timespan()},
                                                      new String[]{"ABC","DEF"},
                                                      new double[]{123.456,789.012},
                                                      new long[]{100,200}
                                             }));
      result=c.k("{x}",flip);
      LOGGER.log(Level.INFO,"Flip Roundtrip match: {0}",(Arrays.deepEquals(new Object[]{flip.x},new Object[]{((c.Flip)result).x})
                        && Arrays.deepEquals(flip.y,((c.Flip)result).y)));
    }catch(Exception e){
      LOGGER.log(Level.SEVERE,e.toString());
    }finally{
      try{if(c!=null)c.close();}catch(java.io.IOException e){
        // ingnore exception
      }
    }
  }
}
