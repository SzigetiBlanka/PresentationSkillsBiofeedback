package com.aut.presentationskills.interactor.user.event;

import com.aut.presentationskills.model.User;

public class GetUserEvent {
    private User user;
    private Throwable throwable;

    public GetUserEvent(){}

    public GetUserEvent(User user, Throwable throwable){
        this.user= user;
        this.throwable=throwable;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
