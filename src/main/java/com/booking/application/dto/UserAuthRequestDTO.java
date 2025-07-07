package com.booking.application.dto;

import java.util.Set;

import com.booking.application.entites.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthRequestDTO {
    private String username;
    private String password;
    private Set<Role> roles;
}
