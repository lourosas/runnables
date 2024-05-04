//////////////////////////////////////////////////////////////////////
/*
Copyright 2024 Lou Rosas

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
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import rosas.lou.runnables.*;

public class TestHarness{
   //
   //
   //
   public TestHarness(){}

   //
   //
   //
   public long timeTasks(int nThreads, Runnable task)
   throws InterruptedException{
      CountDownLatch startGate = new CountDownLatch(1);
      CountDownLatch endGate   = new CountDownLatch(nThreads);

      for(int i = 0; i < nThreads; ++i){
         Thread t = new Thread(){
            public void run(){
               try{
                  startGate.await();
                  try{
                     task.run();
                  }
                  finally{
                     endGate.countDown();
                  }
               }
               catch(InterruptedException ie){}
            }
         };
         t.start();
      }
      long start = System.nanoTime();
      startGate.countDown();
      endGate.await();
      long end   = System.nanoTime();
      return end - start;
   }

}
//////////////////////////////////////////////////////////////////////
