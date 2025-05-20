/**
 * Validation rules for creating a user.
 */
package examples.basic.validation;

import jazzyframework.http.validation.ValidationRules;

public class UserCreateRules extends ValidationRules {
    
    /**
     * Creates a new UserCreateRules instance and sets up validation rules.
     */
    public UserCreateRules() {
        field("name").required().minLength(3).maxLength(50);
        field("email").required().email();
        field("password").required().minLength(8)
            .pattern("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$", 
                "Password must contain at least one uppercase letter, one lowercase letter, and one number");
        field("role").in("admin", "user", "editor");
    }
} 