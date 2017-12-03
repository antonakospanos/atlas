package org.antonakospanos.iot.atlas.dao.model;


import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ACTION")
public class Action {

	@Id
	@GeneratedValue
	private Long id;

	private String type;

	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID")
	private Account account;

	@ManyToOne
	@JoinColumn(name = "MODULE_ID")
	private Module module;

	private String condition;

	private ZonedDateTime lastExecution;

	private ZonedDateTime nextExecution;

	private Long periodOfMinutes;


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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public ZonedDateTime getLastExecution() {
		return lastExecution;
	}

	public void setLastExecution(ZonedDateTime lastExecution) {
		this.lastExecution = lastExecution;
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
}
