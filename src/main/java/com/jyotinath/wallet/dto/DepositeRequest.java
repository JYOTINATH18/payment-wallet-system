package com.jyotinath.wallet.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositeRequest {

    private BigDecimal amount;
}
