package com.example.gameonline.model;


import java.io.Serializable;

public enum TicToe implements Serializable {
    X(1), O(2);

    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    TicToe(int i) {
    }
}
