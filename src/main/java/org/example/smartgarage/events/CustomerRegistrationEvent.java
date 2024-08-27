package org.example.smartgarage.events;

import org.example.smartgarage.models.UserEntity;
import org.springframework.context.ApplicationEvent;

public class CustomerRegistrationEvent extends ApplicationEvent {
    private String password;
    private UserEntity user;
    private String appUrl;

    public CustomerRegistrationEvent(UserEntity user, String password, String appUrl) {
        super(user);
        this.user = user;
        this.password = password;
        this.appUrl = appUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
