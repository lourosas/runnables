//////////////////////////////////////////////////////////////////////
/*
Copyright 2026 Lou Rosas

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.io.*;
import rosas.lou.runnables.*;

public class PipeMonitorable implements Monitorable{
   private PipeData _pipeData;
   private Object   _obj;

   {
      _pipeData = null;
      _obj      = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public PipeMonitorable(){
      this._obj = new Object();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void setData(String type, Object data, boolean toAppend){
      String inputType = type.toUpperCase();
      if(inputType.contains("ERROR")){
         this.setError(data,toAppend);
      }
   }

   //
   //
   //
   private void setError(Object data, boolean toAppend){
      try{
        String   err = this._pipeData.error();
        boolean  isE = this._pipeData.isError();
        int      num = this._pipeData.number();
        double   rte = this._pipeData.rate();
        int      stg = this._pipeData.stage();
        int      tnk = this._pipeData.tank();
        double  temp = this._pipeData.temperature();
        double   tol = this._pipeData.tolerance();
        String  type = this._pipeData.type();
        if(toAppend && isE){
           err = err + "\n" + (String)data;
        }
        else{
           err = (String)data;
           isE = true;
        }
        PipeData pd = new GenericPipeData(err,isE,num,rte,stg,tnk,
                                                     temp,tol,type);
        synchronized(this._obj){
           this._pipeData = pd;
        }
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
   }

   //////////////////Monitor Interface Implementation/////////////////
   //
   //
   //
   public void addData(Object data){
      try{
         synchronized(this._obj){
            this._pipeData = (PipeData)data;
         }
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
   }

   //
   //
   //
   public void addData(String type, Object data){}

   //
   //
   //
   public void addError(String error, boolean toAppend){
      this.setData("error",error,toAppend);
   }

   public Object monitor(){
      synchronized(this._obj){
         return this._pipeData;
      }
   }
}
//////////////////////////////////////////////////////////////////////
