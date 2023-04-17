package org.example.entity;

import java.util.Objects;

public class Registration {
    private Integer participantCode;
    private Integer probeCode;

    public Registration(Integer participantCode, Integer probeCode) {
        this.participantCode = participantCode;
        this.probeCode = probeCode;
    }

    public Integer getParticipantCode() {
        return participantCode;
    }

    public void setParticipantCode(Integer participantCode) {
        this.participantCode = participantCode;
    }

    public Integer getProbeCode() {
        return probeCode;
    }

    public void setProbeCode(Integer probeCode) {
        this.probeCode = probeCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Registration that)) return false;
        return Objects.equals(participantCode, that.participantCode) && Objects.equals(probeCode, that.probeCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(participantCode, probeCode);
    }
}
