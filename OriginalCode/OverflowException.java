//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;

public class OverflowException extends RuntimeException{
   /*
    * */
   public OverflowException(){
      this("Overflow Exception");
   }

   /*
    * */
   public OverflowException(String message){
      super(message);
   }
}
//////////////////////////////////////////////////////////////////////
