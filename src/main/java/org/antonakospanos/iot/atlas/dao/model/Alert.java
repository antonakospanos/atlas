package org.antonakospanos.iot.atlas.dao.model;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.UUID;

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

	private String name;

	private UUID externalId;

	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID")
	private Account account;

	@OneToOne(mappedBy = "alert", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Condition condition;


	public Alert() {
		this.externalId = UUID.randomUUID();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UUID getExternalId() {
		return externalId;
	}

	public void setExternalId(UUID externalId) {
		this.externalId = externalId;
	}

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

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}
}
