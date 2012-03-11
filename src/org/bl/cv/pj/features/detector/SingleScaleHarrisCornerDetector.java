package org.bl.cv.pj.features.detector;

import org.bl.cv.base.ImageBase;

public class SingleScaleHarrisCornerDetector extends AutocorrelationCornerDetector {

	float sensitivity;
	public static boolean debug=true;
	
	protected SingleScaleHarrisCornerDetector(ImageBase image) {
		super(image);
		sensitivity=0.5f;
	}
	
	protected SingleScaleHarrisCornerDetector(ImageBase image, float sens) {
		super(image);
		sensitivity=sens;
	}
	
	protected SingleScaleHarrisCornerDetector(ImageBase image, float sens, float thresh) {
		super(image,thresh);
		sensitivity=sens;
	}

	@Override
	protected float getACMScore(float[] ACM) {
		return ACM[0]*ACM[3]-ACM[1]*ACM[2]-sensitivity*(ACM[0]+ACM[3])*(ACM[0]+ACM[3]);
	}
	
	public static void main(String args[]){
		ImageBase image=new ImageBase("/home/sreedal/Desktop/DumpDocs/RASL/RASL_Code/data/LFW/Colin_Powell/Colin_Powell_0001.jpg");
		SingleScaleHarrisCornerDetector hcd=new SingleScaleHarrisCornerDetector(image,(float) 0.5,0.2f);
		if(SingleScaleHarrisCornerDetector.debug==true)
			System.out.println("Writing them..");
		hcd.print(System.out);
		if(SingleScaleHarrisCornerDetector.debug==true)
			System.out.println("Displaying them..");
		hcd.displayPoints(image);
	}
}
