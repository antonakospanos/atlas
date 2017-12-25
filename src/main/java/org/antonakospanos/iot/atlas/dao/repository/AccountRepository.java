package org.antonakospanos.iot.atlas.dao.repository;

import org.antonakospanos.iot.atlas.dao.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, Long> {

	Account findByUsername(String username);

	Account findByExternalId(UUID externalId);

	List<Account> findByDevices_ExternalId(String externalId);

}