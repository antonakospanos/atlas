package org.antonakospanos.iot.atlas.dao.repository;

import org.antonakospanos.iot.atlas.dao.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long> {

	List<Module> findByDeviceIdAndType(String deviceId, String moduleType);

}
