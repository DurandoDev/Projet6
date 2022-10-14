package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.Balance;
import com.openclassrooms.paymybuddy.model.User;
import org.springframework.data.repository.CrudRepository;

public interface BalanceRepo extends CrudRepository<Balance, Long> {
}
