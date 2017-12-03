package org.antonakospanos.iot.atlas.dao.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "ACCOUNT")
public class Account {

	@Id
	@GeneratedValue
	private Long id;

	private String username;

	/**
	 * Hashed password
	 */
	private String password;

	private String name;

	private String email;

	private String cellphone;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "ACCOUNT_DEVICE_ASSOCIATIONS",
			joinColumns = {@JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID") },
			inverseJoinColumns = {@JoinColumn(name = "DEVICE_ID", referencedColumnName = "ID") }
			)
	public Set<Device> devices;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "id", fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Action> actions;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public Set<Device> getDevices() {
		return devices;
	}

	public void setDevices(Set<Device> devices) {
		this.devices = devices;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
}
