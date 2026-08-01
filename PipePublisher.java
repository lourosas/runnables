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


import java.util.*;
import java.io.*;
import rosas.lou.runnables.*;

public class PipePublisher implements Publisher{
   private List<Subscriber>  _subscribers;
   private PipeData          _pipeData;
   private Exception         _exception;

   {
      _subscribers  = null;
      _pipeData     = null;
      _exception    = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public PipePublisher(){}

   //
   //
   //
   public PipePublisher(PipeData data){}

   //
   //
   //
   public PipePublisher(Exception exception){
      this._exception = exception;
   }

   /////////////////Publisher Interface Implementation////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){}

   //
   //
   //
   public void publish(){}

   //
   //
   //
   public void publish(Object data){}

   //
   //
   //
   public void removeSubscriber(Subscriber subscriber){}

   //
   //
   //
   public Object request(){
      return this._pipeData;
   }
}
//////////////////////////////////////////////////////////////////////
