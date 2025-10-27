package org.blossom.ecommerce.Users.Application.Dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewUser {

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 100, message = "Lastname cannot exceed 100 characters")
    private String lastname;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

    private String document;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number format is invalid")
    private String phoneNumber;

    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;

    private Set<String> favoriteCategoriesList;

    private Set<String> favoriteProductsList;

}