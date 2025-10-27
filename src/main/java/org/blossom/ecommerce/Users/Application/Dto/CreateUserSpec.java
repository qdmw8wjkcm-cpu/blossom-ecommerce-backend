package org.blossom.ecommerce.Users.Application.Dto;

import org.blossom.ecommerce.Users.Domain.ValueObjects.*;

import java.util.Set;

public record CreateUserSpec(
        String name,
        String lastname,
        String password,
        Email email,
        DocumentId document,
        PhoneNumber phoneNumber,
        String city,
        String country,
        Set<CategoryId> favoriteCategories,
        Set<ProductId> favoriteProducts
) {}