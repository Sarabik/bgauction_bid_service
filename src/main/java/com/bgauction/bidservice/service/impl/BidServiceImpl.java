package com.bgauction.bidservice.service.impl;

import com.bgauction.bidservice.exception.BadRequestException;
import com.bgauction.bidservice.exception.NotFoundException;
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
    private static final String SELLER_IS_OWNER = "Seller with id: %d for Auction with id: %d can't be a bidder";
    private static final String NEW_BID_GREATER_THEN_CURRENT = "New bid amount must be greater then current price amount for Auction with id: %d";
    private static final String NEW_BID_GREATER_THEN_OLD = "New bid amount must be greater then previous bid amount for Auction with id: %d";
    private static final String NEW_BIDDER_IS_PREVIOUS = "Bidder id: %d of a new bid is equal to bidder id: %d of the previous bid";
    private static final String AUCTION_NOT_FOUND = "Auction with id: %d not found";
    private static final String AUCTION_NOT_ACTIVE = "Auction with id: %d is not ACTIVE";
    private static final String AUCTION_PRICE_AND_WINNER_NOT_UPDATED = "Current price and winner for auction with id: %d are not updated";

    @Override
    public Bid saveBid(Bid bid) {
        Long auctionId = bid.getAuctionId();

        AuctionDto auction = getAuctionIfItExistsAndActive(auctionId);
        if (auction.getSellerId().equals(bid.getBidderId())) {
            throw new BadRequestException(String.format(SELLER_IS_OWNER, auction.getSellerId(), auctionId));
        }
        if (bid.getBidAmount().compareTo(auction.getCurrentPrice()) < 1) {
            throw new BadRequestException(String.format(NEW_BID_GREATER_THEN_CURRENT, auctionId));
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
            throw new NotFoundException(String.format(AUCTION_NOT_FOUND, auction_id));
        }
        AuctionDto auctionDto = response.getBody();
        if (auctionDto == null) {
            throw new NotFoundException(String.format(AUCTION_NOT_FOUND, auction_id));
        } else if (!auctionDto.getStatus().equals("ACTIVE")) {
            throw new BadRequestException(String.format(AUCTION_NOT_ACTIVE, auction_id));
        }
        return auctionDto;
    }

    private void updateAuctionCurrentPriceAndWinner(Long auctionId, BigDecimal currentPrice, Long winnerId) {
        ResponseEntity<Void> response = auctionClient.updateCurrentPriceAndWinner(auctionId, currentPrice, winnerId);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw new BadRequestException(String.format(AUCTION_PRICE_AND_WINNER_NOT_UPDATED, auctionId));
        }
    }

    private void changeStatusOfLastWinningBid(Bid newBid) {
        Optional<Bid> bidOptional = bidRepository.findWinningBidByAuctionId(newBid.getAuctionId());
        if (bidOptional.isPresent()) {
            Bid winningBid = bidOptional.get();
            if (newBid.getBidAmount().compareTo(winningBid.getBidAmount()) < 1) {
                throw new BadRequestException(String.format(NEW_BID_GREATER_THEN_OLD, winningBid.getAuctionId()));
            } else if (newBid.getBidderId().equals(winningBid.getBidderId())) {
                throw new BadRequestException(String.format(NEW_BIDDER_IS_PREVIOUS, newBid.getBidderId(), winningBid.getBidderId()));
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
