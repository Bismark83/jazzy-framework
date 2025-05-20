# ResponseFactory Class

Jazzy Framework includes a `ResponseFactory` class inspired by Laravel. This class makes it easier to create JSON responses in a more fluent way. This approach is similar to Laravel's `response()->json()` helper function.

## Basic Usage

The `ResponseFactory` class is accessed through the static `response()` method, which returns a new instance of the factory. From there, you can chain methods to create various types of responses.

```java
import static jazzyframework.http.ResponseFactory.response;

// In your controller method
public Response aMethod(Request request) {
    return response().json("status", "success", "message", "Operation completed");
}
```

## Available Methods

### json(Object data)

Creates a JSON response from an object:

```java
// Using a Map
Map<String, Object> data = new HashMap<>();
data.put("name", "Ahmet");
data.put("age", 30);
return response().json(data);

// Using a POJO
User user = new User();
user.setName("Ahmet");
user.setAge(30);
return response().json(user);
```

### json(Object... keyValues)

Creates a JSON response from key-value pairs:

```java
return response().json(
    "name", "Ahmet",
    "age", 30,
    "roles", Arrays.asList("admin", "user")
);
```

### success(String message)

Creates a success response with a message:

```java
return response().success("User created successfully");
```

Output:
```json
{
  "status": "success",
  "message": "User created successfully"
}
```

### success(String message, Object data)

Creates a success response with a message and data:

```java
User user = new User();
user.setId("123");
user.setName("Ahmet");
return response().success("User created successfully", user);
```

Output:
```json
{
  "status": "success",
  "message": "User created successfully",
  "data": {
    "id": "123",
    "name": "Ahmet"
  }
}
```

### Error Responses

For error responses, you can create a JSON response:

```java
return response().json(
    "status", "error",
    "message", "User not found"
).status(404);
```

Output:
```json
{
  "status": "error",
  "message": "User not found"
}
```

For validation errors:

```java
return response().json(
    "status", "error",
    "message", "Validation error",
    "errors", validationErrors
).status(400);
```

### Other Methods

The factory also includes methods for other response types:

```java
// Text response
return response().text("Plain text content");

// HTML response
return response().html("<h1>Hello World</h1>");

// Redirect
return response().redirect("/another-url");
```

## Comparison with Laravel

How to do the same thing with Laravel (PHP) and Jazzy (Java):

### Laravel (PHP)
```php
// Basic JSON response
return response()->json([
    'status' => 'success',
    'message' => 'Operation successful'
]);

// Returning an Eloquent model
$user = User::find(1);
return response()->json($user);
```

### Jazzy (Java)
```java
// Basic JSON response
return response().json(
    "status", "success",
    "message", "Operation successful"
);

// Returning a model/POJO
User user = userRepository.findById("1");
return response().json(user);
```

## Examples

You can see how to use ResponseFactory in the following examples:

```java
// Basic JSON response
public Response example1(Request request) {
    return response().json(
        "id", 1,
        "name", "Hello",
        "active", true
    );
}

// Success response
public Response example2(Request request) {
    return response().success("Operation successful");
}

// Veri içeren başarı yanıtı
public Response example3(Request request) {
    Map<String, Object> userData = new HashMap<>();
    userData.put("id", 123);
    userData.put("name", "Ahmet");
    
    return response().success("Kullanıcı oluşturuldu", userData);
}

// Hata yanıtı
public Response example4(Request request) {
    return response().json(
        "status", "error",
        "message", "Kaynak bulunamadı"
    ).status(404);
}

// Kompleks yanıt
public Response example5(Request request) {
    return response().json(
        "users", JSON.array(
            JSON.of("id", 1, "name", "Ahmet"),
            JSON.of("id", 2, "name", "Mehmet"),
            JSON.of("id",
            3, "name", "Ayşe")
        ),
        "total", 3,
        "page", 1
    );
} 