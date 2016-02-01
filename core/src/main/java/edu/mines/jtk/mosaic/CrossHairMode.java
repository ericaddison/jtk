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

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import edu.mines.jtk.awt.Mode;
import edu.mines.jtk.awt.ModeManager;
import edu.mines.jtk.mosaic.GridView.Horizontal;
import edu.mines.jtk.mosaic.GridView.Vertical;
import edu.mines.jtk.mosaic.Tile;
/**
 * A mode for .... When this mode is active, ...
 * This mode is not exclusive.
 * @author Eric Addison
 * @version 2016.01.29
 */
public class CrossHairMode extends Mode{

	private static final long serialVersionUID = 1L;
	protected CrossHairView xhairView;
	protected Tile _tile;
	
	public CrossHairMode(ModeManager manager) {
		super(manager);
		setName("CrossHair");
		setShortDescription("Display crosshairs at mouse location");
		xhairView = new CrossHairView(Horizontal.NONE,Vertical.NONE,0,0);
		xhairView.setColor(Color.RED);
	}

	protected void setActive(Component component, boolean active) {
		if((component instanceof Tile)){
			if(active){
				_tile = (Tile)component;
				component.addMouseMotionListener(_ml);
				component.addMouseListener(_ml);
				((Tile)component).addTiledView(xhairView);
			} else {
				_tile = null;
				component.removeMouseMotionListener(_ml);
				component.removeMouseListener(_ml);
				((Tile)component).removeTiledView(xhairView);
			}
		}
	}
	
	public GridView getGridView(){
	  return xhairView;
	}
	
	// this mode is not exclusive
	public boolean isExclusive(){
		return false;
	}
	
	public void update(Point p){
		if(isActive()){
			double ux = _tile.getTranscaler().x((int)p.getX());
			double vx = _tile.getHorizontalProjector().v(ux);
			double uy = _tile.getTranscaler().y((int)p.getY());
			double vy = _tile.getVerticalProjector().v(uy);
			xhairView.set(vx,vy);
		}
	}
	
	
	private MouseAdapter _ml = new MouseAdapter(){
		@Override
		public void mouseMoved(MouseEvent e){
			update(e.getPoint());
		}
		public void mouseExited(MouseEvent e){
			xhairView.setHorizontal(Horizontal.NONE);
			xhairView.setVertical(Vertical.NONE);
		}
	};

}
