/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package jtkdemo.mosaic;

import java.awt.*;
import javax.swing.*;

import edu.mines.jtk.mosaic.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * A simple demonstration of {@link edu.mines.jtk.mosaic.PlotFrame}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.07.03
 */
public class PlotFrameDemo {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new PlotFrameDemo();
      }
    });
  }

  public PlotFrameDemo() {
    float[] x = rampfloat(0.0f,4.0f*FLT_PI/200.0f,201);
    float[] s = sin(x);
    _plotPanel = new PlotPanel();
    _plotPanel.setTitle("The sine function");
    _plotPanel.setHLabel("x");
    _plotPanel.setVLabel("sin(x)");
    _gridView = _plotPanel.addGrid();
    _pointsView = _plotPanel.addPoints(x,s);
    _pointsView.setStyle("r-o");
    _plotFrame = new PlotFrame(_plotPanel);
    _plotFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    _plotFrame.setVisible(true);
    _plotFrame.add(
      new Label("In either plot or axes, click-drag to zoom, click to unzoom."),
      BorderLayout.NORTH);
  }

  public PlotFrame getPlotFrame() {
    return _plotFrame;
  }

  public PlotPanel getPlotPanel() {
    return _plotPanel;
  }

  public PointsView getPointsView() {
    return _pointsView;
  }

  public GridView getGridView() {
    return _gridView;
  }

  private PlotFrame _plotFrame;
  private PlotPanel _plotPanel;
  private PointsView _pointsView;
  private GridView _gridView;
}
