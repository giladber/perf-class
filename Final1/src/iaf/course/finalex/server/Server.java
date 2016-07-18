package iaf.course.finalex.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import iaf.course.finalex.model.Person;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import static iaf.course.finalex.routes.Routes.*;

public class Server implements AutoCloseable {

	public static final String RECORD_RECEIVED_EVENT = "record_received";
	
	private static final int SERVER_LISTEN_PORT = 8080;
	private static final Logger LOG = LogManager.getLogger(Server.class);
	
	private final File journalFile = new File("C:\\temp\\journal");
	private final Vertx vertx;
	private final Journal journal;
	private final Store store;
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		new Server().start();
	}
	
	private Server() 
	{
		this.vertx = Vertx.vertx();
		
		try {
			this.journal = new Journal(new FileWriter(journalFile), this.vertx);
			this.store = new Store(this.vertx);
		} catch (IOException e) {
			throw new RuntimeException("Failed to initialize server", e);
		}
	}
	
	public void start() {
		HttpServerOptions opts = new HttpServerOptions().setLogActivity(true);
		HttpServer httpServer = vertx.createHttpServer(opts);
		Router router = Router.router(vertx);
		vertx.eventBus().registerDefaultCodec(Person.class, new PersonCodec());

		router.route().handler(BodyHandler.create());
		router.post(ADD_PERSON_URL).blockingHandler(new AddDataHandler());
		router.get(CLOSE_URL).handler(rc -> handleClose());
		router.get(GET_PERSON_URL).blockingHandler(new GetDataHandler());
		
		httpServer
			.requestHandler(router::accept)
			.listen(SERVER_LISTEN_PORT, result -> {
				if (!result.succeeded()) {
					LOG.fatal("Unable to listen on HTTP server", result.cause());
					System.exit(0);
				}else{
					HttpServer s = result.result();
					LOG.info("Listening on port {}", s.actualPort());
				}
		});
	}

	private void handleClose() {
		try {
			this.close();
		} catch (Exception e) {
			LOG.error("Error while closing", e);
		}
	}


	@Override
	public void close() throws Exception {
		journal.close();
		vertx.close();
	}

	private final class AddDataHandler implements Handler<RoutingContext>
	{
		@Override
		public void handle(RoutingContext rc)
		{
			try {
				String json = rc.getBodyAsString();
				Person person = Json.decodeValue(json, Person.class);
				LOG.info("Received person {}", person.getId());
				store.putRecord(person);
				rc.response().setStatusCode(201).end();
			}
			catch (Exception e) 
			{
				LOG.error("Errored while receiving data", e);
				rc.response().setStatusCode(500).end();
			}
		}
	}
	
	private final class GetDataHandler implements Handler<RoutingContext> {

		@Override
		public void handle(RoutingContext rc) {
			try {
				String id = rc.request().getParam("id");
				UUID uuid = UUID.fromString(id);
				Optional<Person> optPerson = store.findRecord(uuid);

				optPerson.ifPresent(p -> {
					String json = Json.encodePrettily(p);
					rc.response().
					setStatusCode(200).
					putHeader("Content-Length", String.valueOf(json.length()) ).
					putHeader("Content-Type", "application/json").
					write(json).
					end();
				});
				
				if (!optPerson.isPresent()) {
					rc.response().setStatusCode(204).end();
				}
			} catch (Exception e) {
				LOG.error("Error while finding resource", e);
				rc.response().setStatusCode(500).setStatusMessage(e.getMessage()).end();
			}
		}
	}
}
