package iaf.course.finalex.client;

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

	private static final String BASE_DIR_PATH = "C:\\temp";
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 8080;
	private static final RttRecorder RECORDER = new RttRecorder();
	
	private static final Logger LOG = LogManager.getLogger(Client.class);

	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		HttpClient client = vertx.createHttpClient();
		final FileSystem fs = vertx.fileSystem();
		final RecordHttpSender sender = new RecordHttpSender(client);
		final ExecutorService ex = Executors.newSingleThreadExecutor();
		
		ex.submit(RECORDER);
		final WorkerExecutor executor = vertx.createSharedWorkerExecutor("send-data-pool", 1);
		
		LOG.info("Reading directory {}", BASE_DIR_PATH);
		fs.readDir(BASE_DIR_PATH, resultOfFiles -> {
			if (resultOfFiles.succeeded()) {
				resultOfFiles.result().forEach(path -> {
					if (!path.endsWith(".json")) return;
					
					fs.readFile(path, bufferResult -> {
						if (bufferResult.succeeded()) {
							final long now = System.currentTimeMillis();
							String json = new String(bufferResult.result().getBytes());
							executor.executeBlocking(fut -> {
								try {
									sender.send(json, PORT, HOSTNAME, ADD_PERSON_URL, 5000);
									final long then = System.currentTimeMillis();
									RECORDER.recordRtt(then - now);
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
