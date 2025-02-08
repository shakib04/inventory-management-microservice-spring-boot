Let me break down the JWT token validation process step by step:

1. **Token Arrives in Request**
```java
// In JwtAuthenticationFilter
String authHeader = request.getHeader("Authorization");
// "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMTIzIn0.abc123signature"

if (authHeader != null && authHeader.startsWith("Bearer ")) {
    jwt = authHeader.substring(7); // Extract token part
}
```

2. **Token is Split into Three Parts**
```java
// Inside JJWT library
String[] parts = token.split("\\.");
String headerBase64 = parts[0];    // "eyJhbGciOiJIUzUxMiJ9"
String payloadBase64 = parts[1];   // "eyJzdWIiOiJ1c2VyMTIzIn0"
String receivedSignature = parts[2]; // "abc123signature"
```

3. **Decode Header and Payload**
```java
// Decode base64url to JSON
String header = base64UrlDecode(headerBase64);
// Result: {"alg":"HS512","typ":"JWT"}

String payload = base64UrlDecode(payloadBase64);
// Result: {"sub":"user123","iat":1234567890}
```

4. **Calculate Expected Signature**
```java
// Using server's secret key
String dataToSign = headerBase64 + "." + payloadBase64;
byte[] calculatedSignature = HMAC_SHA512(dataToSign.getBytes(), secretKey);
String calculatedSignatureBase64 = base64UrlEncode(calculatedSignature);
```

5. **Compare Signatures**
```java
boolean isValid = constantTimeEqual(receivedSignature, calculatedSignatureBase64);
// Uses constant-time comparison to prevent timing attacks
```

Here's the actual code flow in your implementation:

```java
// 1. JwtAuthenticationFilter intercepts request
@Override
protected void doFilterInternal(HttpServletRequest request, 
                              HttpServletResponse response, 
                              FilterChain filterChain) {
    // Extract token
    final String jwt = extractJwtFromHeader(request);
    
    try {
        // 2. Get username from token (this validates signature)
        String username = jwtTokenUtil.getUsernameFromToken(jwt);
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 3. Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            // 4. Validate token
            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                // Token is valid - set authentication
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, 
                        userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
    } catch (SignatureException e) {
        // Signature validation failed
        logger.error("Invalid JWT signature");
    } catch (ExpiredJwtException e) {
        // Token has expired
        logger.error("JWT token is expired");
    }
}

// In JwtTokenUtil
public String getUsernameFromToken(String token) {
    return getClaimsFromToken(token).getSubject();
}

private Claims getClaimsFromToken(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)  // This is where validation happens
            .getBody();
}

public boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
}
```

So in summary, validation involves:
1. Checking if signature matches (using HMAC-SHA512)
2. Verifying token hasn't expired
3. Verifying username in token matches UserDetails
4. All claims in the token are validated

If any of these steps fail:
- Invalid signature → SignatureException
- Expired token → ExpiredJwtException
- Username mismatch → Authentication fails
- Invalid claims → Authentication fails

The crucial security aspects are:
- Signature verification is done with server's secret key
- Original secret key cannot be derived from token
- All parts of token (header, payload, signature) are needed for validation
- Any modification to any part invalidates the token