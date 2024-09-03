package com.thc.fallspradv.domain;

import com.thc.fallspradv.dto.TbuserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Table(indexes = {
        @Index(columnList = "deleted")
        ,@Index(columnList = "process")
        ,@Index(columnList = "createdAt")
        ,@Index(columnList = "modifiedAt")
})
@Entity
public class Tbuser extends AuditingFields {

    @Setter @Column(nullable = false, unique = true) private String username;
    @Setter @Column(nullable = false) private String password;
    @Setter @Column(nullable = true) private String name;
    @Setter @Column(nullable = true) private String nick;
    @Setter @Column(nullable = true) private String phone;
    @Setter @Column(nullable = true) private String gender;
    @Setter @Column(nullable = true, length=10000) @Lob private String content; // 본문
    @Setter @Column(nullable = true) private String img;

    protected Tbuser(){}
    private Tbuser(String username, String password, String name, String nick, String phone, String gender, String content, String img) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.nick = nick;
        this.phone = phone;
        this.gender = gender;
        this.content = content;
        this.img = img;
    }
    public static Tbuser of(String username, String password, String name, String nick, String phone, String gender, String content, String img) {
        return new Tbuser(username, password, name, nick, phone, gender, content, img);
    }

    public TbuserDto.CreateResDto toCreateResDto() {
        return TbuserDto.CreateResDto.builder().id(this.getId()).build();
    }
}
