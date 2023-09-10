/*
Copyright 2023 Lou Rosas

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

import java.lang.*;
import java.util.*;
import rosas.lou.clock.*;

public class TestTimer4{
   public static void main(String [] args){
      new TestTimer4();
   }

   public TestTimer4(){
      LClock clock   = new LClock();
      LTimer timer   = new LTimer(clock);
      LTimerController controller = new LTimerController();
      LTimerView ltv = new LTimerView("Test Timer", controller);
      //Will have to come up with a better way, but do this for now
      //Technically, everything should go through the Controller
      timer.addSubscriber(ltv);
      controller.addView(ltv);
      controller.addModel(timer);
      Thread t = new Thread(clock, "clock");
      t.start();
   }
}
