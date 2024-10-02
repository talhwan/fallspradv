package com.thc.fallspradv.repository;

import com.thc.fallspradv.domain.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleTypeRepository extends JpaRepository<RoleType, String>{
	RoleType findByTypeName(String typeName);
}
