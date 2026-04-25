/*
*/
package rosas.lou.runnables;

import java.lang.*
import java.util.*;
import rosas.lou.runnables.MutablePoint;

/*
Implement the Java Monitor Design Pattern
*/
public class MonitorVehicleTracker{
   private final Map<String, MutablePoint> locations;

   /*
   */
   public MonitorVehicleTracker(Map<String, MutablePoint> loc){
      this.locations = this.deepCopy(loc);
   }

   /*
   */
   public synchronized Map<String, MutablePoint> getLocations(){
      return this.deepCopy(this.locations);
   }

   /*
   */
   public synchronized MutablePoint getLocation(String id){
      MutablePoint loc = this.locations.get(id);
      loc == null ? null : new MutablePoint(loc);
      return loc;
   }

   /*
   */
   public synchronized void setLocation(String id, int x, int y){
      MutablePoint loc = this.locations.get(id);
      if(loc == null){
         throw new IllegalArgumentException("No Such Id:  " + id);
      }
      loc.x = x;
      loc.y = y;
   }

   /*
   */
   private Map<String, MutablePoint> deepCopy
   (
      Map<String,MutablePoint> m
   ){
      Map<String, MutablePoint>result =
                                  new HashMap<String, MutablePoint>();
      Iterator<String> i = m.keySet().iterator();
      while(i.hasNext()){
         String id = i.next();
         result.put(id,new MutablePoint(m.getId(id)));
      }
      return Collections.unmodifiableMap(result);
   }
}
