package org.antonakospanos.iot.atlas.dao.model;


import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "DEVICE")
public class Device {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String version;

	private String externalId;

	private ZonedDateTime lastContact;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "id", fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Module> modules;


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
}
