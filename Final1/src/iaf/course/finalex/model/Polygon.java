package iaf.course.finalex.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static java.lang.Math.*;

import java.util.Arrays;
import java.util.Objects;

public class Polygon 
{
	private final double[][] coordinates;
	
	@JsonCreator
	public Polygon(@JsonProperty("coordinates") double[][] coordinates) {
		Objects.requireNonNull(coordinates);
		
		this.coordinates = coordinates;
		if (coordinates[0][0] != coordinates[coordinates.length - 1][0] ||
				coordinates[0][1] != coordinates[coordinates.length - 1][1])
		{
			throw new IllegalArgumentException("First and last points of polygon must be identical, but are: " + 
					Arrays.toString(coordinates[0]) + ", " + 
					Arrays.toString(coordinates[coordinates.length - 1]));
		}
	}
	
	public boolean contains(double _lat, double _long) {
		int size = coordinates.length;
		int numCrosses = 0;
		
		for (int i = 0; i < size; i++) {
			if (intersects(coordinates[i], coordinates[ (i+1) % size], _lat, _long)) {
				numCrosses++;
			}
		}
		
		return numCrosses % 2 == 1;
	}

	 static boolean intersects(double[] p1, double[] p2, double _lat, double _long) {
	        if (p1[1] > p2[1])
	            return intersects(p2, p1, _lat, _long);
	 
	        if (_long == p1[1] || _long == p2[1])
	            _long += 0.0001;
	 
	        if (_long > p2[1] || _long < p1[1] || _lat > max(p1[0], p2[0]))
	            return false;
	 
	        if (_lat < min(p1[0], p2[0]))
	            return true;
	 
	        double red = (_long - p1[1]) / (double) (_lat - p1[0]);
	        double blue = (p2[1] - p1[1]) / (double) (p2[0] - p1[0]);
	        return red >= blue;
	    }

	@Override
	public String toString() {
		return "Polygon [coordinates=" + Arrays.deepToString(coordinates) + "]";
	}
	 
	 
	 
}
