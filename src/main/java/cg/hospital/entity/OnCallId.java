package cg.hospital.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class OnCallId implements Serializable {

    @Column(name = "Nurse")
    private Integer nurse;

    @Column(name = "BlockFloor")
    private Integer blockFloor;

    @Column(name = "BlockCode")
    private Integer blockCode;

    @Column(name = "OnCallStart")
    private LocalDateTime onCallStart;

    @Column(name = "OnCallEnd")
    private LocalDateTime onCallEnd;

    public OnCallId() {}

    public OnCallId(Integer nurse, Integer blockFloor, Integer blockCode,
                    LocalDateTime start, LocalDateTime end) {
        this.nurse = nurse;
        this.blockFloor = blockFloor;
        this.blockCode = blockCode;
        this.onCallStart = start;
        this.onCallEnd = end;
    }

    public Integer getNurse() { return nurse; }
    public Integer getBlockFloor() { return blockFloor; }
    public Integer getBlockCode() { return blockCode; }
    public LocalDateTime getOnCallStart() { return onCallStart; }
    public LocalDateTime getOnCallEnd() { return onCallEnd; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OnCallId)) return false;
        OnCallId that = (OnCallId) o;
        return Objects.equals(nurse, that.nurse) &&
               Objects.equals(blockFloor, that.blockFloor) &&
               Objects.equals(blockCode, that.blockCode) &&
               Objects.equals(onCallStart, that.onCallStart) &&
               Objects.equals(onCallEnd, that.onCallEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nurse, blockFloor, blockCode, onCallStart, onCallEnd);
    }

	public void setNurse(Integer nurse) {
		this.nurse = nurse;
	}

	public void setBlockFloor(Integer blockFloor) {
		this.blockFloor = blockFloor;
	}

	public void setBlockCode(Integer blockCode) {
		this.blockCode = blockCode;
	}

	public void setOnCallStart(LocalDateTime onCallStart) {
		this.onCallStart = onCallStart;
	}

	public void setOnCallEnd(LocalDateTime onCallEnd) {
		this.onCallEnd = onCallEnd;
	}
    
    
}