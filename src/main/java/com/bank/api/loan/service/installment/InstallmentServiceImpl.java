package com.bank.api.loan.service.installment;

import com.bank.api.loan.dto.response.LoanPaymentResponse;
import com.bank.api.loan.entity.Loan;
import com.bank.api.loan.entity.LoanInstallment;
import com.bank.api.loan.repository.installment.InstallmentRepository;
import com.bank.api.loan.repository.loan.LoanRepository;
import com.bank.api.loan.service.uservalidation.UserValidationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstallmentServiceImpl implements InstallmentService {
    private final InstallmentRepository installmentRepository;
    private final LoanRepository loanRepository;

    private final UserValidationServiceImpl userValidationService;

    @Override
    public List<LoanInstallment> getInstallmentsByLoan(Long loanId) {
        return installmentRepository.findByLoanIdOrderByDueDateAsc(loanId);
    }

    @Override
    public LoanPaymentResponse payLoan(Long loanId, BigDecimal amount, Long userNo) {
        Optional<Loan> loan = loanRepository.findById(loanId);
        loan.ifPresent(value -> userValidationService.userAccessValidation(userNo, value.getCustomer().getId()));
        List<LoanInstallment> installments = installmentRepository.findByLoanIdOrderByDueDateAsc(loanId);
        return payInstallments(loanId, amount, installments);
    }

    private LoanPaymentResponse payInstallments(Long loanId, BigDecimal amount, List<LoanInstallment> installments) {
        BigDecimal remainingAmount = amount;
        int paidCount = 0;
        BigDecimal totalPaid = BigDecimal.ZERO;

        for (LoanInstallment installment : installments) {
            if (!installment.getIsPaid()) {
                BigDecimal installmentAmount = installment.getAmount();
                boolean isOutOfCalendar = installment.getDueDate().isAfter(LocalDate.now().plusMonths(2).withDayOfMonth(1));
                if (remainingAmount.compareTo(installmentAmount) >= 0 && !isOutOfCalendar) {
                    installment.setPaidAmount(installmentAmount);
                    installment.setPaymentDate(LocalDate.now());
                    installment.setIsPaid(true);
                    installmentRepository.save(installment);
                    remainingAmount = remainingAmount.subtract(installmentAmount);
                    totalPaid = totalPaid.add(installmentAmount);
                    paidCount++;
                } else {
                    break;
                }
            }
        }

        boolean loanFullyPaid = updateLoan(loanId, installments);
        return new LoanPaymentResponse(paidCount, totalPaid, loanFullyPaid);
    }

    private boolean updateLoan(Long loanId, List<LoanInstallment> installments) {
        boolean loanFullyPaid = false;
        if (installments.stream().allMatch(LoanInstallment::getIsPaid)) {
            loanFullyPaid = true;
            Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Invalid Loan"));
            loan.setIsPaid(true);
            loanRepository.save(loan);
        }
        return loanFullyPaid;
    }
}