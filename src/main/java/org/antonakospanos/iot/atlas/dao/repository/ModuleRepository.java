package org.antonakospanos.iot.atlas.dao.repository;

import org.antonakospanos.iot.atlas.dao.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long> {

	Module findByExternalId_AndDevice_Id(String externalId, Long deviceId);

	Module findByExternalId_AndDevice_ExternalId(String moduleExternalId, String deviceExternalId);

	List<Module> findByDevice_Id(Long deviceId);

	List<Module> findByDevice_ExternalId(String externalId);

}
