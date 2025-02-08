Let me clarify how the signature is generated and encoded:

1. **First Process: HMAC-SHA512 Generation**
```java
HMAC512(
    base64UrlEncode(header) + "." + base64UrlEncode(payload),
    secret
)
```
This generates a byte array of raw HMAC-SHA512 hash (64 bytes/512 bits)
Example bytes: [90, 79, 101, 72, 116, 85, 115, 57 ...]

2. **Second Process: Base64URL Encoding**
```java
base64UrlEncode(signature)
```
This takes the HMAC-SHA512 byte array and encodes it to Base64URL format

They appear same because:
```java
// Internal process
byte[] signatureBytes = HMAC512(message, secret);  // Raw bytes
String encodedSignature = base64UrlEncode(signatureBytes);  // Convert to string

// Example
Raw bytes: [90, 79, 101, 72, 116, 85, 115, 57 ...]
Base64URL: "ZOeHtUs9J4K4..."
```

Let's see with actual code:
```java
// What happens inside JJWT
public String generateToken(UserDetails userDetails) {
    // 1. Create and encode header
    String encodedHeader = base64UrlEncode(header);  // eyJhbGc...
    
    // 2. Create and encode payload
    String encodedPayload = base64UrlEncode(payload);  // eyJzdWI...
    
    // 3. Create signature
    String message = encodedHeader + "." + encodedPayload;
    byte[] signatureBytes = HMAC512(message.getBytes(), secret);
    
    // 4. Encode signature
    String encodedSignature = base64UrlEncode(signatureBytes);  // ZOeHtUs...
    
    // 5. Combine all parts
    return encodedHeader + "." + encodedPayload + "." + encodedSignature;
}
```

Why this process?
- Raw HMAC-SHA512 output is binary (bytes)
- JWT needs all parts in string format
- Base64URL encoding converts bytes to URL-safe string
- The final token needs to be transmitted in HTTP headers

So when you see `ZOeHtUs9J4K4`, it's the Base64URL representation of the raw HMAC-SHA512 bytes. The bytes themselves are the actual signature, but we need to encode them to include in the JWT string.