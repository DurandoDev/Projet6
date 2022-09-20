package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.Connections;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionsRepo extends CrudRepository<Connections, Long> {

	List<Connections> findByOwner_id (Long user_id);
}
