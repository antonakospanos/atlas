package org.antonakospanos.iot.atlas.dao.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * ConditionConjunctive combined with OR operator
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

	@ManyToOne
	@JoinColumn(name = "ACTION_ID")
	private Action action;

	@ManyToOne
	@JoinColumn(name = "ALERT_ID")
	private Alert alert;

	@OneToMany(mappedBy = "condition", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<ConditionConjunctive> conditionConjunctives;

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

	public List<ConditionConjunctive> getConditionConjunctives() {
		return conditionConjunctives;
	}

	public void setConditionConjunctives(List<ConditionConjunctive> conditionConjunctives) {
		this.conditionConjunctives = conditionConjunctives;
	}
}
