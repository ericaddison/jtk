package edu.mines.jtk.mosaic;


import java.awt.Point;

import edu.mines.jtk.awt.ModeManager;

public class GuidedCrossHairMode extends CrossHairMode {

	private static final long serialVersionUID = 1L;
	private float[] _f;
	
	protected GuidedCrossHairMode(ModeManager manager, float[] f) {
		super(manager);
		setName("GuidedCrossHair");
		setShortDescription("Display crosshairs at location of specified function");
		_f = f;
	}

	
	public void update(Point p){
		if(isActive() && _f != null){
			double ux = _tile.getTranscaler().x((int)p.getX());
			double vx = _tile.getHorizontalProjector().v(ux);
			double vy = getValueAtMouseLocation(_f, ux);
			xhairView.set(vx,vy);
		}
	}
	
	public float getValueAtMouseLocation(float[] x, double ux){
		int ind = (int)( x.length * ux);
		ind = Math.max(0, ind);
		ind = Math.min(ind,x.length-1);
		return x[ind];
	}
	
	public void setVals(float[] f){
		_f = f;
	}
	
}
