package org.bl.cv.base;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import com.sixlegs.png.PngImage;

public class ImageBase {
	private BufferedImage image;
	protected String location;
	public ImageBase(String fileName){
		// Read in the image file
		try {
			if(fileName.indexOf(".png")>0){
				PngImage p = new PngImage();
				image=p.read(new File(fileName));
			}else{
				image=ImageIO.read(new File(fileName));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		location=fileName;
	}
	
	public void saveOutputs(){
		try {
			ImageIO.write(image,"jpg",new File(location+"_output.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void saveOutputs(String location){
		try {
			ImageIO.write(image,"jpg",new File(location+".jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Graphics2D getGraphics() {
		return (Graphics2D) image.getGraphics();
	}

	public int getWidth() {
		return image.getWidth();
	}

	public int getHeight() {
		return image.getHeight();
	}

	public int getPixel(int i, int j) {
		if(i<image.getWidth() && j<image.getHeight() && i>=0 && j>=0){
			int rgb=image.getRGB(i,j);
			int red = (rgb >> 16) & 0x000000FF;
			int green = (rgb >>8 ) & 0x000000FF;
			int blue = (rgb) & 0x000000FF;
			return (int) ((red+green+blue)/3.0);
		}
		else
			return 0;
	}

	public void convolveWithGaussian(double gaussianSigma) {
		int gaussianWindowSize=(int) Math.floor(gaussianSigma*8);
		double[][] gaussianWindow=new double[gaussianWindowSize][gaussianWindowSize];
		int mp=gaussianWindowSize/2;
		double sum=0;
		for(int i=0;i<gaussianWindowSize;i++)
			for(int j=0;j<gaussianWindowSize;j++){
				gaussianWindow[i][j]=Math.exp(-((i-mp)*(i-mp)+(j-mp)*(j-mp))/(2*gaussianSigma*gaussianSigma))/(2*Math.PI*gaussianSigma*gaussianSigma);
				sum+=gaussianWindow[i][j];
			}
		System.out.println(sum);
		for(int i=mp;(i+mp)<image.getWidth();i++)
			for(int j=mp;(j+mp)<image.getHeight();j++){
				int Rsum=0,Gsum=0,Bsum=0;
				for(int di=-mp;di<mp;di++)
					for(int dj=-mp;dj<mp;dj++){
						Color c=new Color(image.getRGB(i+di, j+dj));
						Rsum+=gaussianWindow[di+mp][dj+mp]*c.getRed();
						Gsum+=gaussianWindow[di+mp][dj+mp]*c.getGreen();
						Bsum+=gaussianWindow[di+mp][dj+mp]*c.getBlue();
					}
//				System.out.println(Rsum+":"+Bsum+":"+Gsum);
				Color c=new Color(Rsum,Gsum,Bsum);
				image.setRGB(i, j, c.getRGB());
			}
		
	}
}
