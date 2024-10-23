package com.bgauction.bidservice.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BidDto {

    @NotNull
    @Positive
    private Long id;

    @NotNull
    @Positive
    private Long auctionId;

    @NotNull
    @Positive
    private Long bidderId;

    @NotNull
    @Positive
    private BigDecimal bidAmount;

    @NotNull
    private LocalDateTime bidTime;

    @NotNull
    @Builder.Default
    private Boolean isWinner = false;
}
