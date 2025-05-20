/**
 * Validation rules for creating a product.
 */
package examples.basic.validation;

import jazzyframework.http.validation.ValidationRules;

public class ProductCreateRules extends ValidationRules {
    
    /**
     * Creates a new ProductCreateRules instance and sets up validation rules.
     */
    public ProductCreateRules() {
        field("name").required().minLength(3).maxLength(100);
        field("description").required().minLength(10);
        field("price").required().numeric().min(0.01);
        field("category").required().in("electronics", "clothing", "books", "home", "sports");
        field("sku").required().pattern("^[A-Z]{2}\\d{4}$", "SKU must be in format XX0000");
    }
} 