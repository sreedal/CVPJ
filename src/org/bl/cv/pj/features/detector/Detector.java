package org.bl.cv.pj.features.detector;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.bl.cv.base.ImageBase;
import org.bl.cv.base.InterestPoint;

public abstract class Detector {
	ArrayList<InterestPoint> interestPoints;
	
	protected Detector(ImageBase image){
		interestPoints=new ArrayList<InterestPoint>();
	}
	
	public ArrayList<InterestPoint> getInterestPoints(){
		return interestPoints;
	}

	protected abstract void detectPoints(ImageBase image);
	
	protected abstract void filterPoints();
	
	public void displayPoints(ImageBase image){
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setColor(Color.RED);
        for(InterestPoint i:interestPoints){
        	i.draw(g2d);
        }
        image.saveOutputs();
//        Frame display=new Frame("Interest Points Detected");
//        Canvas C=new Canvas();
//        C.setSize(image.getWidth(),image.getHeight());
//        C.setBackground(Color.white);
//        C. .update(g2d);
//        display.add(C);
//        display.setLayout(new FlowLayout());
//        display.setSize(image.getWidth(),image.getHeight());
//        display.setVisible(true);
//        C.requestFocus();
	}
	
	public void print(PrintWriter p){
		for(InterestPoint i:interestPoints){
			p.println(i.toString());
		}
	}
	
	protected void print(PrintStream p) {
//		for(InterestPoint i:interestPoints){
//			p.println(i.toString());
//		}
		p.println("In all :"+interestPoints.size()+" interest points");
	}
	
	public void saveOutputs(String location){
		try {
			PrintWriter p=new PrintWriter(new FileOutputStream(location+".all.detections"));
			print(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
