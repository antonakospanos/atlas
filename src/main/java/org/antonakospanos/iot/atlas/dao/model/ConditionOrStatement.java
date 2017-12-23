package org.antonakospanos.iot.atlas.dao.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Action's disjunctive condition statement
 */
@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Table(name = "CONDITION_OR_STATEMENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "atlas.entity-cache")
public class ConditionOrStatement implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "CONDITION_ID")
	private Condition condition;

	@OneToMany(mappedBy = "conditionOrStatement", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<ConditionAndStatement> conditionAndStatements;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Set<ConditionAndStatement> getConditionAndStatements() {
		return conditionAndStatements;
	}

	public void setConditionAndStatements(Set<ConditionAndStatement> conditionAndStatements) {
		this.conditionAndStatements = conditionAndStatements;
	}
}
