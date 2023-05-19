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
public interface Subscriber{
   public void update(Object o);
   public void update(Object o, String s);
   public void error(RuntimeException re);
   public void error(String error);
}
//////////////////////////////////////////////////////////////////////
