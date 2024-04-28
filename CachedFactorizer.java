/*
 * Not sure What the Fuck this is suppossed to do...
 *
 *
 *
 * */
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.math.*;

@ThreadSafe
public class CachedFactorizer{
   @GuardedBy("this") private BigInteger lastNumber;
   @GuardedBy("this") private BigInteger[] lastFactors;
   @GuardedBy("this") private long hits;
   @GuardedBy("this") private long cacheHits;

   //////////////////////Constructor//////////////////////////////////
   //
   //
   //
   public CachedFactorizer(){}

   //////////////////////Public Methods///////////////////////////////
   //
   //
   //
   public synchronized long getHits(){ return this.hits; }

   //
   //
   //
   public synchronized double getChacheHitsRatio(){
      return (double)this.cacheHits/(double)this.hits;
   }

   //
   //
   //
   public void service(BigInteger bi){
      BigInteger    i       =   bi;
      BigInteger[]  factors = null;
      synchronized(this){
         ++this.hits;
         if(i.equals(lastNumber)){
            ++this.cachedHits;
            factors = this.lastFactors.clone();
         }
      }
      if(factors == null){
         factors = factor(i); //WTF???
         synchronized(this){
            lastNumber  = i;
            lastFactors = factors.clone();
         }
      }
   }
}
