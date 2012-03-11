package org.bl.cv.pj.features.descriptor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.IndexOutOfBoundsException;

import org.bl.cv.base.ImageBase;
import org.bl.cv.base.InterestPoint;

public abstract class Descriptor<T> {
	protected T features[];
	protected int maxDimension; 

	@SuppressWarnings("unchecked")
	public Descriptor(int dimension){
		features=(T[])new Object[dimension];
		maxDimension=dimension;
	}
	
	public void print(PrintWriter p){
		for(T i:features){
			p.print(i.toString()+" ");
		}
		p.println();
	}
	
	public void saveOutputs(String location){
		try {
			PrintWriter p=new PrintWriter(new FileOutputStream(location+".descriptor"));
			print(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setDescriptor(T[] values){
		features=values;
	}
	
	public void setDescriptor(T value, int location){
		if(location>=maxDimension || location<0)
			throw new IndexOutOfBoundsException();
		else
			features[location]=value;
	}
	
	public T getDescriptor(int location){
		if(location>=maxDimension || location<0)
			throw new IndexOutOfBoundsException();
		else
			return features[location];
	}
	
	public T[] getDescriptor(){
		return features;
	}
	
	public abstract void buildDescriptor(ImageBase I, InterestPoint p);
	
	public abstract void normalizeDescriptor();
	
}
