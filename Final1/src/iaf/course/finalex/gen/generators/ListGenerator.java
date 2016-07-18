package iaf.course.finalex.gen.generators;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListGenerator<T> implements SizedGenerator<List<T>> {
	
	private final Generator<T> generator;
	
	public ListGenerator(Generator<T> gen) {
		this.generator = gen;
	}

	@Override
	public List<T> generate() {
		return generate(1);
	}

	@Override
	public List<T> generate(int size) {
		return IntStream.range(0, size + 1).
				mapToObj(i -> generator.generate()).
				collect(Collectors.toList());
	}
	
}
