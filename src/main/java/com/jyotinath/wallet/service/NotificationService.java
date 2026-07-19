package com.jyotinath.wallet.service;

import com.jyotinath.wallet.event.TransactionEvent;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendTransferEmail(TransactionEvent event){
        System.out.println("=================================");
        System.out.println("EMAIL SENT");
        System.out.println("Receiver : " + event.getReceiverId());
        System.out.println("Amount   : " + event.getAmount());
        System.out.println("Status   : " + event.getStatus());
        System.out.println("=================================");
    }
}
