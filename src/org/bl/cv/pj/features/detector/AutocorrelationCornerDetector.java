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
		derivativeWindowSize=3;
		gaussianSigma=1;
		gaussianWindowSize=(int) Math.floor(gaussianSigma*8);
		threshold=0;
		BuildGaussianWindow();
		CreateIntegralImage(image);
		CalculatePartialDerivatives(image);
	}
	
	protected AutocorrelationCornerDetector(ImageBase image,float sigma) {
		super(image);
		Ix=new float[image.getWidth()][image.getHeight()];
		Ixx=new float[image.getWidth()][image.getHeight()];
		Iyy=new float[image.getWidth()][image.getHeight()];
		Ixy=new float[image.getWidth()][image.getHeight()];
		Iy=new float[image.getWidth()][image.getHeight()];
		IntegralImage=new float[image.getWidth()][image.getHeight()];
		width=image.getWidth();
		height=image.getHeight();
		derivativeWindowSize=3;
		gaussianSigma=sigma;
		gaussianWindowSize=(int) Math.floor(gaussianSigma*8);
		threshold=0f;
		BuildGaussianWindow();
		image.convolveWithGaussian(gaussianSigma);
		CreateIntegralImage(image);
		CalculatePartialDerivatives(image);
	}
	
	@Override
	protected void detectPoints(ImageBase image) {
		float thresh=1.5f;
		float CornerScore[][]=new float[width][height];
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++){
				CornerScore[i][j]=getACMScore(getAutoCorrelationMatrix(i,j));
			}
		for(int i=1;i<width-1;i++)
			for(int j=1;j<height-1;j++){
				if(CornerScore[i][j]>thresh*CornerScore[i-1][j] &&
						CornerScore[i][j]>thresh*CornerScore[i-1][j-1] &&
						CornerScore[i][j]>thresh*CornerScore[i-1][j+1] &&
						CornerScore[i][j]>thresh*CornerScore[i-1][j-2] &&
						CornerScore[i][j]>thresh*CornerScore[i-1][j+2] &&
						CornerScore[i][j]>thresh*CornerScore[i][j-1] &&
						CornerScore[i][j]>thresh*CornerScore[i][j+1] &&
						CornerScore[i][j]>thresh*CornerScore[i+1][j-1] &&
						CornerScore[i][j]>thresh*CornerScore[i+1][j] &&
						CornerScore[i][j]>thresh*CornerScore[i+1][j+1] &&
						CornerScore[i][j]>thresh*CornerScore[i+1][j-2] &&
						CornerScore[i][j]>thresh*CornerScore[i+1][j+2] &&
						CornerScore[i][j]>thresh*CornerScore[i-2][j] &&
						CornerScore[i][j]>thresh*CornerScore[i-2][j-1] &&
						CornerScore[i][j]>thresh*CornerScore[i-2][j+1] &&
						CornerScore[i][j]>thresh*CornerScore[i-2][j-2] &&
						CornerScore[i][j]>thresh*CornerScore[i-2][j+2] &&
						CornerScore[i][j]>thresh*CornerScore[i][j-2] &&
						CornerScore[i][j]>thresh*CornerScore[i][j+2] &&
						CornerScore[i][j]>thresh*CornerScore[i+2][j-1] &&
						CornerScore[i][j]>thresh*CornerScore[i+2][j] &&
						CornerScore[i][j]>thresh*CornerScore[i+2][j+1] &&
						CornerScore[i][j]>thresh*CornerScore[i+2][j-2] &&
						CornerScore[i][j]>thresh*CornerScore[i+2][j+2] &&
						CornerScore[i][j]>threshold)
					interestPoints.add(new InterestPoint(i,j,0,2));
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
				gaussianWindow[i][j]=Math.exp(-((i-mp)*(i-mp)+(j-mp)*(j-mp))/(2*gaussianSigma*gaussianSigma))/(2*Math.PI*gaussianSigma*gaussianSigma);
	}
	
	protected float[] getAutoCorrelationMatrix(int x, int y){
		float ACM[]=new float[4];
		int mp=gaussianWindowSize/2;
		for(int i=0;i<4;i++)
			ACM[i]=0;
		if(x>=mp && y>=mp && x<(width-mp) && y<(height-mp) ){
			for(int i=x-mp;i<x+mp;i++){
				for(int j=y-mp;j<y+mp;j++){
					ACM[0]=(float) (ACM[0]+gaussianWindow[i-x+mp][j-y+mp]*Ixx[i][j]);
					ACM[1]=(float) (ACM[1]+gaussianWindow[i-x+mp][j-y+mp]*Ixy[i][j]);
					ACM[2]=ACM[1];
					ACM[3]=(float) (ACM[3]+gaussianWindow[i-x+mp][j-y+mp]*Iyy[i][j]);
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
		for(int i=0;i<width;i++){
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
		System.out.println(IntegralImage[width-1][height-1]);
	}
	
}
