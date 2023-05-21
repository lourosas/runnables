//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swimg.*;
import rosas.lou.runnables.*;

/**/
public class CoffeeMakerController
implements ActionListener, KeyListener, ItemListener{
   private Suscriber _subscriber;
   private Maker     _maker;
   
   {
      _subscriber = null;
      _maker      = null;
   };

   ///////////////////////////Constructors////////////////////////////
   /**/
   public CoffeeMakerController(){}

   /**/
   public CoffeeMakerController(Subscriber sub){}

   /**/
   public CoffeeMakerController(Subscriber sub, Maker maker){}

   ///////////////////////////Public Methods//////////////////////////
   /**/
   public void addSuscriber(Subscriber subscriber){}

   /**/
   public void addModel(Maker maker){}

   /////////////////////////Protected Methods/////////////////////////
   ///////////////////////////Private Methods/////////////////////////
   /////////////////////Interface Implementation//////////////////////
   //////////////////////////Action Listener//////////////////////////
   /**/
   public void actionPerformed(ActionEvent e){}

   ///////////////////////////Key Listener////////////////////////////
   /**/
   public void keyPressed(KeyEvent ke){}
   /**/
   public void keyReleased(KeyEvent ke){}
   /**/
   public void keyTyped(KeyEvent ke){}

   //////////////////////////Item Listener////////////////////////////
   /**/
   public void itemStateChanged(ItemEvent ie){}
}
//////////////////////////////////////////////////////////////////////
