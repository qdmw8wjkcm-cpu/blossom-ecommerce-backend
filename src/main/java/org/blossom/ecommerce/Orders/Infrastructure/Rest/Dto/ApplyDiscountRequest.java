package org.blossom.ecommerce.Orders.Infrastructure.Rest.Dto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;

@Schema(name = "ApplyDiscountRequest")
public record ApplyDiscountRequest(
        Long percentage,           
        BigDecimal amount,         
        Instant since,             
        Instant until              
) {}