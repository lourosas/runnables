//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import rosas.lou.runnables.*;
import myclasses.*;
import rosas.lou.lgraphics.*;

/**/
public class CoffeeMakerView extends GenericJFrame
implements Subscriber{
   private static final HEIGHT = 700;
   private static final WIDTH  = 700;

   /////////////////////////Constructors//////////////////////////////
   /**/
   public CoffeeMakerView(){
      this("");
   }

   /**/
   public CoffeeMakerView(Sring title){
      super(title);
      this.setUpGui();
   }

   ///////////////////////////Public Methods//////////////////////////
   /////////////////////////Protected Methods/////////////////////////
   ///////////////////////////Private Methods/////////////////////////
   /**/
   private void setUpGui(){
      this.setLayout(new BorderLayout());
      this.setSize(WIDTH,HEIGHT);
      this.setResizable(false);

      this.setVisible(true);
   }
}
//////////////////////////////////////////////////////////////////////
