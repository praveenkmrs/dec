package com.micropay.customers.datastore;

import com.micropay.customers.domains.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Customer, Long> {
    public Optional<Customer> findByNameIgnoreCaseContainingAndDateOfBirth(String name, LocalDate dob);
}
