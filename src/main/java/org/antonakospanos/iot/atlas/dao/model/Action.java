package org.antonakospanos.iot.atlas.dao.model;


import org.antonakospanos.iot.atlas.enums.ModuleState;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "atlas.entity-cache")
@Table(name = "ACTION")
public class Action implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID")
	private Account account;

	@ManyToOne
	@JoinColumn(name = "MODULE_ID")
	private Module module;

	private ZonedDateTime nextExecution;

	private Long periodOfMinutes;

	@Enumerated(EnumType.STRING)
	private ModuleState state;

	private String value;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public ZonedDateTime getNextExecution() {
		return nextExecution;
	}

	public void setNextExecution(ZonedDateTime nextExecution) {
		this.nextExecution = nextExecution;
	}

	public Long getPeriodOfMinutes() {
		return periodOfMinutes;
	}

	public void setPeriodOfMinutes(Long periodOfMinutes) {
		this.periodOfMinutes = periodOfMinutes;
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
}
