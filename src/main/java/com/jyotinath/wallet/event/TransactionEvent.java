package com.jyotinath.wallet.event;

import com.jyotinath.wallet.entity.Wallet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEvent {
    private Long senderId;
    private Long receiverId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime timestamp;
}

