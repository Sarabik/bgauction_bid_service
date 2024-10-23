package com.bgauction.bidservice.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BidCreationDto {
    @NotNull
    @Positive
    private Long auctionId;

    @NotNull
    @Positive
    private Long bidderId;

    @NotNull
    @Positive
    private BigDecimal bidAmount;
}
