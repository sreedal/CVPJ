package org.bl.cv.base;

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
}
