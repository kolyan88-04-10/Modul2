package com.alvevel;

public interface Builder {
    Builder addBit(Bit bit);
    Result build();
}
