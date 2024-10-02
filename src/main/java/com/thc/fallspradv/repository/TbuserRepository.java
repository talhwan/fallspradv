package com.thc.fallspradv.repository;

import com.thc.fallspradv.domain.Tbuser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TbuserRepository extends JpaRepository<Tbuser, String> {
    Tbuser findByUsername(String username);
    Tbuser findByUsernameAndPassword(String username, String password);

    @EntityGraph(attributePaths = {"tbuserRoleType.roleType"})
    Optional<Tbuser> findEntityGraphRoleTypeById(String id);
}
