package com.bgauction.bidservice.service;

import com.bgauction.bidservice.model.entity.Bid;

import java.util.List;
import java.util.Optional;

public interface BidService {
    Bid saveBid(Bid bid);
    List<Bid> findAllBidsByAuctionId(Long auctionId);
    List<Bid> findLastBidForEachAuctionByBidder(Long bidderId);
    Optional<Bid> findWinningBidByAuctionId(Long auctionId);
    void deleteAllBidsByAuctionId(Long auctionId);
}
