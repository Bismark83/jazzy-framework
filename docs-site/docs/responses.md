# HTTP Responses

In Jazzy, HTTP responses are created using the `Response` class and `ResponseFactory`. Thanks to the fluent API inspired by Laravel, different response types can be easily created.

## Response Object

The `Response` object contains the following properties:

- **Status Code**: HTTP status code (200, 201, 404, etc.)
- **Body**: Response body (JSON, text, HTML, etc.)
- **Content Type**: Response content type
- **Headers**: HTTP headers

## Using ResponseFactory

The `ResponseFactory` class is a helper class accessible via the `response()` static method. This class allows you to easily create different response types.

```java
import static jazzyframework.http.ResponseFactory.response;
```

### JSON Responses

There are several ways to create JSON responses:

#### JSON Response with an Object

```java
public Response getUser(Request request) {
    User user = findUser(request.path("id"));
    
    return response().json(user);
}
```

#### JSON Response with Key-Value Pairs

```java
public Response getUserById(Request request) {
    String id = request.path("id");
    
    return response().json(
        "id", id,
        "name", "Ahmet Yılmaz",
        "email", "ahmet@example.com",
        "age", 32,
        "active", true
    );
}
```

### Success Responses

You can use the `success()` method to create standard success responses:

```java
public Response createUser(Request request) {
    User user = request.toObject(User.class);
    user.setId(generateId());
    
    // Save the user
    
    return response().success("User created successfully");
}
```

Success response with data:

```java
public Response createUser(Request request) {
    User user = request.toObject(User.class);
    user.setId(generateId());
    
    // Save the user
    
    return response().success("User created successfully", user);
}
```

### Error Responses

For error responses, you can create JSON responses directly:

```java
public Response getUserById(Request request) {
    String id = request.path("id");
    User user = findUser(id);
    
    if (user == null) {
        return response().json(
            "status", "error",
            "message", "User not found"
        ).status(404);
    }
    
    return response().json(user);
}
```

Error with custom HTTP status code:

```java
public Response getUserById(Request request) {
    String id = request.path("id");
    
    if (!isValidId(id)) {
        return response().json(
            "status", "error",
            "message", "Invalid user ID"
        ).status(400);
    }
    
    User user = findUser(id);
    
    if (user == null) {
        return response().json(
            "status", "error",
            "message", "User not found"
        ).status(404);
    }
    
    return response().json(user);
}
```

### HTML Responses

You can use the `html()` method for HTML responses:

```java
public Response getUserProfile(Request request) {
    String id = request.path("id");
    User user = findUser(id);
    
    String html = "<html><body>" +
                  "<h1>User Profile: " + user.getName() + "</h1>" +
                  "<p>Email: " + user.getEmail() + "</p>" +
                  "</body></html>";
    
    return response().html(html);
}
```

### Text Responses

You can use the `text()` method for plain text responses:

```java
public Response getVersion(Request request) {
    return response().text("Jazzy Framework v1.0.0");
}
```

### Redirect Responses

You can use the `redirect()` method to redirect the user to another URL:

```java
public Response redirectToLogin(Request request) {
    return response().redirect("/login");
}
```

## HTTP Durum Kodları

Yanıtınızın HTTP durum kodunu ayarlamak için `status()` metodunu kullanabilirsiniz:

```java
public Response createResource(Request request) {
    // Kaynağı oluştur
    
    return response().json(
        "message", "Resource created",
        "id", newResourceId
    ).status(201); // 201 Created
}
```

## Özel Header'lar

Yanıtınıza özel header'lar eklemek için `header()` metodunu kullanabilirsiniz:

```java
public Response getCachedData(Request request) {
    // Verileri al
    
    return response().json(data)
        .header("Cache-Control", "max-age=3600")
        .header("X-API-Version", "1.0");
}
```

## Metod Zincirleme (Method Chaining)

Jazzy'nin fluent API'si, metod zincirlemesini destekler:

```java
public Response complexResponse(Request request) {
    return response().json(data)
        .status(201)
        .header("X-Custom-Header", "Value")
        .header("Cache-Control", "no-cache");
}
```

## Örnekler

### Standart CRUD Yanıtları

```java
// Create
public Response createUser(Request request) {
    Map<String, Object> userData = request.parseJson();
    String newId = generateId();
    userData.put("id", newId);
    
    // Kullanıcıyı kaydet
    
    return response().success("User created", userData).status(201);
}

// Read
public Response getUser(Request request) {
    String id = request.path("id");
    User user = findUser(id);
    
    if (user == null) {
        return response().json(
            "status", "error",
            "message", "User not found"
        ).status(404);
    }
    
    return response().json(user);
}

// Update
public Response updateUser(Request request) {
    String id = request.path("id");
    Map<String, Object> userData = request.parseJson();
    userData.put("id", id);
    
    // Kullanıcıyı güncelle
    
    return response().success("User updated", userData);
}

// Delete
public Response deleteUser(Request request) {
    String id = request.path("id");
    
    // Kullanıcıyı sil
    
    return response().success("User deleted");
}
```

### Doğrulama Hataları

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
    
    // Doğrulama başarılı, işleme devam et
    Map<String, Object> userData = request.parseJson();
    userData.remove("password"); // Hassas veriyi kaldır
    
    // Kullanıcıyı kaydet
    String newId = "user-" + System.currentTimeMillis();
    userData.put("id", newId);
    
    return response().success("User created successfully", userData).status(201);
}
```

### Farklı Yanıt Tipleri Kullanarak Kapsamlı Bir Örnek

```java
public Response processUserRequest(Request request) {
    String action = request.query("action", "view");
    String id = request.path("id");
    
    User user = findUser(id);
    if (user == null) {
        return response().json(
            "status", "error",
            "message", "User not found"
        ).status(404);
    }
    
    switch (action) {
        case "view":
            return response().json(user);
            
        case "view-html":
            return response().html("<h1>User: " + user.getName() + "</h1>");
            
        case "download":
            return response().text(user.toString())
                .header("Content-Disposition", "attachment; filename=\"user-" + id + ".txt\"");
            
        case "profile":
            return response().redirect("/users/" + id + "/profile");
            
        default:
            return response().json(
                "status", "error",
                "message", "Unknown action: " + action
            ).status(400);
    }
}
```

## Sonraki Adımlar

- Veri doğrulama hakkında daha fazla bilgi için [Doğrulama](validation.md) belgesine bakın
- JSON oluşturma ve işleme hakkında bilgi almak için [JSON İşlemleri](json.md) belgesini inceleyin
- Kapsamlı örnekler için [Örnekler](examples.md) belgesine göz atın 