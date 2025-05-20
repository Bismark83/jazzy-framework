/**
 * The main server class of the Jazzy Framework.
 * This class is responsible for starting a HTTP server on a specified port,
 * accepting client connections and delegating requests to appropriate handlers.
 * It also automatically registers the metrics endpoint.
 */
package jazzyframework.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import jazzyframework.controllers.MetricsController;
import jazzyframework.routing.Router;

public class Server {
    private final Router router;
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    /**
     * Creates a new Server instance with the specified router.
     * Automatically registers the metrics endpoint for monitoring.
     * 
     * @param router The router to use for handling HTTP requests
     */
    public Server(Router router, Config config) {
        this.router = router;

        if (config.isEnableMetrics()) {
            router.addRoute("GET", "/metrics", "getMetrics", MetricsController.class);
            logger.info("Metrics route added");
        }
    }

    /**
     * Starts the server on the specified port.
     * Creates a new thread for each incoming connection.
     * 
     * @param port The port number to listen on
     */
    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new RequestHandler(clientSocket, router)).start();
            }
        } catch (IOException e) {
            logger.severe("Error starting server: " + e.getMessage());
        }
    }

}
