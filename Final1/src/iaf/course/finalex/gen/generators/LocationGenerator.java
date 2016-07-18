package iaf.course.finalex.gen.generators;

import iaf.course.finalex.model.LocationData;
import io.netty.util.internal.ThreadLocalRandom;

public class LocationGenerator implements Generator<LocationData> {

	@Override
	public LocationData generate() {
		return new LocationData(ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE),
				ThreadLocalRandom.current().nextDouble(-180, 180),
				ThreadLocalRandom.current().nextDouble(-180, 180));
	}

}
