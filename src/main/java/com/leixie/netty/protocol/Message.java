package com.leixie.netty.protocol;

import java.io.Serializable;

/**
 * @author xielei
 */

public class Message implements Serializable {

    String name;

    Integer number;

    String message;


    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public Integer getNumber() {
        return number;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
            "name='" + name + '\'' +
            ", number=" + number +
            ", message='" + message + '\'' +
            '}';
    }
}
