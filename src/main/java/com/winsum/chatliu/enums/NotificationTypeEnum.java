package com.winsum.chatliu.enums;

public enum NotificationTypeEnum {

    REPLAY_COMMENT(1,"回复了评论"),
    REPLAY_QUESTION(2,"回复了问题");

    private int type;
    private String name;


    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    NotificationTypeEnum(Integer status, String name){
        this.type = status;
        this.name = name;
    }

    public static  String getNameByType(Integer type){
        for (NotificationTypeEnum notification : NotificationTypeEnum.values()) {
            if (notification.getType() == type){
                return notification.getName();
            }
        }
        return "";
    }
}
