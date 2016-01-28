package jtkdemo.mosaic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.mines.jtk.awt.ModeManager;
import edu.mines.jtk.mosaic.CrossHairMode;
import edu.mines.jtk.mosaic.GridView.Style;
import edu.mines.jtk.mosaic.GuidedCrossHairMode;
import edu.mines.jtk.mosaic.GuidedValueMessageMode;
import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import edu.mines.jtk.mosaic.ValueMessageMode;
import edu.mines.jtk.util.ArrayMath;

public class CrosshairAndValueMessageDemo {

  public static void main(String args[]){
    
    // some data
    float[] x = ArrayMath.rampfloat(0,1,100);
    float[] y = ArrayMath.pow(x,2);
    
    // plot it up
    PlotPanel plot = new PlotPanel();
    plot.addPoints(x,y);
    
    // get the ModeManager for the mosaic
    ModeManager mm = plot.getMosaic().getModeManager();
    
    // add a crosshair mode 
    CrossHairMode cHair = new CrossHairMode(mm);
    cHair.setActive(true);
    
    // add a guided crosshair mode
    GuidedCrossHairMode gHair = new GuidedCrossHairMode(mm,y);
    gHair.getGridView().setColor(Color.BLUE);
    gHair.getGridView().setStyle(Style.DASH);
    gHair.setActive(true);
    
    // add some JLabels to hold the valueMessages
    JLabel valueL = new JLabel();
    JLabel gvalueL = new JLabel();
    JPanel southPanel = new JPanel(new GridLayout(0,1));
    southPanel.add(valueL);
    southPanel.add(gvalueL);
    
    // make ValueMessageModes
    ValueMessageMode vmm = new ValueMessageMode(mm,valueL);
    vmm.setActive(true);
    ValueMessageMode gvmm = new GuidedValueMessageMode(mm,gvalueL,"x","f(x)",y);
    gvmm.setActive(true);
    
    
    
    // make a frame
    PlotFrame frame = new PlotFrame(plot);
    frame.add(southPanel,BorderLayout.SOUTH);
    frame.setVisible(true);
    
    
  }
  
}

