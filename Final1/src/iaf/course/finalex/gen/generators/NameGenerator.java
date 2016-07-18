package iaf.course.finalex.gen.generators;

import io.netty.util.internal.ThreadLocalRandom;

public class NameGenerator implements Generator<String> {

	private static final StringGenerator gen = new StringGenerator();
	
	@Override
	public String generate() {
		return gen.generate(ThreadLocalRandom.current().nextInt(2, 30)) + 
				" " + 
				gen.generate(ThreadLocalRandom.current().nextInt(2, 30));
	}

}
