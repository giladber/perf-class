package iaf.course.finalex.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationData 
{
	private final long _time;
	private final double _lat;
	private final double _long;
	
	@JsonCreator
	public LocationData(
			@JsonProperty("_time") long _time, 
			@JsonProperty("_lat") double _lat, 
			@JsonProperty("_long") double _long) {
		this._time = _time;
		this._lat = _lat;
		this._long = _long;
	}

	public long getTime() {
		return _time;
	}

	public double getLat() {
		return _lat;
	}

	public double getLong() {
		return _long;
	}

	@Override
	public String toString() {
		return "LocationData [_time=" + _time + ", _lat=" + _lat + ", _long=" + _long + "]";
	}
	
	
	
	
	
}
