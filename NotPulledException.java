//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;

public class NotPulledException extends RuntimeException{
   /*
    * */
   public NotPulledException(){
      this("Not Pulled Exception");
   }

   /*
    * */
   public NotPulledException(String message){
      super(message);
   }
}
//////////////////////////////////////////////////////////////////////
