package org.training.user.service.model.dto;

import lombok.Data;

@Data
public class RegisterRequest {
  private String username;
  private String password;
  private String emailId;
}
