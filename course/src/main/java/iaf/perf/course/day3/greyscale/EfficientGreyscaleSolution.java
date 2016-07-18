package iaf.perf.course.day3.greyscale;

import java.awt.image.BufferedImage;

import iaf.perf.course.day3.greyscale.GreyscaleEx.GreyscaleConverter;

public class EfficientGreyscaleSolution implements GreyscaleConverter 
{

	@Override
	public BufferedImage convert(BufferedImage img) {
		final int[] data = imageToIntArray(img);
		for (int i = 0; i < data.length; i++) {
			data[i] = GreyscaleSolution.rgbToGrey(data[i]);
		}
		return intArrayToImage(data, img.getWidth(), img.getHeight());
	}
	
	public static int[] imageToIntArray(BufferedImage img) {
		int[] result = new int[img.getWidth() * img.getHeight()];
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				result[j * img.getWidth() + i] = img.getRGB(i, j);
			}
		}
		
		return result;
	}
	
	public static BufferedImage intArrayToImage(int[] data, int width, int height) 
	{
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int value = data[j * width + i];
				result.setRGB(i, j, value);
			}
		}
		return result;
	}

}
