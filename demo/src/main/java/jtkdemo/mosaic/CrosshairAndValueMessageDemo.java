package jtkdemo.mosaic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import edu.mines.jtk.awt.ModeManager;
import edu.mines.jtk.awt.ModeToggleButton;
import edu.mines.jtk.mosaic.CrossHairMode;
import edu.mines.jtk.mosaic.GridView.Style;
import edu.mines.jtk.mosaic.GuidedCrossHairMode;
import edu.mines.jtk.mosaic.GuidedValueMessageMode;
import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import edu.mines.jtk.mosaic.ValueMessageMode;
import edu.mines.jtk.util.ArrayMath;

public class CrosshairAndValueMessageDemo {

  static JButton xhairB, gxhairB, noxhair;
  static JButton labelB, glabelB, nolabel;
  
  public static void main(String args[]){
    
    // some data
    float[] x = ArrayMath.rampfloat(0,1,100);
    float[] y = ArrayMath.pow(x,2);
    
    // plot it up
    PlotPanel plot = new PlotPanel();
    PlotFrame frame = new PlotFrame(plot);
    plot.addPoints(x,y);
    
    // get the ModeManager for the mosaic
    ModeManager mm = frame.getModeManager();
    
    // add a crosshair mode 
    CrossHairMode xHair = new CrossHairMode(mm);
    
    // add a guided crosshair mode
    GuidedCrossHairMode gHair = new GuidedCrossHairMode(mm,y);
    gHair.getGridView().setColor(Color.BLUE);
    gHair.getGridView().setStyle(Style.DASH);
    
    // add some JLabels to hold the valueMessages
    JLabel valueL = new JLabel();
    JLabel gvalueL = new JLabel();
    JPanel southPanel = new JPanel(new GridLayout(0,1));
    southPanel.add(valueL);
    southPanel.add(gvalueL);
    
    // make ValueMessageModes
    ValueMessageMode vmm = new ValueMessageMode(mm,valueL);
    ValueMessageMode gvmm = new GuidedValueMessageMode(mm,gvalueL,"x: ",", f(x): ",y);
    
    
    // make a toolbar for the modes
    JToolBar tb = new JToolBar(JToolBar.VERTICAL);
    ModeToggleButton mb1 = new ModeToggleButton(xHair);
    mb1.setText("Plain Xhair");
    ModeToggleButton mb2 = new ModeToggleButton(gHair);
    mb2.setText("Guided Xhair");
    ModeToggleButton mb3 = new ModeToggleButton(vmm);
    mb3.setText("Plain Value");
    ModeToggleButton mb4 = new ModeToggleButton(gvmm);
    mb4.setText("Guided Value");    
    
    tb.add(mb1);
    tb.add(mb2);
    tb.add(mb3);
    tb.add(mb4);
    
    
    // set up the frame
    frame.add(southPanel,BorderLayout.SOUTH);
    frame.add(tb,BorderLayout.WEST);
    frame.setVisible(true);
    
    
  }

  
}

