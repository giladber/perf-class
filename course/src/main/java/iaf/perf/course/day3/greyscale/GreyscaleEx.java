package iaf.perf.course.day3.greyscale;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GreyscaleEx {
	
	public static final String RGB_LOCATION = "C:\\perf\\rgb.jpg";

	public static void main(String... args) {
		if (args.length <= 0) {
			System.out.println("Must supply full class name (including package) as argument!");
			return;
		}

		BufferedImage rgbImage;
		try {
			rgbImage = ImageIO.read(new File(RGB_LOCATION));
		}  catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("Failed to load image from " + RGB_LOCATION);
			return;
		}
		
		BufferedImage greyscale;
		final String converterClass = args[0];
		try {
			Class<?> clz = Class.forName(converterClass);
			Object o = clz.newInstance();
			GreyscaleConverter converter = (GreyscaleConverter) o;
			greyscale = converter.convert(rgbImage);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Class could not be loaded: " + converterClass);
			return;
		} catch (IllegalAccessException | InstantiationException iae) {
			iae.printStackTrace();
			System.out.println("Could not create new instance of class " + converterClass);
			return;
		} catch (ClassCastException cce) {
			cce.printStackTrace();
			System.out.println("Provided class is not an instance of GreyscaleConverter: " + converterClass);
			return;
		}
		
		showImages(rgbImage, greyscale);
		
	}

	public static void showImages(BufferedImage rgbImage, BufferedImage greyscale) 
	{
		JFrame frame = new JFrame();
		ConverterUIPanel panel = new ConverterUIPanel(rgbImage, greyscale);
		JScrollPane scrollPane = new JScrollPane(panel);
		Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
		Point centerPoint = new Point(resolution.width/2 - panel.getPreferredSize().width / 2,
				resolution.height/2 - panel.getPreferredSize().height / 2);
		frame.setLocation(centerPoint);
		frame.setPreferredSize(panel.getPreferredSize());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(scrollPane);
		frame.setVisible(true);
		frame.pack();
		frame.revalidate();
		frame.repaint();
	}
	
	@SuppressWarnings("serial")
	private static final class ConverterUIPanel extends JPanel
	{
		private final Dimension size;
		private final BufferedImage rgb;
		private final BufferedImage greyscale;
		
		public ConverterUIPanel(BufferedImage rgb, BufferedImage greyscale)
		{
			this.size = new Dimension(rgb.getWidth() + greyscale.getWidth() + 100,
					rgb.getHeight() + 88);
			this.rgb = rgb;
			this.greyscale = greyscale;
			this.setPreferredSize(size);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(rgb, 25, 25, null);
			g.drawImage(greyscale, rgb.getWidth() + 50, 25, null);
		}
	}
	
	public interface GreyscaleConverter {
		/**
		 * Convert an RGB image to greyscale.
		 * Use the following formula, where R|G|B[i,j] are the 
		 * R/G/B components of the RGB pixel at coordinates i,j,
		 * and grey[i,j] consists of the R,G,B components of the greyscale image
		 * at coordinates i,j:
		 * 
		 * a = R[i,j] * 0.21
		 * b = G[i,j] * 0.72
		 * c = B[i,j] * 0.07;
		 * sum = a + b + c
		 * grey[i,j] = (sum, sum, sum)
		 * 
		 * You can get an int value representing the RGB of the (i, j)-th pixel by using
		 * BufferedImage.getRGB(i, j). This int can be converted to a java.awt.Color
		 * object by using the Color(int) constructor. You can get ints representing
		 * the red, green, blue levels of the color by using Color.getRed/Green/Blue().
		 * @param img Image to convert
		 * @return
		 */
		public BufferedImage convert(BufferedImage img);
	}
	
}
