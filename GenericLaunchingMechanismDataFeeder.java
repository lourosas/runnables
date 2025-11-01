//////////////////////////////////////////////////////////////////////
/*
Copyright 2025 Lou Rosas

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
import java.text.*;
import java.time.*;
import java.time.format.*;
import rosas.lou.runnables.*;
import rosas.lou.clock.*;

public class GenericLaunchingMechanismDataFeeder implements
DataFeeder,Runnable{
   private LaunchStateSubstate.State INIT             = null;
   private LaunchStateSubstate.State PREL             = null;
   private LaunchStateSubstate.State IGNI             = null;
   private LaunchStateSubstate.State LAUN             = null;
   private LaunchStateSubstate.State ASCE             = null;
   private LaunchStateSubstate.PreLaunchSubstate SET  = null;
   private LaunchStateSubstate.PreLaunchSubstate CONT = null;
   private LaunchStateSubstate.PreLaunchSubstate FUEL = null;
   private LaunchStateSubstate.PreLaunchSubstate HOLD = null;
   private LaunchStateSubstate.IgnitionSubstate  IGN  = null;
   private LaunchStateSubstate.IgnitionSubstate  BUP  = null;
   private LaunchStateSubstate.AscentSubstate    STG  = null;
   private LaunchStateSubstate.AscentSubstate    IGNE = null;

   //Read In
   private LaunchingMec
   //Calculated
}
//////////////////////////////////////////////////////////////////////
