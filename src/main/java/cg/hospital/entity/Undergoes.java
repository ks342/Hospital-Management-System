package cg.hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Undergoes")
public class Undergoes {

	@EmbeddedId
	private UndergoesId id;

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

	// Plain FK value — owns the Patient column for insert/update
	@Column(name = "Patient")
	private Integer patientId;

	// FK to Patient — @ManyToOne, read-only (patientId owns the column)
	// Patient PK is SSN (Integer) — matches patientId type
	@ManyToOne
	@JoinColumn(name = "Patient", referencedColumnName = "SSN", insertable = false, updatable = false)
	private Patient patient;

	public Undergoes() {
	}

	// Getters
	public UndergoesId getId() {
		return id;
	}

	public void setId(UndergoesId id) {
		this.id = id;
	}

	public Integer getProcedures() {
		return id != null ? id.getProcedures() : null;
	}

	public Integer getStay() {
		return id != null ? id.getStay() : null;
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

	public Integer getPatientId() {
		return patientId;
	}

	public Patient getPatient() {
		return patient;
	}

	// Setters
	public void setProcedures(Integer procedures) {
		if (this.id == null)
			this.id = new UndergoesId();
		this.id.setProcedures(procedures);
	}

	public void setStay(Integer stay) {
		if (this.id == null)
			this.id = new UndergoesId();
		this.id.setStay(stay);
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

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
}