package org.antonakospanos.iot.atlas.dao.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Action's conjunctive condition statement
 */
@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Table(name = "CONDITION_AND_STATEMENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "atlas.entity-cache")
public class ConditionAndStatement implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "CONDITION_OR_STATEMENT_ID")
	private ConditionOrStatement conditionOrStatement;

	@OneToOne(mappedBy = "conditionAndStatement", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private ConditionStatement conditionStatement;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ConditionOrStatement getConditionOrStatement() {
		return conditionOrStatement;
	}

	public void setConditionOrStatement(ConditionOrStatement conditionOrStatement) {
		this.conditionOrStatement = conditionOrStatement;
	}

	public ConditionStatement getConditionStatement() {
		return conditionStatement;
	}

	public void setConditionStatement(ConditionStatement conditionStatement) {
		this.conditionStatement = conditionStatement;
	}
}
