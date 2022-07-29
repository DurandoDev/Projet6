package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.Transfer;
import com.openclassrooms.paymybuddy.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepo extends PagingAndSortingRepository<Transfer, Long> {

	public Page<Transfer> findBySender(User sender, Pageable pageable);

}
