package com.bank.api.loan.service.loan;

import com.bank.api.loan.dto.response.LoanResponse;

import java.math.BigDecimal;
import java.util.List;

public interface LoanService {

    LoanResponse createLoan(Long customerId, BigDecimal amount, BigDecimal interestRate, int installments, Long userNo);

    List<LoanResponse> getLoansByCustomer(Long userNo, Long customerId);
}
