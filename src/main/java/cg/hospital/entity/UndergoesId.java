package cg.hospital.entity;

import java.io.Serializable;
import java.util.Objects;

public class UndergoesId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer procedures;
	private Integer stay;

	public UndergoesId() {
	}

	public UndergoesId(Integer procedures, Integer stay) {
		this.procedures = procedures;
		this.stay = stay;
	}

	public Integer getProcedures() {
		return procedures;
	}

	public Integer getStay() {
		return stay;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof UndergoesId))
			return false;
		UndergoesId that = (UndergoesId) o;
		return Objects.equals(procedures, that.procedures) && Objects.equals(stay, that.stay);
	}

	@Override
	public int hashCode() {
		return Objects.hash(procedures, stay);
	}
}