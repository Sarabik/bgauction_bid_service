package com.bgauction.bidservice.kafka.eventPublisher;

import com.bgauction.bidservice.kafka.event.BidCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Log4j2
@Service
@RequiredArgsConstructor
public class BidEventPublisher {
    private static final String TOPIC = "bid-created-event-topic";
    private final KafkaTemplate<String, BidCreatedEvent> kafkaTemplate;

    public void publishBidCreatedEvent(BidCreatedEvent event) throws ExecutionException, InterruptedException {
        SendResult<String, BidCreatedEvent> send = kafkaTemplate.send(TOPIC, event).get();
        log.info("Send message with topic: {}", send.getRecordMetadata().topic());
    }
}
