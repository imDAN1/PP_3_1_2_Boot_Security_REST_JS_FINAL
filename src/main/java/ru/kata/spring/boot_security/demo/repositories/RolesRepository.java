package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

@Repository
public interface RolesRepository extends CrudRepository<Role, Long> {
}
