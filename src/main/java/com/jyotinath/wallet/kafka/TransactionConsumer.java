package com.jyotinath.wallet.kafka;

import com.jyotinath.wallet.event.TransactionEvent;
import com.jyotinath.wallet.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionConsumer {
    private final NotificationService notificationService;

    public TransactionConsumer(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "wallet-transactions", groupId = "wallet-group")
    public void consume(TransactionEvent event) {
        notificationService.sendTransferEmail(event);
    }
}