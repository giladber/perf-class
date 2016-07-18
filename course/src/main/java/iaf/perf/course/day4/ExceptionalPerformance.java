package iaf.perf.course.day4;

import java.util.concurrent.ThreadLocalRandom;

public class ExceptionalPerformance {

	public interface Computer<IN, OUT> {
		
		public OUT compute(IN in);
		public void sideEffect() throws SideEffectException; 
		
		@SuppressWarnings("unchecked")
		public default OUT[] compute(IN[] ins) {
			OUT[] result = (OUT[]) new Object[ins.length];
			int index = 0;
			
			for (IN in : ins) {
				result[index++] = compute(in);
				try {
					sideEffect();
				} catch (SideEffectException see) {
					//do something meaningful
				}
			}
			return result;
		}
		
	}
	
	@SuppressWarnings("serial")
	public static final class SideEffectException extends Exception {}
	
	public static final class StandardComputer implements Computer<byte[], byte[]> {

		private static final SideEffectException see = new SideEffectException();
		@Override
		public byte[] compute(byte[] in) {
			byte[] result = new byte[in.length];
			System.arraycopy(in, 1, result, 1, in.length - 1);
			result[0] = (byte) 0xFF;
			return result;
		}

		@Override
		public void sideEffect() throws SideEffectException 
		{
			if (ThreadLocalRandom.current().nextInt(10) < 3) {
				throw see;
			}
		}
		
	}
}
