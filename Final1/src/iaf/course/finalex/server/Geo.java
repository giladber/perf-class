package iaf.course.finalex.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iaf.course.finalex.model.LocationData;
import iaf.course.finalex.model.Person;
import iaf.course.finalex.model.Phone;
import iaf.course.finalex.model.Polygon;

public class Geo {
	
	private static final double NANOS_IN_DAY = 1E9 /* Nanos in second */ 
			* 60 /* Sec in min */
			* 60 /* Min in hr */
			* 24; /* Hr in day */
	private static final Map<Long, Integer> EMPTY_OCC_MAP = new HashMap<>();
	
	private double dist(double lat1, double long1, double lat2, double long2) {
		double earthRadius = 6371;
		double latDiff = deg2rad(lat1-lat2);
		double longDiff = deg2rad(long1-long2);
		
		double sinLatDiff = Math.sin(latDiff);
		double sinLongDiff = Math.sin(longDiff);
		double cosLat1 = Math.cos(deg2rad(lat1));
		double cosLat2 = Math.cos(deg2rad(lat2));
		
		double a = sinLatDiff * sinLatDiff + sinLongDiff * sinLongDiff * cosLat1 * cosLat2;
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		return earthRadius * c;
	}
	
	private double deg2rad(double deg) {
		return deg * Math.PI / 180;
	}
	
	
	public double distPerDay(List<LocationData> locations) //list is assumed to be sorted by time 
	{
		double totalDist = 0;
		
		int firstDay = (int) (nano2day(locations.get(0).getTime()));
		int lastDay = (int) (nano2day(locations.get(locations.size()-1).getTime()));
		int numDays = lastDay - firstDay;

		for (int i = 0; i < locations.size() - 1; i++) {
			LocationData cur = locations.get(i);
			LocationData next = locations.get(i+1);
			totalDist += dist(cur.getLat(), cur.getLong(), next.getLat(), next.getLong());
		}
		
		return totalDist / numDays;
	}
	
	public long countVisitorsTo(Collection<Person> records, Polygon area) 
	{
		return records.stream().
			map(Person::getDevices).
			filter(devs -> 
				devs.stream().
				map(Phone::locationHistory).
				flatMap(locs -> locs.stream()).
				anyMatch(loc -> inPolygon(area, loc))).
			count();
	}
	
	private boolean inPolygon(Polygon poly, LocationData loc) {
		return poly.contains(loc.getLat(), loc.getLong());
	}
	
	/**
	 * Computes, from the input records, the maximum amount of people who were present at the input
	 * polygon, in any given day since 01/01/1970.
	 * @param polygon area in which we want to find the maximum number of people present in a single day
	 * @param records records of people to check again
	 * @return
	 */
	public long maxByTimeDaily(Polygon polygon, Collection<Person> records) {
		return 
			records.stream().
			map(p -> groupByPresent(p, polygon)).
			reduce((map1, map2) -> {
				Map<Long, Integer> res = new HashMap<>();
				map1.entrySet().forEach(e -> res.put(e.getKey(), e.getValue() + map2.get(e.getKey())));
				return res;
			}).
			orElse(EMPTY_OCC_MAP).
			values().stream().
				max((i1, i2) -> i1 -i2).
				orElse(0);
	}
	
	/**
	 * Returns a mapping of day (since epoch) -> boolean, each entry representing that the 
	 * input person was present in the input polygon in that day (day = key)
	 * @param person
	 * @param poly
	 * @return
	 */
	private Map<Long, Integer> groupByPresent(Person person, Polygon poly) {
		Map<Long, Integer> occurencesMap = new HashMap<>();
		
		person.getDevices().stream().
			flatMap(p -> p.locationHistory().stream()).
			filter(loc -> inPolygon(poly, loc)).
			forEach(loc -> occurencesMap.put((long) nano2day(loc.getTime()), 1));
		
		return occurencesMap;
	}
	
	private static double nano2day(double nano) {
		return nano / NANOS_IN_DAY;
	}
	
}
