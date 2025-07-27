package com.example.labourApp.Models;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OTPReqDTO  implements Serializable {

    private List<Instant> requestTimes = new ArrayList<>();
    private Instant blockUntil;

    public List<Instant> getRequestTimes() {
        return requestTimes;
    }

    public void setRequestTimes(List<Instant> requestTimes) {
        this.requestTimes = requestTimes;
    }

    public Instant getBlockUntil() {
        return blockUntil;
    }

    public void setBlockUntil(Instant blockUntil) {
        this.blockUntil = blockUntil;
    }

}
