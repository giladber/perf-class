package iaf.course.finalex.gen.generators;

import iaf.course.finalex.model.LocationData;
import iaf.course.finalex.model.Phone;
import io.netty.util.internal.ThreadLocalRandom;

public class PhoneGenerator implements Generator<Phone> {

	private static final ListGenerator<LocationData> locationHistoryGenerator = new ListGenerator<>(new LocationGenerator());
	private static final PhoneNumberGenerator phoneNumGenerator = new PhoneNumberGenerator();
	
	@Override
	public Phone generate() {
		return new Phone(phoneNumGenerator.generate(),
				locationHistoryGenerator.generate(ThreadLocalRandom.current().nextInt(1, 10)));
	}

}
