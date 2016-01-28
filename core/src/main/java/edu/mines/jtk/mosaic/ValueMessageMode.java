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


// Mode to display the value of an array f[] based on the current mouse X location
public class ValueMessageMode extends Mode{

	private static final long serialVersionUID = 1L;
	protected DecimalFormat _df;
	protected Tile _tile;
	private JLabel _messageLabel;
	private String _xlabel = "x";
	private String _ylabel = "y";

	public ValueMessageMode(ModeManager manager, JLabel label, String x, String y) {
		super(manager);
		_messageLabel = label;
		setup(x,y);
	}	
	
	public ValueMessageMode(ModeManager manager, JLabel label) {
		super(manager);
		_messageLabel = label;
		setup("x","y");
	}

	public ValueMessageMode(ModeManager manager) {
		super(manager);
		setup("x","y");
	}	
	
	public ValueMessageMode(ModeManager manager, String x, String y) {
		super(manager);
		setup(x,y);
	}
	
	
	private void setup(String x, String y){
		setName("ValueMessage");
		setShortDescription("Display mouse location values in message bar");
		_xlabel = x;
		_ylabel = y;
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

	public void setXlabel(String x){
		_xlabel = x;
	}
	
	public void setYlabel(String y){
		_ylabel = y;
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
			_messageLabel.setText(_xlabel + ": " + s[0] + " | " + _ylabel + ": " + s[1]);
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
