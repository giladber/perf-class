package iaf.course.finalex.gen.generators;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SetGenerator<T> implements SizedGenerator<Set<T>> 
{
	
	private final Generator<T> baseGenerator;
	
	public SetGenerator(Generator<T> base)
	{
		Objects.requireNonNull(base);
		this.baseGenerator = base;
	}
	
	@Override
	public Set<T> generate() {
		return generate(1);
	}

	@Override
	public Set<T> generate(int size) {
		return IntStream.range(0, size).mapToObj(i -> baseGenerator.generate()).
				collect(Collectors.toSet());
	}

}
