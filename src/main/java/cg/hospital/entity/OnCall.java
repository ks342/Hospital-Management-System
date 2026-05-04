package cg.hospital.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "On_Call")
public class OnCall {

    @EmbeddedId
    private OnCallId id;

    @ManyToOne
    @MapsId("nurse")
    @JoinColumn(name = "Nurse")
    private Nurse nurse;

    // getters
    public OnCallId getId() { return id; }

    public Nurse getNurse() { return nurse; }

    // expose fields for UI/tests
    public LocalDateTime getOnCallStart() {
        return id.getOnCallStart();
    }

    public LocalDateTime getOnCallEnd() {
        return id.getOnCallEnd();
    }

    public Integer getBlockFloor() {
        return id.getBlockFloor();
    }

    public Integer getBlockCode() {
        return id.getBlockCode();
    }
}