/****************************************************************************
Copyright 2016, Colorado School of Mines and others.
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
package edu.mines.jtk.mosaic;

import java.awt.Point;

import javax.swing.JLabel;

import edu.mines.jtk.awt.ModeManager;
/**
 * A mode for .... When this mode is active, ...
 * This mode is not exclusive.
 * @author Eric Addison
 * @version 2016.01.29
 */
public class GuidedValueMessageMode extends ValueMessageMode{

	private static final long serialVersionUID = 1L;
	private float[] _f;
	
	public GuidedValueMessageMode(ModeManager manager) {
		super(manager);
		_f = null;
	}

	public GuidedValueMessageMode(ModeManager manager, float[] f) {
		super(manager);
		_f = f;
	}	
	
	public GuidedValueMessageMode(ModeManager manager, JLabel label) {
		super(manager,label);
		_f = null;
	}
	
	public GuidedValueMessageMode(ModeManager manager, JLabel label, float[] f) {
		super(manager,label);
		_f = f;
	}
	
	public GuidedValueMessageMode(ModeManager manager, JLabel label, String s1, String s2, String s3) {
		super(manager,label,s1,s2,s3);
		_f = null;
	}
	
	public GuidedValueMessageMode(ModeManager manager, JLabel label, String s1, String s2) {
		super(manager,label,s1,s2);
		_f = null;
	}
	
	public GuidedValueMessageMode(ModeManager manager, JLabel label, String s1, String s2, String s3, float[] f) {
		super(manager,label,s1,s2,s3);
		_f = f;
	}
	
	public GuidedValueMessageMode(ModeManager manager, JLabel label, String s1, String s2, float[] f) {
		super(manager,label,s1,s2);
		_f = f;
	}
	
	
	public String[] updateValues(Point p){
		double ux = _tile.getTranscaler().x((int)p.getX());
		double vx = _tile.getHorizontalProjector().v(ux);
		
		if(vx < _tile.getHorizontalProjector().v0())
			vx = _tile.getHorizontalProjector().v0();
		if(vx > _tile.getHorizontalProjector().v1())
			vx = _tile.getHorizontalProjector().v1();

		double vy = getValueAtMouseLocation(_f, ux);
		
		return new String[] {_df.format(vx), _df.format(vy)};		
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
