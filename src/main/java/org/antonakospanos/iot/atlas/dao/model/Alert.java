package org.antonakospanos.iot.atlas.dao.model;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

/**
 * Alerts the user for the module's state & value
 */
@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Table(name = "ALERT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "atlas.entity-cache")
public class Alert {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID")
	private Account account;

	@ManyToOne
	@JoinColumn(name = "MODULE_ID")
	private Module module;

	@OneToMany(mappedBy = "alert", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Condition> conditions;

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

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}
}
