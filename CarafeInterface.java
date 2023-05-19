//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.util.*;
import java.lang.*;
import rosas.lou.runnables.*;
//
//
//
//

public interface CarafeInterface{
   public void   pour(Mug mug) throws NotPulledException;
   public double quantity();
   public void   stopPour();
}
//////////////////////////////////////////////////////////////////////
