package com.github.justadeni.irongate.enums;

public enum Adjacent {
    NEITHER(1),
    LEFT(3),
    RIGHT(2),
    BOTH(4);

    public final int id;

    private Adjacent(int id){
        this.id = id;
    }
}
