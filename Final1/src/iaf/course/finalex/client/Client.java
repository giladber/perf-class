package iaf.course.finalex.client;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpClient;

import static iaf.course.finalex.routes.Routes.*;


public class Client 
{

	private static final Logger LOG = LogManager.getLogger(Client.class);

	private static final class BootstrapData {
		final String directory;
		final String hostname;
		final int port;
		
		BootstrapData(String[] args) {
			try {
				directory = args[0];
				hostname = args[1];
				port = Integer.valueOf(args[2]);
			} catch (NumberFormatException nfe) {
				LOG.error("Invalid port: " + args[2] + " , expected a number", nfe);
				throw new ExceptionInInitializerError(nfe);
			} catch (Exception e) {
				LOG.error("Error while initializing Client: " + e);
				throw new ExceptionInInitializerError(e);
			}

		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		if (args.length < 3) {
			System.out.println("Usage:  Client.jar <json_source_directory> <hostname> <port>");
		}
		
		BootstrapData data = new BootstrapData(args);
		
		Vertx vertx = Vertx.vertx();
		HttpClient client = vertx.createHttpClient();
		final FileSystem fs = vertx.fileSystem();
		final RecordHttpSender sender = new RecordHttpSender(client);
		final ExecutorService ex = Executors.newSingleThreadExecutor();
		final RttRecorder recorder = new RttRecorder(data.directory + "\\histogram");
		
		ex.submit(recorder);
		final WorkerExecutor executor = vertx.createSharedWorkerExecutor("send-data-pool", 1);

		LOG.info("Reading directory {}", data.directory);
		fs.readDir(data.directory, resultOfFiles -> {
			if (resultOfFiles.succeeded()) {
				resultOfFiles.result().forEach(path -> {
					if (!path.endsWith(".json")) return;
					
					fs.readFile(path, bufferResult -> {
						if (bufferResult.succeeded()) {
							final long now = System.currentTimeMillis();
							String json = new String(bufferResult.result().getBytes());
							executor.executeBlocking(fut -> {
								try {
									sender.send(json, data.port, data.hostname, ADD_PERSON_URL, 5000);
									final long then = System.currentTimeMillis();
									recorder.recordRtt(then - now);
								} catch (Exception e) {
									LOG.error("Failed executing send!", e);
								}
								fut.complete();
							}, false,
							result -> { 
								if (result.failed()) {
									result.cause().printStackTrace();
								}
							} );
						}
					});
				});
			}
		});
		
	}

}
