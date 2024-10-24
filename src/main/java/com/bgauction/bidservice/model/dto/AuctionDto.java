package com.bgauction.bidservice.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AuctionDto {
    private Long id;

    @NotNull
    @Positive
    private Long sellerId;

    @NotNull
    @Positive
    private BigDecimal currentPrice;

    @NotNull
    private String status;
}
