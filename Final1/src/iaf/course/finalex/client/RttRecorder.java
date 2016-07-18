package iaf.course.finalex.client;

import org.HdrHistogram.Histogram;
import org.HdrHistogram.HistogramLogWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RttRecorder implements Runnable, AutoCloseable
{
	private static final int MAX_TRACKABLE_LATENCY_SEC = 600; //10 min
	private static final int MAX_TRACKABLE_LATENCY_MS = MAX_TRACKABLE_LATENCY_SEC * 1000;
	private static final long PRINT_INTERVAL_MS = 60_000;
	private static final Logger LOG = LogManager.getLogger(RttRecorder.class);
	
	private final Histogram histogram = new Histogram(1L, MAX_TRACKABLE_LATENCY_MS, 5);
	private final HistogramLogWriter writer = new HistogramLogWriter(System.out);
	private volatile boolean stop = false;
	
	
	public void recordRtt(long ms) {
		synchronized(histogram) {
			histogram.recordValue(ms);
		}
	}

	@Override
	public void close() throws Exception {
		stop = true;
	}

	@Override
	public void run() {
		while (!stop)
		{
			try {
				Thread.sleep(PRINT_INTERVAL_MS);
				writer.outputLegend();
				
				synchronized(histogram) { //default histogram is not thread-safe
					writer.outputIntervalHistogram(histogram);
				}
			} catch (Exception e) {
				LOG.error("Exception while printing histogram! Gonna stop printing :(", e);
				return;
			}
		}
	}
	
}
