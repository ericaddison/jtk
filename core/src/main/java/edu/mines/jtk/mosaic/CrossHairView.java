/****************************************************************************
Copyright 2005, Colorado School of Mines and others.
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


/**
 * Subclass of ArbitraryGridView to draw crosshairs at location p,
 * where p is a Point in data coordinates.
 * 
 * @Author Eric Addison
 * @author Dave Hale, Colorado School of Mines
 * @version 2016.1.22
 */
public class CrossHairView extends GridView {

	public CrossHairView(double x, double y){
		super();
		super.setHorizontal(Horizontal.USER);
		super.setVertical(Vertical.USER);
		super.setVLineLocations(new float[] {(float)x});
		super.setHLineLocations(new float[] {(float)y});
	}
	
	public CrossHairView(Horizontal horizontal, Vertical vertical, double x, double y){
		super();
		setHorizontal(horizontal);
		setVertical(vertical);
		if(horizontal != Horizontal.NONE)
			super.setHLineLocations(new float[] {(float)y});
		if(vertical != Vertical.NONE)
			super.setVLineLocations(new float[] {(float)x});
	}
	
	public void set(double x, double y){
		super.setHLineLocations(new float[] {(float)y});
		super.setVLineLocations(new float[] {(float)x});
	}
	
	
	// only allow modes NONE or USER
	public void setHorizontal(Horizontal horizontal){
		if(horizontal != Horizontal.NONE)
			super.setHorizontal(Horizontal.USER);
		else
			super.setHLineLocations(null);
	}
	
	public void setVertical(Vertical vertical){
		if(vertical != Vertical.NONE)
			super.setVertical(Vertical.USER);
		else
			super.setVLineLocations(null);
	}
	

}

