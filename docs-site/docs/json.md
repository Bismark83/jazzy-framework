# JSON Operations

Jazzy provides easy creation and processing of JSON data format for modern web APIs. This document details the framework's JSON processing capabilities.

## Creating JSON Data

Jazzy offers various methods for creating JSON data:

### Creating JSON with ResponseFactory

The ResponseFactory class allows you to create JSON using key-value pairs:

```java
import static jazzyframework.http.ResponseFactory.response;

public Response getUserData(Request request) {
    return response().json(
        "id", "123",
        "name", "Mehmet Kaya",
        "email", "mehmet@example.com",
        "active", true,
        "age", 35,
        "score", 95.8
    );
}
```

This method is ideal for simple JSON objects and is easy to read.

### Objects with the JSON Class

For more complex JSON structures or when you want to use JSON data outside of ResponseFactory, you can use the `JSON` class:

```java
import static jazzyframework.http.JSON.of;
import static jazzyframework.http.JSON.array;
import jazzyframework.http.JSON;

// Creating with JSON.of method
JSON userData = JSON.of(
    "id", "123",
    "name", "Mehmet Kaya",
    "email", "mehmet@example.com",
    "active", true
);

// Sending as a response
return response().json(userData);
```

### JSON Creation Methods

The JSON class provides several methods for creating various JSON structures:

#### 1. of() Method

Creates a JSON object from key-value pairs:

```java
import static jazzyframework.http.JSON.of;

JSON user = of(
    "id", "123",
    "name", "Ayşe Demir",
    "email", "ayse@example.com"
);
```

#### 2. create() and add() Method

For step-by-step JSON object creation:

```java
JSON user = JSON.create()
    .add("id", "456")
    .add("name", "Ali Yılmaz")
    .add("email", "ali@example.com")
    .add("active", true)
    .add("age", 29);
```

#### 3. array() Method

For creating JSON arrays:

```java
import static jazzyframework.http.JSON.array;

// Creating an array
List<Object> colors = array("red", "blue", "green", "yellow");

// Adding the array to a JSON object
JSON data = JSON.of("colors", colors);
```

## Complex JSON Structures

### Nested JSON Objects

The JSON class supports creating nested JSON objects:

```java
// Creating nested objects
JSON address = JSON.of(
    "street", "Atatürk Street No:123",
    "city", "Istanbul",
    "country", "Turkey",
    "postalCode", "34000"
);

JSON user = JSON.of(
    "id", "789",
    "name", "Zeynep Kara",
    "email", "zeynep@example.com",
    "address", address
);

// Response containing nested object
return response().json(user);
```

### Arrays and Complex Structures

For more complex structures, you can combine arrays and objects:

```java
// User with multiple addresses
JSON homeAddress = JSON.of(
    "type", "home",
    "street", "Bağdat Street No:45",
    "city", "Istanbul"
);

JSON workAddress = JSON.of(
    "type", "work",
    "street", "Levent Plaza No:12",
    "city", "Istanbul"
);

// Array of addresses
List<Object> addresses = JSON.array(homeAddress, workAddress);

// Array of phone numbers
List<Object> phoneNumbers = JSON.array(
    JSON.of("type", "mobile", "number", "+90555123456"),
    JSON.of("type", "work", "number", "+90212987654")
);

// Main user object
JSON user = JSON.of(
    "id", "101",
    "name", "Mustafa Çelik",
    "addresses", addresses,
    "phoneNumbers", phoneNumbers,
    "tags", JSON.array("premium", "verified", "customer")
);
```

## JSON Object Methods

### toMap()

Converts a JSON object to a standard Java Map:

```java
JSON user = JSON.of("name", "Deniz", "age", 27);
Map<String, Object> map = user.toMap();
```

### toString()

Returns the string representation of the JSON object (useful for debugging):

```java
JSON user = JSON.of("name", "Deniz", "age", 27);
String str = user.toString();
System.out.println(str);  // {name=Deniz, age=27}
```

## Common Use Cases

### HTTP Responses

JSON is commonly used especially in HTTP responses:

```java
// Success response
return response().json(
    "status", "success",
    "message", "Operation completed successfully",
    "data", user
);

// Error response
return response().json(
    "status", "error",
    "message", "User not found",
    "code", "USER_NOT_FOUND"
).status(404);

// Validation error
return response().json(
    "status", "error",
    "message", "Validation error",
    "errors", validationErrors
).status(400);
```

### Data Filtering

You can select only the necessary fields when creating JSON data:

```java
public Response getUserProfile(Request request) {
    User user = findUser(request.path("id"));
    
    // Filtering sensitive information
    return response().json(
        "id", user.getId(),
        "name", user.getName(),
        "email", user.getEmail(),
        "joinDate", user.getJoinDate()
        // sensitive fields like password, securityQuestion are not included
    );
}
```

## Special Cases About JSON

### Requirements of the JSON.of() Method

- Must be in key-value pairs (even number of arguments)
- All keys must be of String type

```java
// Correct usage
JSON.of("key1", value1, "key2", value2);

// Incorrect: odd number of arguments
JSON.of("key1", value1, "key2");  // Throws IllegalArgumentException

// Incorrect: non-string key
JSON.of(123, value1);  // Throws IllegalArgumentException
```

## Example: A Comprehensive API Response

```java
public Response getProductDetails(Request request) {
    String productId = request.path("id");
    
    // Get product information from database (for example)
    Product product = findProduct(productId);
    
    if (product == null) {
        return response().json(
            "status", "error", 
            "message", "Product not found"
        ).status(404);
    }
    
    // Category information
    JSON category = JSON.of(
        "id", product.getCategoryId(),
        "name", product.getCategoryName(),
        "path", product.getCategoryPath()
    );
    
    // Pricing information
    JSON pricing = JSON.of(
        "base", product.getBasePrice(),
        "discount", product.getDiscountAmount(),
        "final", product.getFinalPrice(),
        "currency", "TRY",
        "onSale", product.isOnSale()
    );
    
    // Images
    List<String> imageUrls = product.getImageUrls();
    
    // Variants
    List<Object> variants = new ArrayList<>();
    for (Variant variant : product.getVariants()) {
        variants.add(JSON.of(
            "id", variant.getId(),
            "name", variant.getName(),
            "stock", variant.getStock(),
            "attributes", JSON.of(
                "color", variant.getColor(),
                "size", variant.getSize()
            )
        ));
    }
    
    // Main response
    return response().json(
        "id", product.getId(),
        "name", product.getName(),
        "description", product.getDescription(),
        "category", category,
        "pricing", pricing,
        "images", imageUrls,
        "variants", variants,
        "rating", product.getRating(),
        "reviewCount", product.getReviewCount(),
        "inStock", product.isInStock(),
        "createdAt", product.getCreatedAt(),
        "updatedAt", product.getUpdatedAt()
    );
}
```

## Next Steps

- See the [HTTP Requests](requests.md) document for receiving and processing JSON request data
- Explore the [HTTP Responses](responses.md) document for creating JSON responses
- Check out the [Examples](examples.md) document for sample applications 