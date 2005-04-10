/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dave;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import edu.mines.jtk.dsp.*;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;
import static edu.mines.jtk.mosaic.Mosaic.*;

/**
 * A plot of a sampled sequence x(t) and its amplitude and phase spectra.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.25
 */
public class SpectrumPlot extends JFrame {

    /**
     * Plot width, in pixels.
     */
    public static int WIDTH = 950;

    /**
     * Plot height, in pixels.
     */
    public static int HEIGHT = 600;

    // Attributes shared by all mosaics in plot.
    private static Set<AxesPlacement> AXES_PLACEMENT = EnumSet.of(
      AxesPlacement.LEFT,
      AxesPlacement.BOTTOM
    );
    private static BorderStyle BORDER_STYLE = BorderStyle.FLAT;
    private static Font FONT = new Font("SansSerif",Font.PLAIN,12);
    private static Color BACKGROUND_COLOR = Color.WHITE;

    /**
     * Constructs a new spectrum plot for the specified sampled sequence.
     * @param x the sampled sequence x(t). The sampling must be uniform.
     */
    public SpectrumPlot(Real1 x) {
      this(x,false);
    }

    /**
     * Constructs a new spectrum plot for the specified sampled sequence.
     * @param x the sampled sequence x(t). The sampling must be uniform.
     * @param db true, to plot amplitude spectrum in dB.
     */
    public SpectrumPlot(Real1 x, boolean db) {
      Check.argument(x.getSampling().isUniform(),"sampling of x is uniform");

    // Amplitude and phase spectra.
    Real1[] ap = computeSpectra(x,db);
    Real1 a = ap[0];
    Real1 p = ap[1];

    // Mosaic for sampled sequence x(t).
    Mosaic mosaicX = new Mosaic(1,1,AXES_PLACEMENT,BORDER_STYLE);
    mosaicX.setBackground(BACKGROUND_COLOR);
    mosaicX.setFont(FONT);
    mosaicX.setPreferredSize(new Dimension(WIDTH,1*HEIGHT/3));
    Tile tileX = mosaicX.getTile(0,0);
    LollipopView xv = new LollipopView(x.getSampling(),x.getF());
    tileX.addTiledView(xv);
    TileAxis axisXT = mosaicX.getTileAxisBottom(0);
    TileAxis axisXA = mosaicX.getTileAxisLeft(0);
    axisXA.setLabel("Amplitude");
    axisXT.setLabel("Time (s)");
    axisXT.setFormat("%1.6G");
    
    // Mosaic for amplitude A(f) and phase P(f).
    Mosaic mosaicS = new Mosaic(2,1,AXES_PLACEMENT,BORDER_STYLE);
    mosaicS.setBackground(BACKGROUND_COLOR);
    mosaicS.setFont(FONT);
    mosaicS.setPreferredSize(new Dimension(WIDTH,2*HEIGHT/3));
    Tile tileA = mosaicS.getTile(0,0);
    Tile tileP = mosaicS.getTile(1,0);
    MarkLineView av = new MarkLineView(a.getSampling(),a.getF());
    MarkLineView pv = new MarkLineView(p.getSampling(),p.getF());
    tileA.addTiledView(av);
    tileP.addTiledView(pv);
    tileP.setBestVerticalProjector(new Projector(0.5,-0.5));
    TileAxis axisSA = mosaicS.getTileAxisLeft(0);
    TileAxis axisSP = mosaicS.getTileAxisLeft(1);
    TileAxis axisSF = mosaicS.getTileAxisBottom(0);
    if (db) {
      axisSA.setLabel("Amplitude (dB)");
    } else {
      axisSA.setLabel("Amplitude");
    }
    axisSP.setLabel("Phase (cycles)");
    axisSF.setLabel("Frequency (Hz)");

    // Modes.
    ModeManager modeManager = new ModeManager();
    mosaicX.setModeManager(modeManager);
    mosaicS.setModeManager(modeManager);
    TileZoomMode zoomMode = new TileZoomMode(modeManager);
    zoomMode.setActive(true);

    /*
    // Menu.
    JMenuBar menuBar = new JMenuBar();
    JMenu modeMenu = new JMenu("Mode");
    modeMenu.setMnemonic(KeyEvent.VK_M);
    JMenuItem zoomItem = new ModeMenuItem(zoomMode);
    modeMenu.add(zoomItem);
    menuBar.add(modeMenu);

    // ToolBar.
    JToolBar toolBar = new JToolBar(SwingConstants.VERTICAL);
    JToggleButton zoomButton = new ModeToggleButton(zoomMode);
    toolBar.add(zoomButton);
    */

    JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT,mosaicS,mosaicX);
    jsp.setOneTouchExpandable(true);
    jsp.setResizeWeight(0.7);

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //this.setJMenuBar(menuBar);
    //this.add(toolBar,BorderLayout.WEST);
    this.add(jsp,BorderLayout.CENTER);
    this.pack();
    this.setVisible(true);
  }

  private static MarkLineView makeMarkLineView(
    int nx, double dx, double fx, float[] f) 
  {
    Sampling sx = new Sampling(nx,dx,fx);
    MarkLineView mlv = new MarkLineView(sx,f);
    return mlv;
  }

  private static LollipopView makeLollipopView(
    int nx, double dx, double fx, float[] f) 
  {
    Sampling sx = new Sampling(nx,dx,fx);
    LollipopView lv = new LollipopView(sx,f);
    return lv;
  }

  private Real1[] computeSpectra(Real1 x, boolean db) {
    Sampling st = x.getSampling();
    int nt = st.getCount();
    double dt = st.getDelta();
    double ft = st.getFirst();
    float[] xt = x.getF();
    int nfft = FftReal.nfftSmall(5*nt);
    FftReal fft = new FftReal(nfft);
    int nf = nfft/2+1;
    double df = 1.0/(dt*nfft);
    double ff = 0.0;
    float[] cf = new float[2*nf];
    Rap.copy(nt,xt,cf);
    fft.realToComplex(-1,cf,cf);
    float[] wft = Rap.ramp(0.0f,-2.0f*FLT_PI*(float)(df*ft),nf);
    cf = Cap.mul(cf,Cap.complex(Rap.cos(wft),Rap.sin(wft)));
    float[] af = Cap.abs(cf);
    if (db) {
      float amax = Rap.findMax(af);
      af = Rap.mul(1.0f/amax,af);
      af = Rap.log10(af);
      af = Rap.mul(20.0f,af);
    }
    float[] pf = Cap.arg(cf);
    pf = Rap.mul(0.5f/FLT_PI,pf);
    Sampling sf = new Sampling(nf,df,ff);
    Real1 a = new Real1(sf,af);
    Real1 p = new Real1(sf,pf);
    return new Real1[]{a,p};
  }
}
