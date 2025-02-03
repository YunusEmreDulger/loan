package com.bank.api.loan.service.uservalidation;

import com.bank.api.loan.entity.AdminUser;
import com.bank.api.loan.entity.Customer;
import com.bank.api.loan.repository.customer.CustomerRepository;
import com.bank.api.loan.repository.user.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserValidationServiceImpl implements UserValidationService {
    private final CustomerRepository customerRepository;

    private final AdminUserRepository userRepository;

    public void userAccessValidation(Long userNo, Long customerId) {
        Optional<AdminUser> adminUser = userRepository.findAdminUserByUserNo(userNo);
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (adminUser.isEmpty() && customer.isPresent() && !customerId.equals(userNo)) {
            throw new RuntimeException("You can not do any operation for this customerId");
        }
    }
}
