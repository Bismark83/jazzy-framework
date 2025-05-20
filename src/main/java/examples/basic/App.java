/**
 * Example application demonstrating the use of Jazzy Framework.
 * This class sets up routes and starts the server.
 */
package examples.basic;

import jazzyframework.core.Config;
import jazzyframework.core.Server;
import jazzyframework.routing.Router;

public class App 
{
    /**
     * Entry point for the example application.
     * Sets up routes for user and product operations and starts the server.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main( String[] args )
    {
        Config config = new Config();
        config.setEnableMetrics(true); // "/metrics" endpoint is automatically added
        config.setServerPort(8088);

        Router router = new Router();
        
        // User routes
        router.GET("/users/{id}", "getUserById", UserController.class);
        router.GET("/users", "getAllUsers", UserController.class);
        router.POST("/users", "createUser", UserController.class);
        router.PUT("/users/{id}", "updateUser", UserController.class);
        router.DELETE("/users/{id}", "deleteUser", UserController.class);
        router.POST("/users/with-rules", "createUserWithRules", UserController.class);
        
        // Product routes
        router.GET("/products/{id}", "getProduct", ProductController.class);
        router.GET("/products", "listProducts", ProductController.class);
        router.POST("/products", "createProduct", ProductController.class);
        router.PUT("/products/{id}", "updateProduct", ProductController.class);
        router.DELETE("/products/{id}", "deleteProduct", ProductController.class);
        router.POST("/products/with-rules", "createProductWithRules", ProductController.class);
        
        Server server = new Server(router, config);
        server.start(config.getServerPort());
    }
} 