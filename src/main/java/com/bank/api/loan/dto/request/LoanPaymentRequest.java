package com.bank.api.loan.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanPaymentRequest {
    private BigDecimal amount;
    private Long userNo;
}