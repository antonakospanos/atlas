package org.antonakospanos.iot.atlas.dao.repository;

import org.antonakospanos.iot.atlas.dao.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

	Account findByUsername(String username);

	List<Account> findByDevices_ExternalId(String externalId);

}