package org.training.user.service.model.dto;

import lombok.Data;

@Data
public class AuthRequest {
  private String username;
  private String password;
}
