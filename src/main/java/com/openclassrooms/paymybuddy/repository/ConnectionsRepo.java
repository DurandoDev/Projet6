package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.Connections;
import com.openclassrooms.paymybuddy.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionsRepo extends CrudRepository<Connections, Long> {

	@Query("SELECT c from Connections c WHERE c.owner.id=:user_id")
	public List<Connections> findUserConnections(Long user_id);
}
