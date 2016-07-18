package iaf.course.finalex.gen;

import java.io.File;
import java.util.Collection;

import iaf.course.finalex.gen.execute.FromJson;
import iaf.course.finalex.model.Person;

public class ReadData {

	
	public static void main(String[] args) {
		Collection<Person> result = new FromJson().read(new File("C:\\temp\\persons.json"));
		result.stream().forEach(System.out::println);
	}
}
