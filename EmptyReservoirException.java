//////////////////////////////////////////////////////////////////////
//
//
//
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
public class EmptyReservoirException extends RuntimeException{
   private Object _data = null;

   /**/
   public EmptyReservoirException(){
      this("Empty Reservoir Exception");
   }

   /**/
   public EmptyReservoirException(String message){
      super(message);
   }

   /**/
   public EmptyReservoirException(String message, Object data){
      this.data(data);
   }

   /**/
   public Object data(){ return this._data; }

   /**/
   private void data(Object data){ this._data = data; }
}
//////////////////////////////////////////////////////////////////////
