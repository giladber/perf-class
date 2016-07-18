package iaf.course.finalex.gen.generators;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import iaf.course.finalex.model.PhoneNumber;
import io.netty.util.internal.ThreadLocalRandom;

public class PhoneNumberGenerator implements Generator<PhoneNumber>
{
	private static final String ZERO = "0";
	private static final short NUMBER_LENGTH = 7;
	
	@Override
	public PhoneNumber generate() {
		short areaCode = (short) ThreadLocalRandom.current().nextInt(1, 100);
		String number = prependZeroes(ThreadLocalRandom.current().nextInt(1, 9999999));
		
		return new PhoneNumber(areaCode, number);
	}
	
	public String prependZeroes(int to) 
	{
		int length = (int) (Math.log10(to) + 1);
		String zeroes = IntStream.
				range(0, NUMBER_LENGTH - length).
				mapToObj(i -> ZERO).
				collect(Collectors.joining());
		
		return zeroes + to;
	}
	
	
}
