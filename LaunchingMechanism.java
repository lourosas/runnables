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
import java.io.*;
import rosas.lou.runnables.*;
import rosas.lou.clock.*;

public interface LaunchingMechanism{
   public void addDataFeeder(DataFeeder f);
   public void addDataFeeder(String feederFile)throws IOException;
   public void addErrorListener(ErrorListener e);
   public void addSystemListener(SystemListener s);
   public void initialize(String file)throws IOException;
   public LaunchingMechanismData monitor();
   public LaunchingMechanismData monitorInitialization();
   public LaunchingMechanismData monitorPrelaunch();
   public LaunchingMechanismData monitorIgnition();
   public LaunchingMechanismData monitorLaunch();
   public LaunchingMechanismData monitorPostlaunch();
   public void release();
   public void setStateSubstate(LaunchStateSubstate cond);
   public double supportedWeight();
   public String toString();
}
//////////////////////////////////////////////////////////////////////
