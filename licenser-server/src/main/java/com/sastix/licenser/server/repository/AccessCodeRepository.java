package com.sastix.licenser.server.repository;

import com.sastix.licenser.server.model.AccessCode;
import com.sastix.licenser.server.model.Tenant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

@Transactional
public interface AccessCodeRepository extends CrudRepository<AccessCode, Integer> {

    AccessCode getByAccessCode(@Param("accessCode") String accessCode);
    AccessCode getAccessCodeByAccessCodeAndTenant(String accessCode, Tenant tenant);
}
