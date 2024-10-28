package com.bgauction.bidservice.feign;

import com.bgauction.bidservice.model.dto.AuctionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.math.BigDecimal;

@FeignClient(name = "AUCTIONSERVICE")
public interface AuctionClient {

    String URL_BASE = "/auction/{id}";

    @GetMapping(URL_BASE)
    ResponseEntity<AuctionDto> getAuctionById(@PathVariable Long id);

    @PutMapping(URL_BASE + "/price/{price}/winner/{winnerId}")
    ResponseEntity<Void> updateCurrentPriceAndWinner(
            @PathVariable Long id, @PathVariable BigDecimal price, @PathVariable Long winnerId);
}
