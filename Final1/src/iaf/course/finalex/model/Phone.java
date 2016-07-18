package iaf.course.finalex.model;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Phone
{
	private final PhoneNumber number;
	private final List<LocationData> locationHistory; //Promised to be sorted, not using LinkedList due to it not being unmodifiable
	private final UUID id;
	
	public Phone(PhoneNumber number, List<LocationData> locations) {
		this(number, locations, UUID.randomUUID());
	}
	
	@JsonCreator
	public Phone(@JsonProperty("number") PhoneNumber number, 
			@JsonProperty("locationHistory") List<LocationData> locations, 
			@JsonProperty("id") UUID id) {
		this.number = number;
		this.locationHistory = Collections.unmodifiableList(locations);
		this.id = id;
	}

	public PhoneNumber getNumber() {
		return number;
	}

	
	public LocationData locate() {
		return locationHistory.get(locationHistory.size() - 1);
	}

	public List<LocationData> locationHistory() {
		return locationHistory;
	}

	public UUID getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Phone [number=" + number + ", locationHistory=" + locationHistory + ", id=" + id + "]";
	}
	
	
	
}
