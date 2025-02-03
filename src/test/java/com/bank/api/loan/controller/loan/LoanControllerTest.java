package com.bank.api.loan.controller.loan;


import com.bank.api.loan.dto.request.LoanPaymentRequest;
import com.bank.api.loan.dto.request.LoanRequest;
import com.bank.api.loan.dto.response.LoanPaymentResponse;
import com.bank.api.loan.dto.response.LoanResponse;
import com.bank.api.loan.service.installment.InstallmentServiceImpl;
import com.bank.api.loan.service.loan.LoanServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@RunWith(MockitoJUnitRunner.class)
public class LoanControllerTest {

    @InjectMocks
    private LoanController loanController;

    @Mock
    private LoanServiceImpl loanService;

    @Mock
    private InstallmentServiceImpl installmentService;

    @Test
    public void testCreateLoan_Endpoint() {
        LoanRequest request = new LoanRequest();
        request.setCustomerId(1L);
        request.setAmount(BigDecimal.valueOf(10000));
        request.setInterestRate(BigDecimal.valueOf(0.2));
        request.setInstallments(12);
        Mockito.when(loanService.createLoan(any(), any(), any(), Mockito.anyInt(), any())).thenReturn(prepareLoan());
        ResponseEntity<LoanResponse> result = loanController.createLoan(request);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    private static List<LoanResponse> prepareLoanList() {
        List<LoanResponse> loanResponseList = new ArrayList<>();
        LoanResponse loanResponse = prepareLoan();
        loanResponseList.add(loanResponse);
        return loanResponseList;
    }


    private static LoanResponse prepareLoan() {
        return new LoanResponse(1L, 1L, new BigDecimal(240000), 12, false, LocalDateTime.now(), null);
    }

    @Test
    public void testGetLoans_Endpoint() {
        List<LoanResponse> loans = List.of(
                new LoanResponse(1L, 1L, BigDecimal.valueOf(12000), 12, false, LocalDateTime.now(), null)
        );

        Mockito.when(loanService.getLoansByCustomer(1L,1L)).thenReturn(prepareLoanList());
        ResponseEntity<List<LoanResponse>> response = loanController.getLoans(1L,1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testPayLoan_Endpoint() {
        LoanPaymentRequest request = new LoanPaymentRequest();
        request.setAmount(BigDecimal.valueOf(2000));
        request.setUserNo(1L);

        LoanPaymentResponse mockResponse = new LoanPaymentResponse(2, BigDecimal.valueOf(2000), false);

        Mockito.when(installmentService.payLoan(Mockito.anyLong(), any(),anyLong())).thenReturn(mockResponse);

        ResponseEntity<LoanPaymentResponse> response = loanController.payLoan(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getInstallmentsPaid());
    }
}