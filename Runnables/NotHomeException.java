/////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;

public class NotHomeException extends RuntimeException{
   /*
    * */
   public NotHomeException(){
      this("Not Home Exception");
   }

   /*
    * */
   public NotHomeException(String message){
      super(message);
   }
}
/////////////////////////////////////////////////////////////////////
