package com.bank.api.loan.service.user;

import com.bank.api.loan.entity.AdminUser;
import com.bank.api.loan.repository.user.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AdminUserRepository userRepository;

    @Override
    public List<AdminUser> getUsers() {
        return userRepository.findAll();
    }
}
