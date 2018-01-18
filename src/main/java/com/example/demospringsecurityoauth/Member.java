package com.example.demospringsecurityoauth;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class Member implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String username;
    private String remark;

    public Member(){}
    public Member(String name, String username, String remark){
        this.name = name;
        this.username = username;
        this.remark = remark;
    }

}
