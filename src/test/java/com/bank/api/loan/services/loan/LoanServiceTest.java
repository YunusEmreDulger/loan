package com.bank.api.loan.services.loan;

import com.bank.api.loan.dto.response.LoanResponse;
import com.bank.api.loan.entity.Customer;
import com.bank.api.loan.entity.Loan;
import com.bank.api.loan.repository.customer.CustomerRepository;
import com.bank.api.loan.repository.installment.InstallmentRepository;
import com.bank.api.loan.repository.loan.LoanRepository;
import com.bank.api.loan.service.loan.LoanServiceImpl;
import com.bank.api.loan.service.uservalidation.UserValidationServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class LoanServiceTest {

    @InjectMocks
    private LoanServiceImpl loanService;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private InstallmentRepository installmentRepository;

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private UserValidationServiceImpl userValidationService;

    @Test
    public void testCreateLoan_Success() {
        Long customerId = 1L;
        BigDecimal amount = BigDecimal.valueOf(10000);
        BigDecimal interestRate = BigDecimal.valueOf(0.2);
        int installments = 12;
        Long userNo = 1L;

        Customer customer = new Customer(customerId, "John", "Doe", BigDecimal.valueOf(20000), BigDecimal.ZERO);
        Loan loan = new Loan(1L, customer, amount.multiply(BigDecimal.valueOf(1.2)), installments, LocalDateTime.now(), false);

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(loanRepository.save(Mockito.any(Loan.class))).thenReturn(loan);

        LoanResponse createdLoan = loanService.createLoan(customerId, amount, interestRate, installments, userNo);

        assertNotNull(createdLoan);
        assertEquals(customerId, createdLoan.getCustomerId());
        assertEquals(12, createdLoan.getNumberOfInstallments().intValue());
    }

    @Test(expected = RuntimeException.class)
    public void testCreateLoan_CreditLimitExceeded() {
        Long customerId = 1L;
        Long userNo = 1L;
        BigDecimal amount = BigDecimal.valueOf(25000);

        Customer customer = new Customer(customerId, "John", "Doe", BigDecimal.valueOf(20000), BigDecimal.ZERO);
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        loanService.createLoan(customerId, amount, BigDecimal.valueOf(0.2), 12,userNo);
    }
}