package com.ll.domain.post.author.entity;

import com.ll.domain.member.member.entity.Member;
import com.ll.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;
import org.hibernate.annotations.Immutable;


@Entity
@Immutable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MEMBER")
public class Author extends BaseTime {
    @Column(length = 30, name = "NICKNAME")
    private String name;
    @Transient
    @Getter
    private boolean admin;

    public Author(Member member) {
        setId(member.getId());
        setCreateDate(member.getCreateDate());
        setModifyDate(member.getModifyDate());
        this.name = member.getNickname();
        this.admin = member.isAdmin();
    }
}