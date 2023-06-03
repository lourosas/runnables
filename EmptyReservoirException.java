//////////////////////////////////////////////////////////////////////
//
//
//
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
public class EmptyReservoirException extends RuntimeException{
   /**/
   public EmptyReservoirException(){
      this("Empty Reservoir Excption");
   }

   /**/
   public EmptyReservoirException(String message){
      super(message);
   }
}
//////////////////////////////////////////////////////////////////////
