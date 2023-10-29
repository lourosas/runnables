//////////////////////////////////////////////////////////////////////
//
//
//
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
public class EmptyCarafeException extends RuntimeException{
   /**/
   public EmptyCarafeException(){
      this("Empty Reservoir Excption");
   }

   /**/
   public EmptyCarafeException(String message){
      super(message);
   }
}
//////////////////////////////////////////////////////////////////////
