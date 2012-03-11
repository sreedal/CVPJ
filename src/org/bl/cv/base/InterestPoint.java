package org.bl.cv.base;

import java.awt.Graphics2D;

public class InterestPoint {

	float x,y;
	float scale,orientation;
	
	public InterestPoint(int x2, int y2, float orientation2) {
		x=x2;
		y=y2;
		orientation=orientation2;
	}
	
	public String toString(){
		return "X:"+x+" Y:"+y+" O:"+orientation;
	}

	public void draw(Graphics2D g2d) {
		g2d.drawRect((int)x-(int)scale*10,(int) y-(int)scale*10, (int)scale*20, (int) scale*20);
	}

}
