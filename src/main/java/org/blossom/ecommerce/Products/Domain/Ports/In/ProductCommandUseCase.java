package org.blossom.ecommerce.Products.Domain.Ports.In;

import org.blossom.ecommerce.Products.Domain.Models.Product;
import org.blossom.ecommerce.Products.Domain.Spec.*;

import org.blossom.ecommerce.Products.Domain.ValueObjects.CategoryId;
import org.blossom.ecommerce.Products.Domain.ValueObjects.ProductId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductCommandUseCase {
    Optional<Product> create(CreateProductSpec spec);
    Product update(UpdateProduct cmd);
    void delete(ProductId id);

    // Descuentos
    Product activateDiscount(ActivateDiscount cmd);
    Product deactivateDiscount(ProductId id);

    // Calificación y comentarios
    Product rate(RateProduct cmd);
    Product addComment(AddComment cmd);

    Page<Product> findByCategory(CategoryId categoryId, Pageable pageable);
}