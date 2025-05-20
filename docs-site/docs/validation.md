# Validation

Jazzy Framework provides a powerful and flexible validation system that allows you to validate request data easily.

## Basic Validation

You can use the `validator()` method on the `Request` object to validate incoming data:

```java
ValidationResult result = request.validator()
    .field("name").required().minLength(3).maxLength(50)
    .field("email").required().email()
    .field("age").required().min(18).max(100)
    .validate();

if (!result.isValid()) {
    return response().json(
        "status", "error",
        "message", "Validation failed",
        "errors", result.getAllErrors()
    ).status(400);
}
```

## Available Validation Rules

Jazzy's validation system provides many common validation rules:

| Rule | Description |
|------|-------------|
| `required()` | The field must be present and not empty |
| `email()` | The field must be a valid email address |
| `min(value)` | The field must be greater than or equal to the given value |
| `max(value)` | The field must be less than or equal to the given value |
| `minLength(length)` | The field must have at least the given length |
| `maxLength(length)` | The field must not exceed the given length |
| `in(values...)` | The field must be one of the given values |
| `notIn(values...)` | The field must not be one of the given values |
| `pattern(regex)` | The field must match the given regular expression |
| `pattern(regex, message)` | The field must match the given regular expression with a custom error message |

## Using Validation Rules Classes

For more complex validation requirements, you can create reusable validation rule classes:

```java
public class UserCreateRules extends ValidationRules {
    
    public UserCreateRules() {
        field("name").required().minLength(3).maxLength(50);
        field("email").required().email();
        field("password").required().minLength(8)
            .pattern("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$", 
                "Password must contain at least one uppercase letter, one lowercase letter, and one number");
        field("role").in("admin", "user", "editor");
    }
}
```

Then use them in your controller:

```java
public Response createUser(Request request) {
    ValidationResult result = request.validate(new UserCreateRules());
    
    if (!result.isValid()) {
        return response().json(
            "status", "error",
            "message", "Validation failed",
            "errors", result.getAllErrors()
        ).status(400);
    }
    
    // Validation successful, continue processing...
}
```

## Working with Validation Results

The `ValidationResult` class provides methods to work with validation results:

```java
ValidationResult result = request.validator()
    .field("name").required()
    .field("email").required().email()
    .validate();

// Check if validation passed
if (result.isValid()) {
    // All fields are valid
}

// Get all errors
Map<String, List<String>> allErrors = result.getAllErrors();

// Get errors for a specific field
List<String> nameErrors = result.getErrors("name");

// Check if a specific field has errors
boolean hasEmailErrors = result.hasErrors("email");
```

## Custom Validation Messages

You can provide custom validation messages for specific rules:

```java
ValidationResult result = request.validator()
    .field("username")
        .required("A username is required")
        .minLength(5, "Username must be at least 5 characters")
    .field("age")
        .required("Please provide your age")
        .min(18, "You must be at least 18 years old")
    .validate();
```

## Next Steps

For more information about working with HTTP requests and responses, see:

- [HTTP Requests](requests.md)
- [HTTP Responses](responses.md)
- [JSON Operations](json.md) 