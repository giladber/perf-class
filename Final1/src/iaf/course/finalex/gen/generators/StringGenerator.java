package iaf.course.finalex.gen.generators;

import io.netty.util.internal.ThreadLocalRandom;

public class StringGenerator implements SizedGenerator<String> 
{
	private static final CharGenerator charGenerator = new CharGenerator();

	@Override
	public String generate() 
	{
		final int length = ThreadLocalRandom.current().nextInt();
		return generate(length);
	}

	@Override
	public String generate(int size)
	{
		StringBuilder builder = new StringBuilder(size);
		
		for (int i = 0; i < size; i++) {
			builder.append(charGenerator.generate());
		}
		
		return builder.toString();
	}
	
	private static final class CharGenerator //do not implement Generator to avoid autoboxing
	{

		public char generate() {
			return (char) ThreadLocalRandom.current().nextInt('a', 'z');
		}
	
	}
	
}