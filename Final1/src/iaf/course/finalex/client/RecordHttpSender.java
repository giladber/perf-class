package iaf.course.finalex.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;

public class RecordHttpSender 
{
	private final HttpClient client;
	private static final Logger LOG = LogManager.getLogger(RecordHttpSender.class);

	public RecordHttpSender(HttpClient client) {
		this.client = client;
	}

	public void send(String record, int port, String host, String uri, int timeoutMs) 
	{
		LOG.info("starting sending of record {} \nTo {}:{}{}", record, host, port, uri);
		
		HttpClientRequest req = client.post(port, host, uri).
			putHeader("content-type", "application/json").
			putHeader("content-length", String.valueOf(record.length())).
			handler(this::handleResponse).
			exceptionHandler(Throwable::printStackTrace).
			setTimeout(timeoutMs).
			write(record);
		
		req.end();
		
		LOG.info("Finished sending person!");
	}

	private void handleResponse(HttpClientResponse response) 
	{
		if (response.statusCode() >= 200 && response.statusCode() <= 300) 
		{
			System.out.println("Added record successfully! status="+response.statusCode());
		}else{
			System.out.println("Unexpected result when adding response: status=" + response.statusCode() +
					", status message=" + response.statusMessage());
		}
	};
}
