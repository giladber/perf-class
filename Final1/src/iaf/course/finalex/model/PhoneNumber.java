package iaf.course.finalex.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PhoneNumber {
	private final short areaCode;
	private final String number;
	
	@JsonCreator
	public PhoneNumber(@JsonProperty("areaCode") short areaCode, 
			@JsonProperty("number") String number) 
					throws IllegalArgumentException, NumberFormatException
	{
		Objects.requireNonNull(number);
		if (areaCode < 0 || areaCode > 99) 
		{
			throw new IllegalArgumentException("Area code must be between 0 and 99 inclusive, is " + areaCode);
		}
		
		int numAsInt = Integer.valueOf(number);
		if (number.length() != 7 || numAsInt < 0) {
			throw new IllegalArgumentException("Number must be 7 digits long and non-negative, is: " + numAsInt);
		}

		this.areaCode = areaCode;
		this.number = number;
	}
	
	public short getAreaCode() {
		return areaCode;
	}

	public String getNumber() {
		return number;
	}
	
	@Override
	public String toString() {
		return areaCode + "-" + number;
	}
	
	
	
}
