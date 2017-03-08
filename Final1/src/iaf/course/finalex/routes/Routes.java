package iaf.course.finalex.routes;

public final class Routes {

	private Routes() {}
	
	private static final String PERSON_URL = "/person";
	
	public static final String ADD_PERSON_URL = PERSON_URL + "/add";
	public static final String GET_PERSON_URL = PERSON_URL + "/find";
	public static final String CLOSE_URL = "/ceasendesist";
	public static final String VISITORS_COUNT_URL = PERSON_URL + "/count";
	
}
