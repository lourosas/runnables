//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

//
//
//
public class MakerSubscriber implements Subscriber{
   ////////////////////////Public Methods/////////////////////////////
   //
   //Constructor of no arguments
   //
   public MakerSubscriber(){}


   /////////////////////Protected Methods/////////////////////////////
   ///////////////////////Private Methods/////////////////////////////
   /////////////////////Interface Methods/////////////////////////////
   //
   //
   //
   public void update(Object o){}

   //
   //
   //
   public void update(Object o, String s){}

   //
   //
   //
   public void error(RuntimeException re){
      //Temporary fix for the moment...
      System.out.println(re);
   }

   //
   //
   //
   public void error(RuntimeException re, Object o){}

   //
   //
   //
   public void error(String error){
      //Temporary for the moment...but proof of concept...
      System.out.println(error);
   }

}
//////////////////////////////////////////////////////////////////////
