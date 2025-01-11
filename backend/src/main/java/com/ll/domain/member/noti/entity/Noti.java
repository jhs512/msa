package com.ll.domain.member.noti.entity;


import com.ll.domain.member.member.entity.Member;
import com.ll.global.jpa.entity.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Noti extends BaseTime {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member receiver;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member occurredBy;
    private String msg;
}

