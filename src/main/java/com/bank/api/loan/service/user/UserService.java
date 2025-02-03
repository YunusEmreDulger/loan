package com.bank.api.loan.service.user;

import com.bank.api.loan.entity.AdminUser;

import java.util.List;

public interface UserService {

    List<AdminUser> getUsers();
}
