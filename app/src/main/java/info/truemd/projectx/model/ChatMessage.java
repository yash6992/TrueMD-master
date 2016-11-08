package com.truemdhq.projectx.model;

/**
 * Created by Technovibe on 17-04-2015.
 */
public class ChatMessage {
    private long id;
    private boolean isMe, onClickable, onLongPressable;
    private String message;
    private Long userId;
    private Long dateTime;


    public boolean isOnLongPressable() {
        return onLongPressable;
    }

    public void setOnLongPressable(boolean onLongPressable) {
        this.onLongPressable = onLongPressable;
    }

    public boolean isOnClickable() {
        return onClickable;
    }

    public void setOnClickable(boolean onClickable) {
        this.onClickable = onClickable;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getIsme() {
        return isMe;
    }

    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Long getDate() {
        return dateTime;
    }

    public void setDate(Long dateTime) {
        this.dateTime = dateTime;
    }
}
