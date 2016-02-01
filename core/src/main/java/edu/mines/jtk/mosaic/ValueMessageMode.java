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

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;

import javax.swing.JLabel;

import edu.mines.jtk.awt.Mode;
import edu.mines.jtk.awt.ModeManager;
import edu.mines.jtk.mosaic.Tile;


/**
 * A mode for .... When this mode is active, ...
 * This mode is not exclusive.
 * @author Eric Addison
 * @version 2016.01.29
 */
// Mode to display the value of an array f[] based on the current mouse X location
public class ValueMessageMode extends Mode{

	private static final long serialVersionUID = 1L;
	protected DecimalFormat _df;
	protected Tile _tile;
	private JLabel _messageLabel;
	private String _pre = "(";
	private String _mid = " , ";
	private String _post = ")";

	public ValueMessageMode(ModeManager manager, JLabel label, String s1, String s2, String s3) {
		super(manager);
		_messageLabel = label;
		setup(s1,s2,s3);
	}	
	
	public ValueMessageMode(ModeManager manager, JLabel label, String s1, String s2) {
		super(manager);
		_messageLabel = label;
		setup(s1,s2,"");
	}
	
	public ValueMessageMode(ModeManager manager, JLabel label) {
		super(manager);
		_messageLabel = label;
		setup("("," , ",")");
	}

	public ValueMessageMode(ModeManager manager) {
		super(manager);
		setup("("," , ",")");
	}	
	
	public ValueMessageMode(ModeManager manager, String s1, String s2, String s3) {
		super(manager);
		setup(s1,s2,s3);
	}
	
	public ValueMessageMode(ModeManager manager, String s1, String s2) {
		super(manager);
		setup(s1,s2,"");
	}
	
	
	private void setup(String pre, String mid, String post){
		setName("ValueMessage");
		setShortDescription("Display mouse location values in message bar");
		_pre = pre;
		_mid = mid;
		_post = post;
		// number format
		_df = new DecimalFormat();
		_df.setMaximumFractionDigits(3);
		_df.setGroupingUsed(false);
	}
	
	protected void setActive(Component component, boolean active) {
		if((component instanceof Tile)){
			if(active){
				_tile = (Tile)component;
				component.addMouseMotionListener(_ml);
			} else {
				_tile = null;
				component.removeMouseMotionListener(_ml);
			}
		}
	}

	public void setPreString(String pre){
		_pre = pre;
	}
	
	public void setMidString(String mid){
		_mid = mid;
	}
	
	public void setPostString(String post){
		_post = post;
	}
	
	
	// this mode is not exclusive
	public boolean isExclusive(){
		return false;
	}
	
	public void update(Point p){
		if(isActive()){
			String[] s = updateValues(p);
			updateMessage(s);
		}
	}
	
	public void updateMessage(String[] s){
		if(s[0] != null && s[1] != null && _messageLabel != null)
			_messageLabel.setText(_pre + s[0] + _mid + s[1] + _post);
	}
	
	public String[] updateValues(Point p){
		double ux = _tile.getTranscaler().x((int)p.getX());
		double vx = _tile.getHorizontalProjector().v(ux);
		
		double v0 = _tile.getHorizontalProjector().v0();
		double v1 = _tile.getHorizontalProjector().v1();
		double vmin = Math.min(v0, v1);
		double vmax = Math.max(v0, v1);
		vx = (vx < vmin) ? vmin : vx;
		vx = (vx > vmax) ? vmax : vx;
		
		double uy = _tile.getTranscaler().y((int)p.getY());
		double vy = _tile.getVerticalProjector().v(uy);
		
		v0 = _tile.getVerticalProjector().v0();
		v1 = _tile.getVerticalProjector().v1();
		vmin = Math.min(v0, v1);
		vmax = Math.max(v0, v1);
		vy = (vy < vmin) ? vmin : vy;
		vy = (vy > vmax) ? vmax : vy;
		
		return new String[] {_df.format(vx), _df.format(vy)};
	}
	
	
	public void setJLabel(JLabel label){
		_messageLabel = label;
	}
	
	private MouseMotionListener _ml = new MouseAdapter(){
		@Override
		public void mouseMoved(MouseEvent e){
			update(e.getPoint());
		}
	};

	
	
}
