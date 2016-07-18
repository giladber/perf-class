package iaf.perf.course.day3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import iaf.perf.course.day3.greyscale.EfficientGreyscaleSolution;
import iaf.perf.course.day3.greyscale.GreyscaleEx;
import iaf.perf.course.day3.greyscale.GreyscaleSolution;
import iaf.perf.course.day3.greyscale.ParallelGreyscaleSolution;
import iaf.perf.course.day3.greyscale.GreyscaleEx.GreyscaleConverter;

public class GreyscaleBenchmark {

	private final GreyscaleConverter serial = new GreyscaleSolution();
	private final GreyscaleConverter efficient = new EfficientGreyscaleSolution();
	private final GreyscaleConverter parallel = new ParallelGreyscaleSolution();
	
	public volatile BufferedImage intermediateImage; //prevent DCE
	
	private static final BufferedImage rgb;
	
	private static final long WARMUP_ITER = (long) 1E2;
	private static final long BENCHMARK_ITER = (long) 1E3;
	
	static {
		try {
			rgb = ImageIO.read(new File(GreyscaleEx.RGB_LOCATION));
		}  catch (IOException ioe) {
			System.out.println("Failed to load image from " + GreyscaleEx.RGB_LOCATION);
			throw new RuntimeException(ioe);
		}
	}
	
	public static void main(String... args) {
		new GreyscaleBenchmark().benchmark();
	}
	
	public void benchmark() {
		warmup();
		System.gc();
		measure();
	}
	
	private void measure() {
		long serialStart = System.nanoTime();
		for (int i = 0; i < BENCHMARK_ITER; i++) {
			serial.convert(rgb);
		}
		long serialEnd = System.nanoTime();
		
		System.gc();
		long effStart = System.nanoTime();
		for (int i = 0; i < BENCHMARK_ITER; i++) {
			efficient.convert(rgb);
		}
		long effEnd = System.nanoTime();
		
		System.gc();
		long parStart = System.nanoTime();
		for (int i = 0; i < BENCHMARK_ITER; i++) {
			parallel.convert(rgb);
		}
		long parEnd = System.nanoTime();
		
		System.out.println("Serial: " + (serialEnd - serialStart)/1E6 + "ms");
		System.out.println("Efficient: " + (effEnd - effStart)/1E6 + "ms");
		System.out.println("Parallel: " + (parEnd - parStart)/1E6 + "ms");
	}

	private void warmup() {
		long start = System.nanoTime();
		for (int i = 0; i < WARMUP_ITER; i++) {
			serial.convert(rgb);
			efficient.convert(rgb);
			parallel.convert(rgb);
		}
		long end = System.nanoTime();
		System.out.println("Warmup time: " + (end-start)/1E6 + "ms");
	}
	
}
