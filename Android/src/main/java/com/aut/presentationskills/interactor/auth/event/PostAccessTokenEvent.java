package com.aut.presentationskills.interactor.auth.event;

public class PostAccessTokenEvent {
    private String jwt;
    private Throwable throwable;

    public PostAccessTokenEvent(){}

    public PostAccessTokenEvent(String jwt, Throwable throwable){
        this.jwt= jwt;
        this.throwable=throwable;
    }


    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
