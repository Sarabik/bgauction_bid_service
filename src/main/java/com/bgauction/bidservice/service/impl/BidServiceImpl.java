package com.bgauction.bidservice.service.impl;

import com.bgauction.bidservice.model.entity.Bid;
import com.bgauction.bidservice.repository.BidRepository;
import com.bgauction.bidservice.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;

    @Override
    public Bid saveBid(Bid bid) {
        Optional<Bid> bidOptional = bidRepository.findWinningBidByAuctionId(bid.getAuctionId());
        // check if seller_id and bidder_id are not equal!
        if (bidOptional.isPresent()) {
            Bid winningBid = bidOptional.get();
            if (bid.getBidAmount().compareTo(winningBid.getBidAmount()) < 1) {
                throw new RuntimeException("New bid amount must be greater then amount of the previous one");
            } else if (bid.getBidderId().equals(winningBid.getBidderId())) {
                throw new RuntimeException("Bidder id of a new bid is equal to bidder id of the previous one");
            }
            winningBid.setIsWinner(false);
            bidRepository.save(winningBid);
        }
        bid.setIsWinner(true);
        return bidRepository.save(bid);
    }

    @Override
    public List<Bid> findAllBidsByAuctionId(Long auctionId) {
        return bidRepository.findByAuctionId(auctionId);
    }

    @Override
    public List<Bid> findLastBidForEachAuctionByBidder(Long bidderId) {
        return bidRepository.findLastBidForEachAuctionByBidder(bidderId);
    }

    @Override
    public Optional<Bid> findWinningBidByAuctionId(Long auctionId) {
        return bidRepository.findWinningBidByAuctionId(auctionId);
    }

    @Override
    public void deleteAllBidsByAuctionId(Long auctionId) {
        bidRepository.deleteByAuctionId(auctionId);
    }
}
