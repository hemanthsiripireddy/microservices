package com.eazybytes.accounts.service;

public class Sample {

    public void create(String email){
        if(email.contains("@")){
            String name=email.substring(0,email.indexOf("@")+1);
        }
    }
}
