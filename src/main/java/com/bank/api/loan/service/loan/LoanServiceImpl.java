package com.bank.api.loan.service.loan;

import com.bank.api.loan.entity.Customer;
import com.bank.api.loan.entity.Loan;
import com.bank.api.loan.entity.LoanInstallment;
import com.bank.api.loan.repository.customer.CustomerRepository;
import com.bank.api.loan.repository.installment.InstallmentRepository;
import com.bank.api.loan.repository.loan.LoanRepository;
import com.bank.api.loan.service.uservalidation.UserValidationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bank.api.loan.constant.InstallmentNumbers.*;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final InstallmentRepository installmentRepository;
    private final CustomerRepository customerRepository;


    private final UserValidationServiceImpl userValidationService;

    @Override
    public com.bank.api.loan.dto.response.LoanResponse createLoan(Long customerId, BigDecimal amount, BigDecimal interestRate, int installments, Long userNo) {

        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            return new com.bank.api.loan.dto.response.LoanResponse("Customer not found");
        }
        Customer customer = customerOpt.get();
        userValidationService.userAccessValidation(userNo, customerId);
        inputValidations(amount, interestRate, installments, customer);
        BigDecimal totalAmount = amount.multiply(BigDecimal.ONE.add(interestRate));
        BigDecimal installmentAmount = totalAmount.divide(BigDecimal.valueOf(installments), 2, RoundingMode.HALF_UP);
        Loan loan = prepareLoan(installments, customer, totalAmount);
        createInstallments(installments, installmentAmount, loan);
        customer.setUsedCreditLimit(customer.getUsedCreditLimit().add(amount));
        customerRepository.save(customer);

        return new com.bank.api.loan.dto.response.LoanResponse(
                loan.getId(), loan.getCustomer().getId(), loan.getLoanAmount(),
                loan.getNumberOfInstallments(), loan.getIsPaid(), loan.getCreateDate(), null
        );

    }

    @Override
    public List<com.bank.api.loan.dto.response.LoanResponse> getLoansByCustomer(Long userNo, Long customerId) {
        userValidationService.userAccessValidation(userNo, customerId);
        return loanRepository.findByCustomerId(customerId).stream()
                .map(loan -> new com.bank.api.loan.dto.response.LoanResponse(
                        loan.getId(), loan.getCustomer().getId(), loan.getLoanAmount(),
                        loan.getNumberOfInstallments(), loan.getIsPaid(), loan.getCreateDate(), null
                ))
                .collect(Collectors.toList());
    }


    private static void inputValidations(BigDecimal amount, BigDecimal interestRate, int installments, Customer customer) {
        if (customer.getUsedCreditLimit().add(amount).compareTo(customer.getCreditLimit()) > 0) {
            throw new RuntimeException("Credit limit exceeded");
        }

        if (installments != SIX && installments != NINE && installments != TWELVE && installments != TWENTY_FOUR) {
            throw new IllegalArgumentException("Invalid installment count");
        }

        if (interestRate.compareTo(BigDecimal.valueOf(0.1)) < 0 ||
                interestRate.compareTo(BigDecimal.valueOf(0.5)) > 0) {
            throw new IllegalArgumentException("Invalid interest rate");
        }
    }

    private Loan prepareLoan(int installments, Customer customer, BigDecimal totalAmount) {
        Loan loan = new Loan();
        loan.setCustomer(customer);
        loan.setLoanAmount(totalAmount);
        loan.setNumberOfInstallments(installments);
        loan.setCreateDate(LocalDateTime.now());
        loan.setIsPaid(false);
        loan = loanRepository.save(loan);
        return loan;
    }

    private void createInstallments(int installments, BigDecimal installmentAmount, Loan loan) {
        for (int i = 0; i < installments; i++) {
            LoanInstallment installment = new LoanInstallment();
            installment.setLoan(loan);
            installment.setAmount(installmentAmount);
            installment.setPaidAmount(BigDecimal.ZERO);
            installment.setDueDate(LocalDate.now().plusMonths(i + 1).withDayOfMonth(1));
            installment.setIsPaid(false);
            installmentRepository.save(installment);
        }
    }
}