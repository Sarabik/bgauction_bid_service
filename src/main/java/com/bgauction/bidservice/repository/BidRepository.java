package com.bgauction.bidservice.repository;

import com.bgauction.bidservice.model.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByAuctionId(Long auctionId);

    @Query("SELECT b FROM Bid b WHERE b.bidTime = (SELECT MAX(b2.bidTime) FROM Bid b2 WHERE b2.auctionId = b.auctionId AND b2.bidderId = :bidderId) AND b.bidderId = :bidderId")
    List<Bid> findLastBidForEachAuctionByBidder(@Param("bidderId") Long bidderId);

    @Query("SELECT b FROM Bid b WHERE b.auctionId = :auctionId AND b.isWinner = true")
    Optional<Bid> findWinningBidByAuctionId(@Param("auctionId") Long auctionId);

    @Transactional
    void deleteByAuctionId(Long auctionId);
}
