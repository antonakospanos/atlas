package org.antonakospanos.iot.atlas.dao.model;

import org.antonakospanos.iot.atlas.enums.ModuleState;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "MODULE")
public class Module {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "DEVICE_ID")
	private Device device;

	private String type;

	@Enumerated(EnumType.STRING)
	private ModuleState state;

	private String value;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "id", fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Action> actions;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ModuleState getState() {
		return state;
	}

	public void setState(ModuleState state) {
		this.state = state;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
}


