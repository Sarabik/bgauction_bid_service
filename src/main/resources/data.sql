INSERT INTO bids (auction_id, bidder_id, bid_amount, bid_time, is_winner) VALUES
(1, 3, 120.00, NOW() - INTERVAL 20 MINUTE, false),
(1, 2, 150.50, NOW() - INTERVAL 15 MINUTE, true),
(2, 1, 250.00, NOW() - INTERVAL 20 MINUTE, false),
(2, 3, 350.50, NOW() - INTERVAL 15 MINUTE, true);
