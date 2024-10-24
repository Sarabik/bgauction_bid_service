package com.bgauction.bidservice.feign;

import com.bgauction.bidservice.model.dto.AuctionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.math.BigDecimal;

@FeignClient(name = "auction-service", url = "localhost:8200/auction")
public interface AuctionClient {
    @GetMapping("/{id}")
    ResponseEntity<AuctionDto> getAuctionById(@PathVariable Long id);

    @PutMapping("/{id}/price/{price}/winner/{winnerId}")
    ResponseEntity<Void> updateCurrentPriceAndWinner(
            @PathVariable Long id, @PathVariable BigDecimal price, @PathVariable Long winnerId);
}
