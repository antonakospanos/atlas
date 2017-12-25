package org.antonakospanos.iot.atlas.dao.model;


import org.antonakospanos.iot.atlas.enums.ModuleState;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Changes the module's state & value
 */
@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "atlas.entity-cache")
@Table(name = "ACTION")
public class Action implements Serializable {

	public Action() {
		this.externalId = UUID.randomUUID();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private UUID externalId;

	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID")
	private Account account;

	@ManyToOne
	@JoinColumn(name = "MODULE_ID")
	private Module module;

	private ZonedDateTime nextExecution;

	private Long periodInSecods;

	@Enumerated(EnumType.STRING)
	private ModuleState state;

	private String value;

	@OneToOne(mappedBy = "action", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Condition condition;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UUID getExternalId() {
		return externalId;
	}

	public void setExternalId(UUID externalId) {
		this.externalId = externalId;
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

	public Long getPeriodInSecods() {
		return periodInSecods;
	}

	public void setPeriodInSecods(Long periodInSecods) {
		this.periodInSecods = periodInSecods;
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

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}
}
