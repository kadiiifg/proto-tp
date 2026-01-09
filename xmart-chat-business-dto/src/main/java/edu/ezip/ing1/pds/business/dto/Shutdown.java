package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "Shutdown")
public class Shutdown {
    public String reason;

    public Shutdown() {
    }

    @JsonProperty("reason")
    public String getReason() {
        return reason;
    }

    @JsonProperty("reason")
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Shutdown{" +
                "reason='" + reason + '\'' +
                '}';
    }
}
