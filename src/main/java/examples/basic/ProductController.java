/**
 * Example controller for product operations.
 * Demonstrates validation, parameter handling, and response creation.
 */
package examples.basic;

import static jazzyframework.http.ResponseFactory.response;
import java.util.HashMap;
import java.util.Map;

import jazzyframework.http.JSON;
import jazzyframework.http.Request;
import jazzyframework.http.Response;
import jazzyframework.http.validation.ValidationResult;
import examples.basic.validation.ProductCreateRules;

public class ProductController {
    
    /**
     * Gets a product by ID.
     * 
     * @param request The HTTP request
     * @return A Response object with product data
     */
    public Response getProduct(Request request) {
        String id = request.path("id");
        
        return response().json(
            "id", id,
            "name", "Premium Headphones",
            "description", "Noise-canceling wireless headphones",
            "price", 199.99,
            "inStock", true
        );
    }

    /**
     * Lists products with pagination support.
     * 
     * @param request The HTTP request
     * @return A Response object with a list of products
     */
    public Response listProducts(Request request) {
        int page = request.queryInt("page", 1);
        int limit = request.queryInt("limit", 10);
        String category = request.query("category", "all");
        boolean inStock = request.queryBoolean("in_stock", false);
        
        Map<String, Object> filters = new HashMap<>();
        filters.put("category", category);
        filters.put("inStock", inStock);
        
        // Sample product list
        return response().json(
            "products", JSON.array(
                JSON.of(
                    "id", "prod-1",
                    "name", "Wireless Earbuds",
                    "price", 89.99
                ),
                JSON.of(
                    "id", "prod-2",
                    "name", "Smart Watch",
                    "price", 249.99
                ),
                JSON.of(
                    "id", "prod-3",
                    "name", "Bluetooth Speaker",
                    "price", 129.99
                )
            ),
            "total", 3,
            "page", page,
            "limit", limit,
            "filters", filters
        );
    }

    /**
     * Creates a new product with validation.
     * 
     * @param request The HTTP request
     * @return A Response object with the created product
     */
    public Response createProduct(Request request) {
        // Validate the request
        ValidationResult result = request.validator()
            .field("name").required().minLength(3).maxLength(100)
            .field("description").required().minLength(10)
            .field("price").required().numeric().min(0.01)
            .field("category").required().in("electronics", "clothing", "books", "home", "sports")
            .field("sku").required().pattern("^[A-Z]{2}\\d{4}$", "SKU must be in format XX0000")
            .validate();
        
        if (!result.isValid()) {
            return response().json(
                "status", "error",
                "message", "Validation failed",
                "errors", result.getAllErrors()
            ).status(400);
        }
        
        // Extract fields from the validated request
        Map<String, Object> productData = request.parseJson();
        
        // Generate a new ID (would be handled by database in real app)
        String newId = "prod-" + System.currentTimeMillis();
        productData.put("id", newId);
        
        return response().success("Product created successfully", productData).status(201);
    }
    
    /**
     * Updates a product with validation.
     * 
     * @param request The HTTP request
     * @return A Response object confirming the update
     */
    public Response updateProduct(Request request) {
        String id = request.path("id");
        
        // Validate the request - note that fields aren't required for updates
        ValidationResult result = request.validator()
            .field("name").minLength(3).maxLength(100)
            .field("description").minLength(10)
            .field("price").numeric().min(0.01)
            .field("category").in("electronics", "clothing", "books", "home", "sports")
            .field("sku").pattern("^[A-Z]{2}\\d{4}$", "SKU must be in format XX0000")
            .validate();
        
        if (!result.isValid()) {
            return response().json(
                "status", "error",
                "message", "Validation failed",
                "errors", result.getAllErrors()
            ).status(400);
        }
        
        // Extract fields from the validated request
        Map<String, Object> productData = request.parseJson();
        productData.put("id", id);
        
        return response().success("Product updated successfully", productData);
    }
    
    /**
     * Deletes a product.
     * 
     * @param request The HTTP request
     * @return A Response object confirming the deletion
     */
    public Response deleteProduct(Request request) {
        String id = request.path("id");
        
        return response().success("Product deleted successfully", 
            JSON.of("id", id)
        );
    }
    
    /**
     * Creates a product with ValidationRules.
     * Shows how to use a separate validation rules class.
     * 
     * @param request The HTTP request
     * @return A Response with the created product
     */
    public Response createProductWithRules(Request request) {
        ValidationResult result = request.validate(new ProductCreateRules());
        
        if (!result.isValid()) {
            return response().json(
                "status", "error",
                "message", "Validation failed",
                "errors", result.getAllErrors()
            ).status(400);
        }
        
        Map<String, Object> productData = request.parseJson();
        String newId = "prod-" + System.currentTimeMillis();
        productData.put("id", newId);
        
        return response().success("Product created successfully", productData).status(201);
    }
} 