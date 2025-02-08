Let me break down the JWT token generation process with the actual code. This happens in the `JwtTokenUtil` class:

```java
public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("roles", userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()));
    
    return Jwts.builder()  // Main builder for JWT
            .setClaims(claims)  // Set custom claims (roles)
            .setSubject(userDetails.getUsername())  // Set subject (username)
            .setIssuedAt(new Date(System.currentTimeMillis()))  // Set issued time
            .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))  // Set expiry
            .signWith(getSigningKey(), SignatureAlgorithm.HS512)  // Sign with HMAC-SHA512
            .compact();  // Build final token
}

// Get the signing key
private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
}
```

Step by step internal process:

1. **Create Header**
```java
// Internal JJWT process
{
    "alg": "HS512",  // Algorithm used
    "typ": "JWT"     // Type of token
}
// Gets Base64URL encoded
// Result: eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9
```

2. **Create Payload (Claims)**
```java
// Custom claims added
claims.put("roles", ["ROLE_USER"]);

// Standard claims added by JJWT
{
    "sub": "username",              // from setSubject()
    "iat": 1706745600,             // from setIssuedAt()
    "exp": 1706832000,             // from setExpiration()
    "roles": ["ROLE_USER"]         // from setClaims()
}
// Gets Base64URL encoded
// Result: eyJzdWIiOiJ1c2VyMTIzIiwiaWF0IjoxNzA2NzQ1NjAwLCJleHAiOjE3MDY4MzIwMDAsInJvbGVzIjpbIlJPTEVfVVNFUiJdfQ
```

3. **Create Signature**
```java
// JJWT internal process
HMAC512(
    base64UrlEncode(header) + "." + base64UrlEncode(payload),
    secret
)
// Result: ZOeHtUs9J4K4...
```

4. **Combine All Parts**
```java
finalToken = base64UrlEncode(header) + "." + 
            base64UrlEncode(payload) + "." + 
            base64UrlEncode(signature)
// Result: eyJhbGciOiJIUzUxMiIs....ZOeHtUs9J4K4
```

The key JJWT methods involved:
```java
// Main builder methods
Jwts.builder()            // Creates JWT builder
    .setClaims()          // Sets claims
    .setSubject()         // Sets subject
    .setIssuedAt()        // Sets issued time
    .setExpiration()      // Sets expiration
    .signWith()           // Signs with algorithm
    .compact()            // Builds final token

// Key generation methods
Keys.hmacShaKeyFor()      // Creates signing key
Decoders.BASE64.decode()  // Decodes base64 secret
```

Want me to show you how to generate a token with specific claims or a different structure?