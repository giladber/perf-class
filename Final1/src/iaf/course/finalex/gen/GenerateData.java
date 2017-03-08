package iaf.course.finalex.gen;

import java.io.File;

import iaf.course.finalex.gen.execute.ToJson;

public class GenerateData {

	public static void main(String[] args) {
		if (args.length > 0) {
			File targetFolder = new File(args[0]);
			boolean createdDirectories = targetFolder.mkdirs();
			
			if (!createdDirectories && !(targetFolder.isDirectory() && targetFolder.exists())) {
				System.out.println("Failed to create directory at " + targetFolder);
			}

			new ToJson().execute(targetFolder);
		} else {
			System.out.println("Usage: GenerateData <target_folder>");
		}
	}

}
