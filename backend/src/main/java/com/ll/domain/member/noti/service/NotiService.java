package com.ll.domain.member.noti.service;

import com.ll.domain.member.member.entity.Member;
import com.ll.domain.member.noti.entity.Noti;
import com.ll.domain.member.noti.repository.NotiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotiService {
    private final NotiRepository notiRepository;

    public void send(Member receiver, Member occurredBy, String msg) {
        Noti noti = Noti.builder()
                .receiver(receiver)
                .occurredBy(occurredBy)
                .msg(msg)
                .build();

        notiRepository.save(noti);
    }
}
