package org.antonakospanos.iot.atlas.dao.repository;

import org.antonakospanos.iot.atlas.dao.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActionRepository extends JpaRepository<Action, Long> {

	Action findByExternalId(UUID actionExternalId);

	List<Action> findByModule_Id(Long moduleId);


	List<Action> findByModule_ExternalId(String moduleExternalId);

	List<Action> findByAccount_ExternalId(UUID accountExternalId);

	List<Action> findByAccount_Username(String username);

	List<Action> findByAccount_Username_AndModule_ExternalId(String username, String externalId);

	List<Action> findByAccount_ExternalId_AndModule_ExternalId(UUID accountExternalId, String moduleExternalId);

}
