package org.bl.cv.pj.features.descriptor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.bl.cv.base.ImageBase;
import org.bl.cv.pj.features.detector.Detector;

public abstract class DescriptorFactory {
	protected ImageBase image;
	protected Detector detector;
	protected ArrayList<? extends Descriptor> descriptors;
	protected int dimensions;
	
	protected DescriptorFactory(String fileName){
		image=new ImageBase(fileName);
		detector=null;
		descriptors=null;
	}
	
	public abstract void buildAllDescriptors();
	
	protected void setDetector(Detector d){
		if(d!=null)
			detector=d;
		else
			throw new NullPointerException();
	}
	
	public ArrayList<? extends Descriptor> getDescriptors(){
		return descriptors;
	} 
	
	public Detector getDetector(){
		return detector;
	}
	
	public void print(PrintWriter p){
		if(p==null)
			throw new NullPointerException();
		for(Descriptor i:descriptors){
			i.print(p);
		}
	}
	
	public void saveOutputs(String location){
		try {
			PrintWriter p=new PrintWriter(new FileOutputStream(location+".all.descriptors"));
			print(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
