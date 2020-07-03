package com.h2code.huchat;

public class Contacts {
    public String name, status, image ,notificationKey;

    public Contacts()
    {

    }

    public Contacts(String name, String status, String image) {
        this.name = name;
        this.status = status;
        this.image = image;
    }



    public String getNotificationKey() {
        return notificationKey;
    }
    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
