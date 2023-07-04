//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;

public class AlreadyBrewingException extends RuntimeException{
   /*
    * */
   public AlreadyBrewingException(){
      this("Already Brewing");
   }

   /*
    * */
   public AlreadyBrewingException(String message){
      super(message);
   }
}
//////////////////////////////////////////////////////////////////////
