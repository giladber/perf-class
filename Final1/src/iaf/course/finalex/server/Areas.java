package iaf.course.finalex.server;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import iaf.course.finalex.model.Polygon;

public final class Areas 
{
	
	private static final File TEL_AVIV_JSON = new File("C:\\temp\\areas\\telaviv.json"); 
	public static final Polygon TEL_AVIV_YA_HABIBI;
	
	static {
		try {
			TEL_AVIV_YA_HABIBI = new ObjectMapper().readValue(TEL_AVIV_JSON, Polygon.class);
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e); //terminate if unable to read coordinates file
		}
	}
	
	public static Polygon byCode(int code) {
		return AreaCode.lookup(code).polygon;
	}
	
	private static enum AreaCode {
		TEL_AVIV(51, TEL_AVIV_YA_HABIBI);
		
		private AreaCode(int code, Polygon poly) {
			this.code = code;
			this.polygon = poly;
		}
		
		private static AreaCode lookup(int code) {
			return code == TEL_AVIV.code ? AreaCode.TEL_AVIV : null;
		}
		
		private final int code;
		private final Polygon polygon;
		
	}
}
