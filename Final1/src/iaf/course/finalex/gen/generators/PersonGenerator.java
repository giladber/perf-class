package iaf.course.finalex.gen.generators;

import iaf.course.finalex.model.Person;
import iaf.course.finalex.model.Phone;
import io.netty.util.internal.ThreadLocalRandom;

public class PersonGenerator implements Generator<Person> {

	private static final NameGenerator nameGenerator = new NameGenerator();
	private static final ListGenerator<Phone> phonesGenerator = new ListGenerator<>(new PhoneGenerator());
	
	@Override
	public Person generate() {
		return new Person(
				nameGenerator.generate(), 
				phonesGenerator.generate(ThreadLocalRandom.current().nextInt(1, 4)));
	}

}
