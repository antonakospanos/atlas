package org.antonakospanos.iot.atlas.dao.model;


import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Table(name = "DEVICE")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "atlas.entity-cache")
public class Device implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String version;

	private String externalId;

	private ZonedDateTime lastContact;

	@OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Module> modules;

	public Device() {
	}

	public Device(String name, String version, String externalId, ZonedDateTime lastContact, List<Module> modules) {
		this.name = name;
		this.version = version;
		this.externalId = externalId;
		this.lastContact = lastContact;
		this.modules = modules;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public ZonedDateTime getLastContact() {
		return lastContact;
	}

	public void setLastContact(ZonedDateTime lastContact) {
		this.lastContact = lastContact;
	}

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	public boolean addModule(Module module) {
		boolean addition = false;

		if (this.modules == null) {
			this.modules = new ArrayList<Module>();
		}

		if (this.modules.add(module)) {
			module.setDevice(this);
			addition = true;
		}

		return addition;
	}

	public boolean removeModule(Module module) {
		boolean remove = false;

		if ((this.modules != null) && this.modules.remove(module)) {
			module.setDevice(null);
			remove = true;
		}

		return remove;
	}
}
