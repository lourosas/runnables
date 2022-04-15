/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.util.concurrent.*;
import rosas.lou.runnables.*;

public class ACallable<T> implements Callable<T>{
   private T t = null;

   /**/
   public ACallable(T t_){ this.t = t_; }
   ///////////////////////////////////////////////////////////////////
   /**/
   public T call() throws Exception{
      return this.loadType();
   }

   /**/
   private T loadType(){
      return null;
   }
}
