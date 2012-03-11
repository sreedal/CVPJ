package org.bl.cv.pj.features.descriptor.chog;

import org.bl.cv.base.ImageBase;
import org.bl.cv.base.InterestPoint;
import org.bl.cv.pj.features.descriptor.Descriptor;

public class CHOGDescriptor extends Descriptor<Float>{

	static int dimension=63;
	
	public CHOGDescriptor() {
		super(dimension);
	}
	
	public void buildDescriptor(ImageBase I, InterestPoint p){
		//TODO Implement this function to calculate and store the desriptor corresponding to the location p in the image I
	}
	
	public void normalizeDescriptor(){
		//TODO Implement this function to normalize the stored descriptor
	}
}
