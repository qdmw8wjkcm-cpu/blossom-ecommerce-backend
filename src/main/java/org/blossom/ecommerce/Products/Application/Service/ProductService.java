package org.blossom.ecommerce.Products.Application.Service;

import lombok.RequiredArgsConstructor;
import org.blossom.ecommerce.Products.Domain.Enums.ProductMessage;
import org.blossom.ecommerce.Products.Domain.Exceptions.DomainValidationException;
import org.blossom.ecommerce.Products.Domain.Models.Product;
import org.blossom.ecommerce.Products.Domain.Ports.In.ProductCommandUseCase;
import org.blossom.ecommerce.Products.Domain.Ports.In.ProductQueryUseCase;
import org.blossom.ecommerce.Products.Domain.Spec.*;
import org.blossom.ecommerce.Products.Domain.ValueObjects.CategoryId;
import org.blossom.ecommerce.Products.Domain.ValueObjects.ProductId;
import org.blossom.ecommerce.Products.Infrastructure.Ports.Out.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductCommandUseCase, ProductQueryUseCase {

    private final ProductRepository repo;

    @Override
    public Optional<Product> create(CreateProductSpec spec) {
        if (spec.title() == null || spec.title().isBlank()) {
            throw new DomainValidationException(ProductMessage.EMPTY_TITLE.getMessage());
        }
        if (spec.price() == null || spec.price().compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainValidationException(ProductMessage.INVALID_PRICE.getMessage());
        }
        if (spec.category() == null) {
            throw new DomainValidationException(ProductMessage.EMPTY_CATEGORY.getMessage());
        }

        Product product = Product.create(
                spec.title(),
                spec.description(),
                spec.price(),
                spec.category(),
                spec.stock(),
                spec.country(),
                spec.city()
                );

        Product saved = repo.save(product);
        return Optional.of(saved);
    }

    @Override
    public Product update(UpdateProduct cmd) {
        Product current = repo.findById(cmd.id())
                .orElseThrow(() -> new DomainValidationException(ProductMessage.PRODUCT_NOT_FOUND.getMessage()));

        current.updateFrom(cmd);

        return repo.save(current);
    }

    @Override
    public void delete(ProductId id) {
        Product current = repo.findById(id)
                .orElseThrow(() -> new DomainValidationException(ProductMessage.PRODUCT_NOT_FOUND.getMessage()));

        current.delete();
        repo.save(current);

    }

    @Override
    public Product activateDiscount(ActivateDiscount cmd) {
        var p = repo.findById(cmd.id())
                .orElseThrow(() -> new DomainValidationException(ProductMessage.PRODUCT_NOT_FOUND.getMessage()));

        p.activateDiscount(cmd.percentage(), cmd.amount(), cmd.since(), cmd.until());
        return repo.save(p);
    }

    @Override
    public Product deactivateDiscount(ProductId id) {
        var p = repo.findById(id)
                .orElseThrow(() -> new DomainValidationException(ProductMessage.PRODUCT_NOT_FOUND.getMessage()));

        p.clearDiscount();
        return repo.save(p);
    }

    @Override
    public Product rate(RateProduct cmd) {
        var p = repo.findById(cmd.id())
                .orElseThrow(() -> new DomainValidationException(ProductMessage.PRODUCT_NOT_FOUND.getMessage()));

        p.setRating(cmd.rating());
        return repo.save(p);
    }

    @Override
    public Product addComment(AddComment cmd) {
        var p = repo.findById(cmd.id())
                .orElseThrow(() -> new DomainValidationException(ProductMessage.PRODUCT_NOT_FOUND.getMessage()));

        p.addComment(cmd.comment());
        return repo.save(p);
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        return repo.findById(id);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public Page<Product> findByCategory(CategoryId categoryId, Pageable pageable) {
        return repo.findByCategory(categoryId, pageable);
    }

    @Override
    public Page<Product> findByPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable) {
        var lower = (min != null) ? min : BigDecimal.ZERO;
        var upper = (max != null) ? max : new BigDecimal("999999999");
        return repo.findByPriceBetween(lower, upper, pageable);
    }

    @Override
    public Page<Product> findByTitleContains(String title, Pageable pageable) {
        var q = (title == null) ? "" : title.trim();
        return repo.findByTitleContains(q, pageable);
    }

    @Override
    public Page<Product> findByRatingAtLeast(Double rating, Pageable pageable) {
        double r = (rating == null) ? 0.0 : rating;
        return repo.findByRatingAtLeast(r, pageable);
    }

    @Override
    public Page<Product> findDiscounted(Pageable pageable) {
        return repo.findDiscounted(pageable);
    }

    @Override
    public Page<Product> findByCountry(String country, Pageable pageable) {
        var c = (country == null) ? "" : country.trim();
        return repo.findByCountry(c, pageable);
    }

    @Override
    public Page<Product> findByCity(String city, Pageable pageable) {
        var c = (city == null) ? "" : city.trim();
        return repo.findByCity(c, pageable);
    }
}