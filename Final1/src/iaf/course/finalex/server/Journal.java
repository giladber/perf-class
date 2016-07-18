package iaf.course.finalex.server;

import static iaf.course.finalex.server.Server.RECORD_RECEIVED_EVENT;

import java.io.Writer;
import java.time.Instant;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import iaf.course.finalex.model.Person;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;


/*
 * Oh, the horror
 */
public class Journal implements AutoCloseable
{
	
	private final Map<UUID, Instant> history = new ConcurrentHashMap<>();
	private final Writer writer;
	private Instant lastWritten = Instant.MIN;
	
	
	private static final Logger LOG = LogManager.getLogger(Journal.class);
	
	public Journal(final Writer writer, Vertx vertx) {
		this.writer = writer;
		vertx.eventBus().consumer(RECORD_RECEIVED_EVENT).
			handler(this::handleRecord).
			completionHandler(this::handleCompletion);
		
		vertx.setPeriodic(1000, id -> {
			vertx.executeBlocking(f -> {
				doWrite();
				f.complete();
			}, a -> {});}); //probably legit perl
		
	}

	private void handleRecord(Message<Object> o) {
		if (o != null && o.body() instanceof Person) {
			Person p = (Person) o.body();
			history.put(p.getId(), Instant.now());
			LOG.info("Added {} to history", p.getId());
		} else {
			LOG.warn("Received a message with invalid record data in Journal");
		}
	}
	
	/*
	 * Changing lastWritten to the .now() *before* calling write,
	 * since entries may be added to the map while writing - which we may not observe.
	 * Worst case - an entry gets written twice.
	 * To fix the "worst case", instead of using Instant.now() we would give lastWritten
	 * the maximum value of the Instants of written entries.
	 * But we're lazy!
	 */
	private void doWrite() {
		Instant previousLastWritten = lastWritten;
		try {
			lastWritten = Instant.now(); 
			String logData = aggregateSince(previousLastWritten);
			if (logData.length() > 0) {
				writer.write(logData);
				writer.flush();
			}
		} catch (Exception e) {
			lastWritten = previousLastWritten;
			LOG.error("Error journaling, ", e);
		}
	}
	
	private String aggregateSince(Instant when) 
	{
		return history.entrySet().stream().
			filter(entry -> {
				try {
					checkWritabe(entry, when);
				} catch (Exception e) {
					return false;
				}
				return true;
			}).
			map(this::entry2string).
			collect(Collectors.joining());
	}
	
	
	private String entry2string(Entry<UUID, Instant> e) {
		return "\n" + e.getKey() + " was received at [" + e.getValue().toString() + "]";
	}
	
	private void checkWritabe(Map.Entry<UUID, Instant> entry, Instant lw) {
		if (entry.getValue().isBefore(lw))
		{
			throw new No();
		}
	}
	
	
	private void handleCompletion(AsyncResult<Void> r) {
		if (r.succeeded()) {
			LOG.info("Successfully registered Journal on event bus for {}", RECORD_RECEIVED_EVENT);
		} else {
			LOG.error("Failed registering to event bus for {}", RECORD_RECEIVED_EVENT, r.cause());
		}
	}
	
	
	@SuppressWarnings("serial")
	private static final class No extends RuntimeException {}


	@Override
	public void close() throws Exception {
		this.writer.close();
	}
	
	
}
