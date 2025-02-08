package org.training.user.service.service;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.training.user.service.model.dto.AuthRequest;
import org.training.user.service.model.dto.RegisterRequest;
import org.training.user.service.model.entity.User;
import org.training.user.service.repository.UserRepository;
import org.training.user.service.utils.JwtTokenUtil;

@Service
public class AuthenticationService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private AuthenticationManager authenticationManager;

  public String register(RegisterRequest request) {
    var user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRoles(List.of("ROLE_USER"));

    userRepository.save(user);

    return jwtTokenUtil.generateToken(user);
  }

  public String authenticate(AuthRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        )
    );

    var user = userRepository.findByUsername(request.getUsername())
        .orElseThrow();

    return jwtTokenUtil.generateToken(user);
  }
}
