package com.jyotinath.wallet.dto;

import com.jyotinath.wallet.entity.Wallet;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    private long transactionId;
    private BigDecimal amount;
    private String crDrFlg;
    private String senderName;
    private String receiverName;
    private LocalDateTime timeStamp;

    public TransactionResponse(long transactionId, BigDecimal amount, String crDrFlg, String senderName,String receiverName, LocalDateTime timeStamp){
        this.transactionId = transactionId;
        this.amount = amount;
        this.crDrFlg = crDrFlg;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.timeStamp = timeStamp;
    }
}
