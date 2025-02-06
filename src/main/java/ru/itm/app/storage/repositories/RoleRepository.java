package ru.itm.app.storage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itm.app.storage.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
