package com.ll.global.jpa.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseTime extends BaseEntity {
    @CreatedDate
    @Setter(AccessLevel.PROTECTED)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Setter(AccessLevel.PROTECTED)
    private LocalDateTime modifyDate;
}