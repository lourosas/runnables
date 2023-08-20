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

public class CoffeeMakerViewTwo extends GenericJFrame
implements Subscriber{
   private static final int HEIGHT = 700;
   private static final int WIDTH  = 700;

   private String _powerString;
   private String _stateString;
   private String _carafeStateString;

   private ButtonGroup _powerGroup;
   private CoffeeMakerController _controller;

   {
      _powerString       = null;
      _stateString       = null;
      _carafeStateString = null;
      _controller        = null;
      _powerGroup        = null;   
   };

   //////////////////////////Constructors/////////////////////////////
   //
   //
   //
   public CoffeeMakerViewTwo(CoffeeMakerController controller){
      this("", controller);
   }

   //
   //
   //
   //
   public CoffeeMakerViewTwo
   (
      String title,
      CoffeeMakerController controller
   ){
      super(title);
      this._controller = controller;
      this.setUpGUI();
   }

   ////////////////////////Public Methods/////////////////////////////
   //////////////////////Protected Methods////////////////////////////
   ///////////////////////Private Methods/////////////////////////////
   //
   //
   //
   private void setUpGUI(){
      this.setLayout(new BorderLayout());
      this.setSize(WIDTH,HEIGHT);
      this.setRisizable(false);
      //JPanel centerPanel = this.setUpCenterPanel();
      //JPanel northPanel  = this.setUpNorthPanel();
      //JPanel southPanel  = this.setUpSouthPanel();
      //this.getContentPane().add(northPanel, BorderLayout.NORTH);
      //this.getContentPane().add(centerPanel,BorderLayout.CENTER);
      //this.getContentPane().add(southPanel, BorderLayout.SOUTH);
      this.setVisible(true);
   }

   ////////////////////Interface Methods//////////////////////////////
   //
   //
   //
   public void update(Object o){}

   //
   //
   //
   public void update(Object o, String s){}

   //
   //
   //
   public void error(RuntimeException re){}

   //
   //
   //
   public void error(RuntimeException re, Object o){}

   //
   //
   //
   public void error(String error){}
}
//////////////////////////////////////////////////////////////////////
