package kx.examples;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import kx.c;
/**
 * Example of code that can be used to serialize/dezerialise a Java type (array of ints) to KDB+ format.
 * @author charlie
 */
public class SerializationOnly{
  private static final Logger LOGGER = Logger.getLogger(SerializationOnly.class.getName());
  private SerializationOnly(){}
  /**
   * Runs program and prints whether serialization/deserialization of an
   * integer array match.
   * @param s not used
   */
  public static void main(String[]s){
    c c=new c();
    int[]input=new int[50000];
    for(int i=0;i<input.length;i++)input[i]=i%10;
    try{
      LOGGER.log(Level.INFO,"{0}",Arrays.equals(input,(int[])c.deserialize(c.serialize(1,input,true))));
      LOGGER.log(Level.INFO,"{0}",Arrays.equals(input,(int[])c.deserialize(c.serialize(1,input,false))));
    }catch(Exception ex){
      LOGGER.log(Level.SEVERE,null,ex);
    }
  }
}
