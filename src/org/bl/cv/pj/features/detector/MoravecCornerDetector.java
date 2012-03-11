package org.bl.cv.pj.features.detector;

import java.io.PrintStream;

import org.bl.cv.base.ImageBase;
import org.bl.cv.base.InterestPoint;

public class MoravecCornerDetector extends Detector{

	private static boolean debug = true;
	
	private int windowSize;
	private float threshold;
	
	protected MoravecCornerDetector(ImageBase image) {
		super(image);
		windowSize=5;
		threshold=10;
		detectPoints(image);
		filterPoints();
	}
	
	protected MoravecCornerDetector(ImageBase image,int winSize) {
		super(image);
		windowSize=winSize;
		threshold=50;
		detectPoints(image);
		filterPoints();
	}
	
	protected MoravecCornerDetector(ImageBase image,int winSize,float thresh) {
		super(image);
		windowSize=winSize;
		threshold=thresh;
		detectPoints(image);
		filterPoints();
	}

	@Override
	protected void detectPoints(ImageBase image) {
		if(debug==true)
			System.out.println("Detecting Points");
		int border=windowSize/2+1;
		for(int x=border+1;(x+border+1)<image.getWidth();x++)
			for(int y=border+1;(y+border+1)<image.getHeight();y++){
				float minDisplacement=Float.MAX_VALUE;
				float orientation=0;
				boolean set=false;
				for(int dirx=-1;dirx<2;dirx++)
					for(int diry=-1;diry<2;diry++){
						if(dirx==0 && diry==0)
							continue;
						float displacement=0;
						for(int dx=-(border);dx<(border+1);dx++)
							for(int dy=-(border);dy<(border+1);dy++){
								displacement+=Math.abs(image.getPixel(x+dx,y+dy)-image.getPixel(x+dirx+dx,y+diry+dy));
							}
						if(displacement<minDisplacement){
							minDisplacement=displacement;
							orientation=(float) Math.atan2(diry,dirx);
							set=true;
						}
					}
				if(minDisplacement>threshold && set){
//					System.out.println("Threshold:"+minDisplacement+"/"+threshold);
					interestPoints.add(new InterestPoint(x,y,orientation));
				}
			}
		if(debug==true)
			System.out.println("Completed..");
	}

	@Override
	protected void filterPoints() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String args[]){
		ImageBase image=new ImageBase("/home/sreedal/Desktop/DumpDocs/RASL/RASL_Code/data/LFW/Colin_Powell/Colin_Powell_0001.jpg");
		MoravecCornerDetector mvc=new MoravecCornerDetector(image,3,(float) 305);
		if(MoravecCornerDetector.debug==true)
			System.out.println("Writing them..");
		mvc.print(System.out);
		if(MoravecCornerDetector.debug==true)
			System.out.println("Displaying them..");
		mvc.displayPoints(image);
	}
}
