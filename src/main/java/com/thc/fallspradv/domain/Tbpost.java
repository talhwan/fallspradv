package com.thc.fallspradv.domain;

import com.thc.fallspradv.dto.TbpostDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Table(indexes = {
        @Index(columnList = "deleted")
        ,@Index(columnList = "process")
        ,@Index(columnList = "createdAt")
        ,@Index(columnList = "modifiedAt")
})
@Entity
public class Tbpost extends AuditingFields {

    @Setter @Column(nullable = true) private String title;
    @Setter @Column(nullable = true, length=10000) private String content;
    @Setter @Column(nullable = true) private String author;

    protected Tbpost(){}
    private Tbpost(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
    public static Tbpost of(String title, String content, String author) {
        return new Tbpost(title, content, author);
    }

    public TbpostDto.CreateResDto toCreateResDto() {
        return TbpostDto.CreateResDto.builder().id(this.getId()).build();
    }
}
