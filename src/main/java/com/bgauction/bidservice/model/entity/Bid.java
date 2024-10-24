package com.bgauction.bidservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "bids")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Positive
    @Column(name = "auction_id", updatable = false)
    private Long auctionId;

    @NotNull
    @Positive
    @Column(name = "bidder_id", updatable = false)
    private Long bidderId;

    @NotNull
    @Positive
    @Column(name = "bid_amount")
    private BigDecimal bidAmount;

    @CreationTimestamp
    @Column(name = "bid_time", updatable = false)
    private LocalDateTime bidTime;

    @NotNull
    @Builder.Default
    @Column(name = "is_winner")
    private Boolean isWinner = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return Objects.equals(id, bid.id) && Objects.equals(auctionId, bid.auctionId)
                && Objects.equals(bidderId, bid.bidderId) && Objects.equals(bidAmount, bid.bidAmount)
                && Objects.equals(bidTime, bid.bidTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, auctionId, bidderId, bidAmount, bidTime);
    }
}
