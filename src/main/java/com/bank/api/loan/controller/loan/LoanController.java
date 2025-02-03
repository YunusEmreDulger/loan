package com.bank.api.loan.controller.loan;

import com.bank.api.loan.dto.request.LoanPaymentRequest;
import com.bank.api.loan.dto.request.LoanRequest;
import com.bank.api.loan.dto.response.LoanInstallmentResponse;
import com.bank.api.loan.dto.response.LoanPaymentResponse;
import com.bank.api.loan.dto.response.LoanResponse;
import com.bank.api.loan.service.installment.InstallmentService;
import com.bank.api.loan.service.loan.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@CrossOrigin
public class LoanController {
    private final LoanService loanService;
    private final InstallmentService installmentService;

    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(@RequestBody LoanRequest request) {
        LoanResponse loanResponse = loanService.createLoan(request.getCustomerId(), request.getAmount(),
                request.getInterestRate(), request.getInstallments(), request.getUserNo());
        return ResponseEntity.ok(loanResponse);
    }

    @GetMapping
    public ResponseEntity<List<LoanResponse>> getLoans(@RequestParam Long userNo, Long customerId) {
        List<LoanResponse> loans = loanService.getLoansByCustomer(userNo, customerId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{loanId}/installments")
    public ResponseEntity<List<LoanInstallmentResponse>> getInstallments(@PathVariable Long loanId) {
        List<LoanInstallmentResponse> installments = installmentService.getInstallmentsByLoan(loanId)
                .stream()
                .map(installment -> new LoanInstallmentResponse(
                        installment.getId(), installment.getLoan().getId(), installment.getAmount(),
                        installment.getPaidAmount(), installment.getDueDate(),
                        installment.getPaymentDate(), installment.getIsPaid()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(installments);
    }

    @PostMapping("/{loanId}/pay")
    public ResponseEntity<LoanPaymentResponse> payLoan(
            @PathVariable Long loanId, @RequestBody LoanPaymentRequest request) {

        LoanPaymentResponse response = installmentService.payLoan(loanId, request.getAmount(), request.getUserNo());
        return ResponseEntity.ok(response);
    }
}
