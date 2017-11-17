package main;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpServer;

import io.github.squat_team.SQuATSillyBotsNegotiator;
import io.github.squat_team.model.RestArchitecture;

@SuppressWarnings("restriction")
public class NoSpringServer {

	/** The HttpServer */
	protected final transient HttpServer httpServer;

	/** the negotiator */
	private final transient SQuATSillyBotsNegotiator negotiator;

	public NoSpringServer(SQuATSillyBotsNegotiator negotiator, int port) throws IOException {
		this.negotiator = negotiator;
		this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);

		this.httpServer.createContext("/result", exchg -> {
			JSONObject result = new JSONObject();
			if ("GET".equalsIgnoreCase(exchg.getRequestMethod())) {

				RestArchitecture restArchitecture = null;
				try {
					restArchitecture = this.negotiator.findAgreementCandidate();
					buildResult(result, restArchitecture);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
					result.put("result", e.getMessage()).toString();
				}
			} else {
				result.put("result", "Invalid Request Method").toString();
			}

			String rsp = result.toString();
			try {
				exchg.getResponseHeaders().add("Status", "OK");
				exchg.sendResponseHeaders(200, rsp.length());
				try (OutputStream os = exchg.getResponseBody()) {
					os.write(rsp.getBytes());
					os.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		this.httpServer.start();
	}

	private static JSONObject buildResult(JSONObject target, RestArchitecture restArch) {
		if (restArch == null)
			return target;

		// Put name and architecture
		target.put("architecture-instance", restArch.getRestArchitecture());

		// Put additional arch
		if (restArch.getCost() != null)
			target.put("cost", restArch.getCost());
		if (restArch.getInsinter() != null)
			target.put("insinter-modular", restArch.getInsinter());
		if (restArch.getSplitrespn() != null)
			target.put("splitrespn-modular", restArch.getSplitrespn());
		if (restArch.getWrapper() != null)
			target.put("wrapper-modular", restArch.getWrapper());

		return target;
	}
}
