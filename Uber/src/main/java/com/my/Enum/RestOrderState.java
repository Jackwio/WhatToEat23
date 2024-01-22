package com.my.Enum;



public enum RestOrderState {
    UNACCEPT(0,"未接單"),REJECT(1,"拒絕訂單"),
    ACCEPTED(2,"已接單"),FINISH(3,"完成訂單");


    private int code;
    private String value;

    RestOrderState(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
