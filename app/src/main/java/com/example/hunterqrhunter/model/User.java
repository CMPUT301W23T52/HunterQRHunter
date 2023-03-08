package com.example.hunterqrhunter.model;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class User {
    String uid;
    String firstName;
    String lastName;
    int born;
    int hash;

    public User(String uid,String fn, String ln, int born, int hash) {
        this.uid = uid;
        this.firstName =  fn;
        this.lastName = ln;
        this.born = born;
        this.hash = hash;
    }


//    Array<HashQR> qrArray;

    public String getFirstName() {
        return this.firstName;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", this.uid);
        result.put("firstName", this.firstName);
        result.put("lastName", this.lastName);
        result.put("born", born);
        result.put("hash", hash);

        return result;
    }}