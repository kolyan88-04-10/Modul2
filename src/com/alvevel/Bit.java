package com.alvevel;

public enum Bit {
    ZERO(0), ONE(1);

    Bit(int value) {
        this.value = value;
    }

    private final int value;

    public int value() {
        return value;
    }

    public static Bit getBit(char bitChar) {
        switch (bitChar) {
            case '0' : return ZERO;
            case '1' : return ONE;
            default: throw new RuntimeException();
        }
    }

}
