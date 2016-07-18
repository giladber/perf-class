package iaf.course.finalex.gen.execute;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.IntStream;

import com.google.gson.Gson;

import iaf.course.finalex.gen.generators.PersonGenerator;
import iaf.course.finalex.model.Person;

public class ToJson {
	private static final int AMOUNT = 15_000_000;
	private static final PersonGenerator generator = new PersonGenerator();

	public void execute(File directory) 
	{
		Gson gson = new Gson();
		IntStream.range(0, AMOUNT).
			forEach( i -> {
				try (FileWriter writer = new FileWriter(toFile(directory, i))) {
					Person p = generator.generate();
					gson.toJson(p, writer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		
	}
	
	private File toFile(File baseDirectory, int index) {
		return new File(baseDirectory.getAbsolutePath()+ "\\record" + index + ".json");
	}
}
