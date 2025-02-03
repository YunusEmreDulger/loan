package com.bank.api.loan.service.installment;

import com.bank.api.loan.dto.response.LoanPaymentResponse;
import com.bank.api.loan.entity.LoanInstallment;

import java.math.BigDecimal;
import java.util.List;

public interface InstallmentService {

    List<LoanInstallment> getInstallmentsByLoan(Long loanId);

    LoanPaymentResponse payLoan(Long loanId, BigDecimal amount, Long userNo);
}
