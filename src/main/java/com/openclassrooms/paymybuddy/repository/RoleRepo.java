package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.Role;
import com.openclassrooms.paymybuddy.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends CrudRepository<Role, Long> {
	Optional<Role> findByName (String Name);
}
