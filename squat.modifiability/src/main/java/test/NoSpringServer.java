package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.henshin.model.HenshinPackage;
import org.eclipse.emf.henshin.trace.TracePackage;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.core.CorePackage;
import org.palladiosimulator.pcm.core.entity.EntityPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.seff.SeffPackage;
import org.palladiosimulator.pcm.system.SystemPackage;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import de.uka.ipd.sdq.identifier.IdentifierPackage;
import de.uka.ipd.sdq.stoex.StoexPackage;
import io.github.squat_team.AbstractPCMBot;
import io.github.squat_team.json.JSONUtils;
import io.github.squat_team.json.JSONification;
import io.github.squat_team.json.UnJSONification;
import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.modifiability.ModifiabilityPCMScenario;
import io.github.squat_team.modifiability.kamp.KAMPPCMBot;
import io.github.squat_team.modifiability.kamp.EvaluationType;
import io.github.squat_team.util.SQuATHelper;

public class NoSpringServer {
	/** The port to use */
	private final int port;

    /** The HttpServer */
    protected final transient HttpServer httpServer;

    /** Map to save the current execution status of the various bots */
    private final Map<String, ExecutionStatus> executions;

    /** Thread pool for executions */
    private final ExecutorService threadPool = Executors.newFixedThreadPool(32);

