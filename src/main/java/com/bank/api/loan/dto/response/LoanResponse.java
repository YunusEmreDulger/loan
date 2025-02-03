package com.bank.api.loan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class LoanResponse {

    public LoanResponse(String errorMessage){
        this.errorMessage = errorMessage;
    }
    private Long loanId;
    private Long customerId;
    private BigDecimal totalAmount;
    private Integer numberOfInstallments;
    private Boolean isPaid;
    private LocalDateTime createDate;
    private String errorMessage;
}