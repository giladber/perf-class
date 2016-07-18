package iaf.perf.course.day3.greyscale;

import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import iaf.perf.course.day3.greyscale.GreyscaleEx.GreyscaleConverter;

/**
 * Not thread safe! Remember: parallel != concurrent
 * @author Gilad Ber
 *
 */
public class ParallelGreyscaleSolution implements GreyscaleConverter 
{

	/* Number of threads to use for parallelization */
	private static final int PARALLELISM = Runtime.getRuntime().availableProcessors();
	
	/* Number of pixels in the image beyond which we parallelize the algorithm */
	private static final int PARALLELISM_THRESHOLD = 1 << 14;
	
	/* Recommended size (in ints) of a conversion task. Make sure it is a multiple
	 * of two times the cache line size (in bytes) 
	 * The reason the size needs to be a multiple of 2*cache line size, is so that 
	 * we avoid false sharing between tasks. 
	 */
	private static final int TASK_SIZE = 1 << 12; 
	
	@Override
	public BufferedImage convert(BufferedImage img) {
		final int pixels = img.getHeight() * img.getWidth();
		return ConversionType.of(PARALLELISM, pixels).newConverter().convert(img);
	}
	
	private static final class ConvertTask implements Runnable
	{
		private final int[] data;
		private final int lowerBound;
		private final int upperBound;
		private final CountDownLatch latch;
		
		public ConvertTask(int[] data, int lb, int ub, CountDownLatch latch) {
			this.data = data;
			this.lowerBound = lb;
			this.upperBound = ub;
			this.latch = latch;
		}
		
		@Override
		public void run() {
			int maxIndex = Math.min(data.length, upperBound);
			for (int i = lowerBound; i < maxIndex; i++) {
				data[i] = GreyscaleSolution.rgbToGrey(data[i]);
			}
			latch.countDown();
		}
	}
	
	
	
	private enum ConversionType {
		SERIAL {
			@Override
			public GreyscaleConverter newConverter() {
				return new GreyscaleSolution();
			}
		} ,
		PARALLEL {
			@Override
			public GreyscaleConverter newConverter() {
				return new ParallelConverter();
			}
		};
		
		private static ConversionType of(int parallelismLevel, int numPixels) 
		{
			if (parallelismLevel == 1 || numPixels < PARALLELISM_THRESHOLD) {
				return SERIAL;
			}
			else {
				return PARALLEL;
			}
		}
		
		public abstract GreyscaleConverter newConverter(); 
	}
	
	private static final class ParallelConverter implements GreyscaleConverter
	{
		private final ExecutorService pool = Executors.newFixedThreadPool(PARALLELISM);

		@Override
		public BufferedImage convert(BufferedImage img) {
			return convertParallel(img);
		}
		
		private BufferedImage convertParallel(BufferedImage rgb) 
		{
			int[] data = EfficientGreyscaleSolution.imageToIntArray(rgb);
			int length = data.length;
			int numTasks = (int) Math.ceil((double)length/(double)TASK_SIZE);
			CountDownLatch latch = new CountDownLatch(numTasks);
			
			submitTasks(data, length, numTasks, latch);
			awaitFinish(latch);
			
			return EfficientGreyscaleSolution.
					intArrayToImage(data, rgb.getWidth(), rgb.getHeight());
		}

		private void awaitFinish(CountDownLatch latch) {
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		private void submitTasks(int[] data, int length, int numTasks,
				CountDownLatch latch) 
		{
			for (int i = 0; i < numTasks; i++) {
				int lowerBound = i * TASK_SIZE;
				int upperBound = Math.min(lowerBound + TASK_SIZE, length);
				pool.submit(new ConvertTask(data, lowerBound, upperBound, latch));
			}
		}
		
	}

}
