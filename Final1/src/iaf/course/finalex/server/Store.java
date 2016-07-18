package iaf.course.finalex.server;

import static iaf.course.finalex.server.Server.RECORD_RECEIVED_EVENT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import iaf.course.finalex.model.Person;
import io.vertx.core.Vertx;

public class Store
{
	private final Collection<Person> records;
	private final Geo geo = new Geo();
	private final Vertx vertx;
	
	public Store(Vertx vertx) {
		this.records = new ArrayList<>();
		this.vertx = vertx;
	}
	
	public void putRecord(Person p) {
		synchronized(records) {
			records.add(p);
			vertx.eventBus().publish(RECORD_RECEIVED_EVENT, p);
		}
	}
	
	public Optional<Person> findRecord(UUID id) {
		Optional<Person> result;
		
		synchronized(records) {
			result = records.stream().
					filter(p -> p.getId().equals(id)).
					findAny();
		}
		
		return result;
	}
	
	public long queryVisitorsTo(int areaCode) {
		return geo.countVisitorsTo(records, Areas.byCode(areaCode));
	}
}
