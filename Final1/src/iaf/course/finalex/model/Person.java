package iaf.course.finalex.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Person {
	private final String name;
	private final UUID id;
	private final Collection<Phone> devices;
	
	public Person(String name, Collection<Phone> devices) {
		this(name, UUID.randomUUID(), devices);
	}

	@JsonCreator
	public Person(@JsonProperty("name") String name, 
			@JsonProperty("id") UUID id, 
			@JsonProperty("devices") Collection<Phone> devices) {
		this.name = name;
		this.devices = new HashSet<>(devices);
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public UUID getId() {
		return id;
	}
	
	public Collection<Phone> getDevices() {
		return devices;
	}
	

	@Override
	public String toString() {
		return "Person [name=" + name + ", id=" + id + ", devices=" + devices + "]";
	}
	
	
	
	
}
