package com.bank.api.loan.services.installment;

import com.bank.api.loan.dto.response.LoanPaymentResponse;
import com.bank.api.loan.entity.Loan;
import com.bank.api.loan.entity.LoanInstallment;
import com.bank.api.loan.repository.installment.InstallmentRepository;
import com.bank.api.loan.repository.loan.LoanRepository;
import com.bank.api.loan.service.installment.InstallmentServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(MockitoJUnitRunner.class)
public class InstallmentServiceTest {

    @InjectMocks
    private InstallmentServiceImpl installmentService;

    @Mock
    private InstallmentRepository installmentRepository;
    @Mock
    private LoanRepository loanRepository;

    @Test
    public void testPayLoan_Success() {
        Long loanId = 1L;
        Long userNo = 1L;
        BigDecimal paymentAmount = BigDecimal.valueOf(2000);

        Loan loan = new Loan(loanId, null, BigDecimal.valueOf(12000), 12, LocalDateTime.now(), false);
        List<LoanInstallment> installments = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            LoanInstallment installment = new LoanInstallment((long) i, loan, BigDecimal.valueOf(1000), BigDecimal.ZERO, LocalDate.now().plusMonths(i), null, false);
            installments.add(installment);
        }

        Mockito.when(installmentRepository.findByLoanIdOrderByDueDateAsc(loanId)).thenReturn(installments);

        LoanPaymentResponse response = installmentService.payLoan(loanId, paymentAmount,userNo);

        assertEquals(1, response.getInstallmentsPaid());
        assertEquals(BigDecimal.valueOf(1000), response.getTotalAmountPaid());
        assertFalse(response.isLoanFullyPaid());
    }

    @Test
    public void testPayLoan_NotEnoughAmount() {
        Long loanId = 1L;
        Long userNo = 1L;
        BigDecimal paymentAmount = BigDecimal.valueOf(500);

        Loan loan = new Loan(loanId, null, BigDecimal.valueOf(12000), 12, LocalDateTime.now(), false);
        List<LoanInstallment> installments = List.of(
                new LoanInstallment(1L, loan, BigDecimal.valueOf(1000), BigDecimal.ZERO, LocalDate.now(), null, false)
        );

        Mockito.when(installmentRepository.findByLoanIdOrderByDueDateAsc(loanId)).thenReturn(installments);

        LoanPaymentResponse response = installmentService.payLoan(loanId, paymentAmount,userNo);

        assertEquals(0, response.getInstallmentsPaid());
        assertEquals(BigDecimal.ZERO, response.getTotalAmountPaid());
    }
}