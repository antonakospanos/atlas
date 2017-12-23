package org.antonakospanos.iot.atlas.dao.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 *  Action's condition
 */
@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Table(name = "CONDITION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "atlas.entity-cache")
public class Condition implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "ACTION_ID")
	private Action action;

	@OneToOne
	@JoinColumn(name = "ALERT_ID")
	private Alert alert;

	@OneToMany(mappedBy = "condition", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<ConditionOrStatement> conditionOrStatements;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Alert getAlert() {
		return alert;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}

	public Set<ConditionOrStatement> getConditionOrStatements() {
		return conditionOrStatements;
	}

	public void setConditionOrStatements(Set<ConditionOrStatement> conditionOrStatements) {
		this.conditionOrStatements = conditionOrStatements;
	}
}
