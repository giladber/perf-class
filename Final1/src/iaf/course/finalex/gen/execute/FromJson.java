package iaf.course.finalex.gen.execute;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import iaf.course.finalex.model.Person;

public class FromJson 
{
	
	public Collection<Person> read(File from) 
	{
		Gson gson = new Gson();
		
		try (FileReader reader = new FileReader(from))
		{
			Set<Person> result = gson.fromJson(reader, new TypeToken<Set<Person>>(){}.getType());
			return result;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return Collections.emptySet();
	}
	
}
