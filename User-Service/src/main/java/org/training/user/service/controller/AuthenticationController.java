package org.training.user.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.training.user.service.model.dto.AuthRequest;
import org.training.user.service.model.dto.AuthResponse;
import org.training.user.service.model.dto.RegisterRequest;
import org.training.user.service.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
  @Autowired
  private AuthenticationService authService;

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
    String token = authService.register(request);
    return ResponseEntity.ok(new AuthResponse(token));
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
    String token = authService.authenticate(request);
    return ResponseEntity.ok(new AuthResponse(token));
  }
}
