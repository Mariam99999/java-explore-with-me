package com.example.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewUserRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String email;

}