	public static void recreate() {
		Resource.Factory.Registry.INSTANCE.getContentTypeToFactoryMap().clear();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().clear();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		//PCM Packages
		EPackage.Registry.INSTANCE.clear();
		EPackage.Registry.INSTANCE.put(IdentifierPackage.eNS_URI, IdentifierPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(StoexPackage.eNS_URI, StoexPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(CorePackage.eNS_URI, CorePackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(EntityPackage.eNS_URI, EntityPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(RepositoryPackage.eNS_URI, RepositoryPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(SystemPackage.eNS_URI, SystemPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(ResourceenvironmentPackage.eNS_URI, ResourceenvironmentPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(AllocationPackage.eNS_URI, AllocationPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(UsagemodelPackage.eNS_URI, UsagemodelPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(SeffPackage.eNS_URI, SeffPackage.eINSTANCE);
		//Henshin Packages
		EPackage.Registry.INSTANCE.put(HenshinPackage.eNS_URI, HenshinPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(TracePackage.eNS_URI, TracePackage.eINSTANCE);
		//EPackage.Registry.INSTANCE.put(StateSpacePackage.eNS_URI, StateSpacePackage.eINSTANCE);
		//EPackage.Registry.INSTANCE.put(WrapPackage.eNS_URI, WrapPackage.eINSTANCE);
	}

    /**
	 * Bot executor function, this functions generates the UUID and prepares asynchronous execution
	 *
	 * @param requestBody the HTTP POST request body
	 * @param fn the function to execute the corresponding function to the rest endpoint
	 */
	private String botFn(String requestBody, BiFunction<ExecutionContext, JSONStringer, String> fn) {
        //recreate();
		String executionUUID = UUID.randomUUID().toString();
		String response = executionUUID;
		try {
			JSONStringer jsonStringer = new JSONStringer();
			jsonStringer.object();
			jsonStringer.key("executionID").value(executionUUID);
			jsonStringer.endObject();
			response  = jsonStringer.toString();
			this.executions.put(executionUUID, ExecutionStatus.WAITING);

			//
			// Execute on the Bot thread pool
			//
			this.threadPool.execute(() -> {
				ExecutionStatus status = this.executions.get(executionUUID);
				if (status == null)
					return;
				this.executions.put(executionUUID, ExecutionStatus.EXECUTING);

				try {
					// Retrieve parameters
					JSONObject jsonBody = new JSONObject(requestBody);
                    String callbackURL = jsonBody.getString("cbURL");

                    // Write from JSON
                    UnJSONification unJSONification = new UnJSONification(executionUUID);
                    unJSONification.getFile(jsonBody.getJSONObject("cost"));
                    unJSONification.getFile(jsonBody.getJSONObject("insinter-modular"));
                    unJSONification.getFile(jsonBody.getJSONObject("splitrespn-modular"));
                    unJSONification.getFile(jsonBody.getJSONObject("wrapper-modular"));

					// Architecture instance
                    JSONObject jsonArchInstance = jsonBody.getJSONObject("architecture-instance");
					PCMArchitectureInstance architectureInstance = unJSONification.getArchitectureInstance(jsonArchInstance);

                    // Scenario - TODO
                    ModifiabilityPCMScenario scenario = ModifiabilityBotTest.initializeScenario();

                    // Create the bot and context
                    KAMPPCMBot bot = new KAMPPCMBot(scenario);
                    bot.setEvaluationType(EvaluationType.COMPLEXITY);
					ExecutionContext context = new ExecutionContext(bot, architectureInstance);

					// Prepare the result stringer
					JSONStringer resultStringer = new JSONStringer();
					resultStringer.object().key("executionUUID").value(executionUUID);

					// Execute and generate result
                    String result = fn.apply(context, resultStringer);

					// call back the result callback url
					URL url = new URL(callbackURL);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setRequestProperty( "Content-Length", String.valueOf(result.length()));
					try (OutputStream outputStream = connection.getOutputStream()) {
						outputStream.write(result.getBytes());
						outputStream.flush();
					}

                    try (InputStream in = connection.getInputStream()) {
                    }
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					this.executions.remove(executionUUID);
				}
			});
		} catch (JSONException e) {
		}
        return response;
	}

    /**
     * 
     */
    private static String readBody(HttpExchange exchg) {
        String body = null;

        try (InputStream is = exchg.getRequestBody()) {
            List<Byte> byteList = new ArrayList<>();
            int ch;
            while ((ch = is.read()) != -1) {
                byteList.add((byte) ch);
            }

            byte b[] = new byte[byteList.size()];
            int index = -1;
            for (byte byt : byteList) {
                b[++index] = byt;
            }
            byteList.clear();
            body = new String(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

    public NoSpringServer(int port, String...args) throws IOException {
        this.port = port;
        this.executions = Collections.synchronizedMap(new HashMap<>());
        this.httpServer = HttpServer.create(new InetSocketAddress(this.port), 0);

        this.httpServer.createContext("/status", exchg -> {
            String rsp;
            if ("GET".equalsIgnoreCase(exchg.getRequestMethod())) {
                rsp = exchg.getRequestURI().getPath();
                if (rsp.contains("/status/")) {
                    rsp = rsp.substring("/status/".length());
                }
            } else {
                rsp = "INVALID METHOD";
            }
            exchg.sendResponseHeaders(200, rsp.length());
            OutputStream os = exchg.getResponseBody();
            os.write(rsp.getBytes());
            os.close();
        });

        this.httpServer.createContext("/test", exchg -> {
            String body = readBody(exchg);
			System.out.println(body);
			exchg.getResponseHeaders().add("Status", "OK");
			exchg.sendResponseHeaders(200, body.length());
			try (OutputStream os = exchg.getResponseBody()) {
                os.write(body.getBytes());
                os.flush();
			}
        });

		this.httpServer.createContext("/run", exchg -> {
			String input = readBody(exchg);
			System.out.println(input);
			exchg.getResponseHeaders().add("Status", "OK");
			exchg.sendResponseHeaders(200, input.length());
			try (OutputStream os = exchg.getResponseBody()) {
                os.write(input.getBytes());
                os.flush();
			}
        });

        this.httpServer.createContext("/analyze", exchg -> {
            String body = readBody(exchg);
            String rsp = null;
            if ("POST".equalsIgnoreCase(exchg.getRequestMethod())) {
                rsp = this.botFn(body, (ctx, stringer) -> {
                    KAMPPCMBot bot = ctx.getBot();
                    PCMArchitectureInstance architectureInstance = ctx.getArchitectureInstance();
                    PCMScenarioResult result = bot.analyze(architectureInstance);
                    String resultString;
                    try {
                        JSONification jsoNification = new JSONification(stringer);
                        jsoNification.add(result);
                        resultString = jsoNification.toJSON();
                    } catch (JSONException e) {
                        resultString = e.getMessage();
                    }
                    return resultString;
                });
            } else {
                rsp = "INVALID METHOD";
            }

            exchg.getResponseHeaders().add("Status", "OK");
			exchg.sendResponseHeaders(200, rsp.length());
			try (OutputStream os = exchg.getResponseBody()) {
                os.write(rsp.getBytes());
                os.flush();
			}
        });

        this.httpServer.createContext("/searchForAlternatives", exchg -> {
            String body = readBody(exchg);
            String rsp = null;
            if ("POST".equalsIgnoreCase(exchg.getRequestMethod())) {
                rsp = this.botFn(body, (ctx, stringer) -> {
                    KAMPPCMBot bot = ctx.getBot();
                    PCMArchitectureInstance architectureInstance = ctx.getArchitectureInstance();
                    List<PCMScenarioResult> results = bot.searchForAlternatives(architectureInstance);
                    String resultString;
                    try {
                        JSONification jsoNification = new JSONification(stringer);
                        jsoNification.add(results);
                        resultString = jsoNification.toJSON();
                    } catch (JSONException e) {
                        resultString = e.getMessage();
                    }
                    return resultString;
                });
            } else {
                rsp = "INVALID METHOD";
            }

            exchg.getResponseHeaders().add("Status", "OK");
			exchg.sendResponseHeaders(200, rsp.length());
			try (OutputStream os = exchg.getResponseBody()) {
                os.write(rsp.getBytes());
                os.flush();
			}
        });

        this.httpServer.start();

        if(args.length > 0 && "print".equalsIgnoreCase(args[0])) {
            String BASE = TestConstants.MODEL_PATH;
            String basicPath = TestConstants.MODEL_PATH + "/default";
            Allocation allocation = SQuATHelper.loadAllocationModel("file:/" + basicPath + ".allocation");
            org.palladiosimulator.pcm.system.System system = SQuATHelper
                    .loadSystemModel("file:/" + basicPath + ".system");
            ResourceEnvironment resourceenvironment = SQuATHelper
                    .loadResourceEnvironmentModel("file:/" + basicPath + ".resourceenvironment");
            Repository repository = SQuATHelper.loadRepositoryModel("file:/" + basicPath + ".repository");
            UsageModel usageModel = SQuATHelper.loadUsageModel("file:/" + basicPath + ".usagemodel");
            PCMArchitectureInstance architectureInstance = new PCMArchitectureInstance("", repository, system, allocation, resourceenvironment, usageModel);
            
            Repository repositoryAlternatives = SQuATHelper.loadRepositoryModel("file:/" + TestConstants.MODEL_PATH + "/alternativeRepository.repository");
            architectureInstance.setRepositoryWithAlternatives(repositoryAlternatives);

            JSONification jsoNification2 = new JSONification();
            jsoNification2.add(architectureInstance);
            jsoNification2.add("cost", new File("" + basicPath + ".cost"));
            jsoNification2.add("insinter-modular", new File("" + BASE + "/insinter-modular.henshin"));
            jsoNification2.add("splitrespn-modular", new File("" + BASE + "/splitrespn-modular.henshin"));
            jsoNification2.add("wrapper-modular", new File("" + BASE + "/wrapper-modular.henshin"));
            
            String jsonArch = jsoNification2.toJSON();
            System.out.println(jsonArch);
        }

    }
}
