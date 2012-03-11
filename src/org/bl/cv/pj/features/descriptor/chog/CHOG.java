package org.bl.cv.pj.features.descriptor.chog;

import java.util.ArrayList;

import org.bl.cv.pj.features.descriptor.DescriptorFactory;
import org.bl.cv.pj.features.detector.hacd.HessianAffineCornerDetector;

// Implements the CHOG Descriptor as proposed in "Compressed Histogram of Gradients: A Low-Bitrate Descriptor : IJCV 2011"
public class CHOG extends DescriptorFactory{
	
	CHOG(String inputImage){
		super(inputImage);
		detector=new HessianAffineCornerDetector(image);
		detector.displayPoints(image);
		dimensions=63;
		descriptors=new ArrayList<CHOGDescriptor>(dimensions);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) { // Used only for internal testing - not to be used in the library routines.
		CHOG test=new CHOG("Data/Images/Objects/Caltech101/PNGImages/wrench/image_0001.png");
		test.saveOutputs("Outputs/Images/Objects/Caltech101/PNGImages/wrench/image_0001"); 
	}

	@Override
	public void buildAllDescriptors() {
		// TODO Auto-generated method stub
		
	}

}
