package cg.hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Undergoes")
@IdClass(UndergoesId.class)
public class Undergoes {

	// Composite PK — Part 1: Procedures (FK to Procedures.Code)
	// Plain Integer in @Id — @IdClass cannot hold entity references
	@Id
	@Column(name = "Procedures")
	private Integer procedures;

	// Composite PK — Part 2: Stay (FK to Stay.StayID)
	// Plain Integer in @Id — same rule as above
	@Id
	@Column(name = "Stay")
	private Integer stay;

	@Column(name = "DateUndergoes")
	private LocalDateTime dateUndergoes;

	// FK to Physician — @ManyToOne, read-only (physicianId owns the column)
	@ManyToOne
	@JoinColumn(name = "Physician", insertable = false, updatable = false)
	private Physician physician;

	// Plain FK value — owns the Physician column for insert/update
	@Column(name = "Physician")
	private Integer physicianId;

	// FK to Stay — @ManyToOne using same column as the @Id "Stay" above
	// insertable=false, updatable=false because the @Id column owns it
	@ManyToOne
	@JoinColumn(name = "Stay", insertable = false, updatable = false)
	private Stay stayEntity;

	// FK to Nurse — @ManyToOne, AssistingNurse column
	// insertable=false, updatable=false because assistingNurseId owns the column
	@ManyToOne
	@JoinColumn(name = "AssistingNurse", insertable = false, updatable = false)
	private Nurse assistingNurse;

	// Plain FK value — owns the AssistingNurse column for insert/update
	@Column(name = "AssistingNurse")
	private Integer assistingNurseId;

	public Undergoes() {
	}

	// Getters
	public Integer getProcedures() {
		return procedures;
	}

	public Integer getStay() {
		return stay;
	}

	public LocalDateTime getDateUndergoes() {
		return dateUndergoes;
	}

	public Physician getPhysician() {
		return physician;
	}

	public Integer getPhysicianId() {
		return physicianId;
	}

	public Stay getStayEntity() {
		return stayEntity;
	}

	public Nurse getAssistingNurse() {
		return assistingNurse;
	}

	public Integer getAssistingNurseId() {
		return assistingNurseId;
	}

	// Setters
	public void setProcedures(Integer procedures) {
		this.procedures = procedures;
	}

	public void setStay(Integer stay) {
		this.stay = stay;
	}

	public void setDateUndergoes(LocalDateTime dateUndergoes) {
		this.dateUndergoes = dateUndergoes;
	}

	public void setPhysician(Physician physician) {
		this.physician = physician;
	}

	public void setPhysicianId(Integer physicianId) {
		this.physicianId = physicianId;
	}

	public void setStayEntity(Stay stayEntity) {
		this.stayEntity = stayEntity;
	}

	public void setAssistingNurse(Nurse assistingNurse) {
		this.assistingNurse = assistingNurse;
	}

	public void setAssistingNurseId(Integer assistingNurseId) {
		this.assistingNurseId = assistingNurseId;
	}
}