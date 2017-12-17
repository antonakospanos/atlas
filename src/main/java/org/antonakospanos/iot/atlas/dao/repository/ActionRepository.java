package org.antonakospanos.iot.atlas.dao.repository;

import org.antonakospanos.iot.atlas.dao.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Long> {

	List<Action> findByModule_Id(Long moduleId);

	List<Action> findByModule_ExternalId(String externalId);

	List<Action> findByAccount_Username(String username);

	List<Action> findByAccount_Username_AndModule_ExternalId(String username, String externalId);


}
