package iaf.course.finalex.gen.generators;

public interface SizedGenerator<T> extends Generator<T> {
	public T generate(int size);
}
