package org.antonakospanos.iot.atlas.dao.model;

import org.antonakospanos.iot.atlas.enums.ModuleState;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Action's condition statement
 */
@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Table(name = "CONDITION_STATEMENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "atlas.entity-cache")
public class ConditionStatement implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "CONDITION_AND_STATEMENT_ID")
	private ConditionAndStatement conditionAndStatement;

	@ManyToOne
	@JoinColumn(name = "MODULE_ID")
	private Module module;

	/**  Added by DTO deserializer before finding the DB relationship	*/
	private transient String deviceExternalId;

	/**  Added by DTO deserializer before finding the DB relationship	*/
	private transient String moduleExternalId;

	@Enumerated(EnumType.STRING)
	private ModuleState state;

	private String value;

	private Double minValue;

	private Double maxValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ConditionAndStatement getConditionAndStatement() {
		return conditionAndStatement;
	}

	public void setConditionAndStatement(ConditionAndStatement conditionAndStatement) {
		this.conditionAndStatement = conditionAndStatement;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
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

	public Double getMinValue() {
		return minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public String getDeviceExternalId() {
		return deviceExternalId;
	}

	public void setDeviceExternalId(String deviceExternalId) {
		this.deviceExternalId = deviceExternalId;
	}

	public String getModuleExternalId() {
		return moduleExternalId;
	}

	public void setModuleExternalId(String moduleExternalId) {
		this.moduleExternalId = moduleExternalId;
	}
}
