package org.bl.cv.pj.features.detector;

import org.bl.cv.base.ImageBase;

public class SingleScaleHarrisCornerDetector extends AutocorrelationCornerDetector {

	
	float sensitivity;
	public static boolean debug=true;
	
	protected SingleScaleHarrisCornerDetector(ImageBase image) {
		super(image);
		sensitivity=0.5f;
		detectPoints(image);
	}
	
	protected SingleScaleHarrisCornerDetector(ImageBase image, float sens) {
		super(image);
		sensitivity=sens;
		detectPoints(image);
	}
	
	protected SingleScaleHarrisCornerDetector(ImageBase image, float sens, float thresh) {
		super(image,thresh);
		sensitivity=sens;
		detectPoints(image);
	}

	@Override
	protected float getACMScore(float[] ACM) {
		if(debug) 
			if(ACM[0]+ACM[1]+ACM[2]+ACM[3]>0 && ACM[0]*ACM[3]-ACM[1]*ACM[2]-sensitivity*(ACM[0]+ACM[3])*(ACM[0]+ACM[3])>0)
				System.out.println(ACM[0]*ACM[3]-ACM[1]*ACM[2]-sensitivity*(ACM[0]+ACM[3])*(ACM[0]+ACM[3]));
		return ACM[0]*ACM[3]-ACM[1]*ACM[2]-sensitivity*(ACM[0]+ACM[3])*(ACM[0]+ACM[3]);
	}
	
	public static void main(String args[]){
		ImageBase image=new ImageBase("/home/sreedal/Desktop/DumpDocs/RASL/RASL_Code/data/LFW/Colin_Powell/Colin_Powell_0001.jpg");
		SingleScaleHarrisCornerDetector hcd=new SingleScaleHarrisCornerDetector(image,(float) 0.16,(float)1);
		if(SingleScaleHarrisCornerDetector.debug==true)
			System.out.println("Writing them..");
		hcd.print(System.out);
		if(SingleScaleHarrisCornerDetector.debug==true)
			System.out.println("Displaying them..");
		hcd.displayPoints(image);
	}
}
