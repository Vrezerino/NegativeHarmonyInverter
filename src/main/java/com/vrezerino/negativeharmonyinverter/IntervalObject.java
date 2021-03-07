package com.vrezerino.negativeharmonyinverter;

public class IntervalObject {

    private final int inversionDistance;
    private final int keyShift;

    public IntervalObject(int inversionDistance, int keyShift) {
        this.inversionDistance = inversionDistance;
        this.keyShift = keyShift;
    }

    public int getInversionDistance() {
        return this.inversionDistance;
    }

    public int getKeyShift() {
        return this.keyShift;
    }
}
