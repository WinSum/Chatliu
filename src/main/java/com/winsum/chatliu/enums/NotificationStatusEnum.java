package com.winsum.chatliu.enums;

public enum NotificationStatusEnum {
    READ(0), UNREAD(1);

    private int status;

    public int getStatus() {
        return status;
    }

    NotificationStatusEnum(int status) {
        this.status = status;
    }
}
