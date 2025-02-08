Let me explain the key JWT terminology and components:

1. **Claims** - These are statements about an entity (user) and additional metadata:

Standard Claims (Registered Claims):
```json
{
    "sub": "1234567890",    // Subject (user identifier)
    "iat": 1516239022,      // Issued At (timestamp when token was generated)
    "exp": 1516242622,      // Expiration Time (when token expires)
    "iss": "auth-service",  // Issuer (who created the token)
    "aud": "web-app",       // Audience (intended recipient)
    "nbf": 1516239022,      // Not Before (token not valid before this time)
    "jti": "unique-id"      // JWT ID (unique identifier for the token)
}
```

2. **Types of Claims**:
```java
// 1. Private Claims - Custom claims for your application
claims.put("roles", ["ROLE_USER", "ROLE_ADMIN"]);
claims.put("userId", "12345");

// 2. Public Claims - Registered in IANA JSON Web Token Registry
claims.put("name", "John Doe");
claims.put("email", "john@example.com");

// 3. Registered Claims - Predefined claims (shown above)
.setSubject(username)        // sub
.setIssuedAt(new Date())    // iat
.setExpiration(expDate)     // exp
```

3. **Header** - Contains metadata about the token:
```json
{
    "alg": "HS512",  // Algorithm used for signature
    "typ": "JWT",    // Type of token
    "cty": "JWT"     // Content Type (optional)
}
```

4. **Implementation in JwtTokenUtil**:
```java
public String generateToken(UserDetails userDetails) {
    // Create custom claims
    Map<String, Object> claims = new HashMap<>();
    claims.put("roles", userDetails.getAuthorities());
    
    return Jwts.builder()
        // Registered claims
        .setSubject(userDetails.getUsername())     // Who the token refers to
        .setIssuedAt(new Date())                  // When token was issued
        .setExpiration(expiryDate)                // When token expires
        .setIssuer("your-auth-service")          // Who issued the token
        
        // Custom claims
        .setClaims(claims)                        // Add custom claims
        
        // Sign and build
        .signWith(getSigningKey(), SignatureAlgorithm.HS512)
        .compact();
}

// Extracting claims
public String getSubjectFromToken(String token) {
    return getClaimsFromToken(token).getSubject();
}

public Date getExpirationFromToken(String token) {
    return getClaimsFromToken(token).getExpiration();
}

public List<String> getRolesFromToken(String token) {
    return getClaimsFromToken(token).get("roles", List.class);
}
```

5. **Storage and Security**:
```java
// Claims should be kept minimal for security
public String createMinimalToken(String username) {
    return Jwts.builder()
        .setSubject(username)        // Only essential information
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(key)
        .compact();
}

// Don't store sensitive information in claims
// BAD:
claims.put("password", userPassword);  // Never do this
claims.put("creditCard", cardNumber);  // Never do this

// GOOD:
claims.put("permissions", ["read", "write"]);
claims.put("userType", "premium");
```

6. **Token Structure**:
```
header.payload.signature
|                      |
eyJhbGciOiJIUzI1....
```

Best Practices:
- Keep tokens small (minimize claims)
- Use standard claims when possible
- Don't store sensitive data in claims
- Set appropriate expiration times
- Consider using refresh tokens for long-term access

Would you like me to explain any of these concepts in more detail?