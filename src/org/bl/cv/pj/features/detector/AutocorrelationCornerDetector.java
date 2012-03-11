package org.bl.cv.pj.features.detector;

import java.awt.Color;

import org.bl.cv.base.ImageBase;
import org.bl.cv.base.InterestPoint;

public abstract class AutocorrelationCornerDetector extends Detector{

	float Ix[][],Iy[][];
	float Ixx[][],Iyy[][],Ixy[][];
	float IntegralImage[][];
	int width;
	int height;
	int derivativeWindowSize;
	int gaussianWindowSize;
	double gaussianSigma;
	double gaussianWindow[][];
	private float threshold;
	
	protected AutocorrelationCornerDetector(ImageBase image) {
		super(image);
		Ix=new float[image.getWidth()][image.getHeight()];
		Ixx=new float[image.getWidth()][image.getHeight()];
		Iyy=new float[image.getWidth()][image.getHeight()];
		Ixy=new float[image.getWidth()][image.getHeight()];
		Iy=new float[image.getWidth()][image.getHeight()];
		IntegralImage=new float[image.getWidth()][image.getHeight()];
		width=image.getWidth();
		height=image.getHeight();
		derivativeWindowSize=10;
		gaussianWindowSize=10;
		gaussianSigma=3;
		threshold=30;
		BuildGaussianWindow();
		CreateIntegralImage(image);
		CalculatePartialDerivatives(image);
	}
	
	protected AutocorrelationCornerDetector(ImageBase image,float t) {
		super(image);
		Ix=new float[image.getWidth()][image.getHeight()];
		Ixx=new float[image.getWidth()][image.getHeight()];
		Iyy=new float[image.getWidth()][image.getHeight()];
		Ixy=new float[image.getWidth()][image.getHeight()];
		Iy=new float[image.getWidth()][image.getHeight()];
		IntegralImage=new float[image.getWidth()][image.getHeight()];
		width=image.getWidth();
		height=image.getHeight();
		derivativeWindowSize=10;
		gaussianWindowSize=10;
		gaussianSigma=3;
		threshold=t;
		BuildGaussianWindow();
		CreateIntegralImage(image);
		CalculatePartialDerivatives(image);
	}
	
	@Override
	protected void detectPoints(ImageBase image) {
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++){
				float v=getACMScore(getAutoCorrelationMatrix(i,j));
				if(v>threshold)
					interestPoints.add(new InterestPoint(i,j)); //TODO Add Scale and Orientation Mapping
			}
	}
	
	@Override
	protected void filterPoints() {
		// TODO Auto-generated method stub
		
	}
	
	protected abstract float getACMScore(float ACM[]);
	
	protected void BuildGaussianWindow(){
		gaussianWindow=new double[gaussianWindowSize][gaussianWindowSize];
		int mp=gaussianWindowSize/2;
		for(int i=0;i<gaussianWindowSize;i++)
			for(int j=0;j<gaussianWindowSize;j++)
				gaussianWindow[i][j]=Math.exp(((i-mp)*(i-mp)+(j-mp)*(j-mp))/(2*gaussianSigma*gaussianSigma))/(Math.sqrt(2*Math.PI)*gaussianSigma);
	}
	
	protected float[] getAutoCorrelationMatrix(int x, int y){
		float ACM[]=new float[4];
		int mp=gaussianWindowSize/2;
		for(int i=0;i<4;i++)
			ACM[i]=0;
		if(x>=mp && y>=mp){
			for(int i=x-mp;i<x+mp;i++){
				for(int j=x-mp;j<y+mp;j++){
					ACM[0]=(float) (ACM[0]+gaussianWindow[i+mp][j+mp]*Ixx[i][j]);
					ACM[1]=(float) (ACM[1]+gaussianWindow[i+mp][j+mp]*Ixy[i][j]);
					ACM[2]=(float) (ACM[2]+gaussianWindow[i+mp][j+mp]*Ixy[i][j]);
					ACM[3]=(float) (ACM[3]+gaussianWindow[i+mp][j+mp]*Iyy[i][j]);
				}
			}
		}
		return ACM;
	}

	private void CalculatePartialDerivatives(ImageBase image) {
		for(int i=derivativeWindowSize/2;i<width-derivativeWindowSize/2;i++){
			for(int j=derivativeWindowSize/2;j<height-derivativeWindowSize/2;j++){
				Ix[i][j]=IntegralImage[(int)Math.ceil(i+derivativeWindowSize/2)][(int)Math.ceil(j+derivativeWindowSize/2)]-IntegralImage[i][(int)Math.floor(j-derivativeWindowSize/2)]-(IntegralImage[i][(int)Math.ceil(j+derivativeWindowSize/2)]-IntegralImage[(int)Math.floor(i-derivativeWindowSize/2)][(int)Math.floor(j-derivativeWindowSize/2)]);
				Iy[i][j]=IntegralImage[(int)Math.ceil(i+derivativeWindowSize/2)][(int)Math.ceil(j+derivativeWindowSize/2)]-IntegralImage[(int)Math.floor(i-derivativeWindowSize/2)][j]-(IntegralImage[(int)Math.ceil(i+derivativeWindowSize/2)][j]-IntegralImage[(int)Math.floor(i-derivativeWindowSize/2)][(int)Math.floor(j-derivativeWindowSize/2)]);
				Ixx[i][j]=Ix[i][j]*Ix[i][j];
				Iyy[i][j]=Iy[i][j]*Iy[i][j];
				Ixy[i][j]=Ix[i][j]*Iy[i][j];
			}
		}
		
	}

	private void CreateIntegralImage(ImageBase image) {
		for(int i=1;i<width;i++){
			for(int j=0;j<height;j++){
				Color c=new Color(image.getPixel(i, j));
				int pix=(int) ((c.getRed()+c.getBlue()+c.getGreen())/3.0);
				if(i==0)
					if(j>0)
						IntegralImage[i][j]=IntegralImage[i][j-1]+pix;
					else
						IntegralImage[i][j]=pix;
				else
					if(j>0)
						IntegralImage[i][j]=IntegralImage[i-1][j]+IntegralImage[i][j-1]-IntegralImage[i-1][j-1]+pix;
					else
						IntegralImage[i][j]=IntegralImage[i-1][j]+pix;
			}
		}
	}
	
	
}
