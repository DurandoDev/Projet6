package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.Transfer;
import com.openclassrooms.paymybuddy.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepo extends CrudRepository<Transfer, Long> {

}
