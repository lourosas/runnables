/*
*/

package rosas.lou.runnables;

import java.lang.*;
import java.util.*;

public class MutablePoint{
   public int x;
   public int y;

   {
      x = 0;
      y = 0;
   };

   /*
   */
   public MutablePoint(){}

   /*
   */
   public MutablePoint(int x_, int y_){
      this.x = x_;
      this.y = y_;
   }

   /*
   */
   public MutablePoint(MutablePoint p){
      this.x = p.x;
      this.y = p.y
   }

   /*
   */
   public int getX(){ return this.x; }

   /*
   */
   public int getY(){ return this.y; }

}
