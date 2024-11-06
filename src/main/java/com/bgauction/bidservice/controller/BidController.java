package com.bgauction.bidservice.controller;

import com.bgauction.bidservice.model.dto.BidCreationDto;
import com.bgauction.bidservice.model.dto.BidDto;
import com.bgauction.bidservice.model.entity.Bid;
import com.bgauction.bidservice.model.mapper.BidMapper;
import com.bgauction.bidservice.service.BidService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;
    private final BidMapper bidMapper;
    private static final String AUCTION_ID_GREATER_THEN_0 = "Auction id must be greater then 0";
    private static final String BIDDER_ID_GREATER_THEN_0 = "Bidder id must be greater then 0";

    @PostMapping("/bid")
    public ResponseEntity<?> createBid(@Valid @RequestBody BidCreationDto bidDto,
                                       @RequestHeader(value = "X-User-Id") Long userId) {
        if (!userId.equals(bidDto.getBidderId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        BidDto savedBid = bidMapper.bidToBidDto(bidService.saveBid(bidMapper.bidCreationDtoToBid(bidDto)));
        return ResponseEntity.status(201).body(savedBid);
    }

    @GetMapping("/bid/auction/{auctionId}")
    public ResponseEntity<?> getBidsByAuctionId(@PathVariable Long auctionId) {
        if (auctionId < 1) {
            return new ResponseEntity<>(AUCTION_ID_GREATER_THEN_0, HttpStatus.BAD_REQUEST);
        }
        List<Bid> bids = bidService.findAllBidsByAuctionId(auctionId);
        return ResponseEntity.ok(bids);
    }

    @GetMapping("/bid/bidder/{bidderId}/last")
    public ResponseEntity<?> getLastBidForEachAuction(@PathVariable Long bidderId,
                                                      @RequestHeader(value = "X-User-Id") Long userId) {
        if (!userId.equals(bidderId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (bidderId < 1) {
            return new ResponseEntity<>(BIDDER_ID_GREATER_THEN_0, HttpStatus.BAD_REQUEST);
        }
        List<Bid> bids = bidService.findLastBidForEachAuctionByBidder(bidderId);
        return ResponseEntity.ok(bids);
    }

    @DeleteMapping("/internal/bid/auction/{auctionId}")
    public ResponseEntity<?> deleteBidsByAuctionId(@PathVariable Long auctionId) {
        if (auctionId < 1) {
            return new ResponseEntity<>(AUCTION_ID_GREATER_THEN_0, HttpStatus.BAD_REQUEST);
        }
        bidService.deleteAllBidsByAuctionId(auctionId);
        return ResponseEntity.noContent().build();
    }

}
