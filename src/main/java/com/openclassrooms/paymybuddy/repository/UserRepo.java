package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.Role;
import com.openclassrooms.paymybuddy.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {
	Optional<User> findByEmail(String email);
}
