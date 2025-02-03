package com.bank.api.loan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class LoanPaymentResponse {
    private int installmentsPaid;
    private BigDecimal totalAmountPaid;
    private boolean isLoanFullyPaid;
}