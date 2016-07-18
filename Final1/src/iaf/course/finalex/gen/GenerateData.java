package iaf.course.finalex.gen;

import java.io.File;

import iaf.course.finalex.gen.execute.ToJson;

public class GenerateData {

	public static void main(String[] args) {
		new ToJson().execute(new File("C:\\temp\\perfdata"));
	}

}
