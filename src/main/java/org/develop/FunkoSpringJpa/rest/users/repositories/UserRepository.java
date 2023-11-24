package org.develop.FunkoSpringJpa.rest.users.repositories;

import org.develop.FunkoSpringJpa.rest.users.commons.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsernameIgnoreCase(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

    List<User> findAllByUsernameIgnoreCase(String username);

    @Modifying
    @Query("UPDATE User p SET p.isActive = false WHERE p.id = :id")
    void updateIsDActiveToFalseById(Long id);

}
