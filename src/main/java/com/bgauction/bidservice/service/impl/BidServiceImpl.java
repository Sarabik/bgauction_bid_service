package com.bgauction.bidservice.service.impl;

import com.bgauction.bidservice.feign.AuctionClient;
import com.bgauction.bidservice.model.dto.AuctionDto;
import com.bgauction.bidservice.model.entity.Bid;
import com.bgauction.bidservice.repository.BidRepository;
import com.bgauction.bidservice.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final AuctionClient auctionClient;

    @Override
    public Bid saveBid(Bid bid) {
        Long auctionId = bid.getAuctionId();

        AuctionDto auction = getAuctionIfItExistsAndActive(auctionId);
        if (auction.getSellerId().equals(bid.getBidderId())) {
            throw new RuntimeException("Auction seller can't be a bidder. Id: " + bid.getBidderId());
        }
        if (bid.getBidAmount().compareTo(auction.getCurrentPrice()) < 1) {
            throw new RuntimeException("New bid amount must be greater then current price for the auction");
        }

        changeStatusOfLastWinningBid(bid);

        bid.setIsWinner(true);
        Bid savedBid = bidRepository.save(bid);

        updateAuctionCurrentPriceAndWinner(auctionId, bid.getBidAmount(), bid.getBidderId());

        return savedBid;
    }

    private AuctionDto getAuctionIfItExistsAndActive(Long auction_id) {
        ResponseEntity<AuctionDto> response = auctionClient.getAuctionById(auction_id);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw new RuntimeException("Auction with id " + auction_id + " not found");
        }
        AuctionDto auctionDto = response.getBody();
        if (auctionDto == null) {
            throw new RuntimeException("Auction with id " + auction_id + " not found");
        } else if (!auctionDto.getStatus().equals("ACTIVE")) {
            throw new RuntimeException("Auction with id " + auction_id + " is not active");
        }
        return auctionDto;
    }

    private void updateAuctionCurrentPriceAndWinner(Long auctionId, BigDecimal currentPrice, Long winnerId) {
        ResponseEntity<Void> response = auctionClient.updateCurrentPriceAndWinner(auctionId, currentPrice, winnerId);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw new RuntimeException("Current price and winner for auction with id " + auctionId + " are not updated");
        }
    }

    private void changeStatusOfLastWinningBid(Bid newBid) {
        Optional<Bid> bidOptional = bidRepository.findWinningBidByAuctionId(newBid.getAuctionId());
        if (bidOptional.isPresent()) {
            Bid winningBid = bidOptional.get();
            if (newBid.getBidAmount().compareTo(winningBid.getBidAmount()) < 1) {
                throw new RuntimeException("New bid amount must be greater then amount of the previous one");
            } else if (newBid.getBidderId().equals(winningBid.getBidderId())) {
                throw new RuntimeException("Bidder id of a new bid is equal to bidder id of the previous one");
            }
            winningBid.setIsWinner(false);
            bidRepository.save(winningBid);
        }
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
