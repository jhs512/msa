package com.ll.domain.member.noti.eventListener;

import com.ll.domain.member.member.entity.Member;
import com.ll.domain.member.member.service.MemberService;
import com.ll.domain.member.noti.service.NotiService;
import com.ll.global.serviceEvent.post.comment.PostCommentCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class NotiEventListener {
    private final MemberService memberService;
    private final NotiService notiService;

    @KafkaListener(topics = "service.post.comment.created", groupId = "1")
    public void listen(PostCommentCreatedEvent event) {
        Member receiver = memberService.findById(event.getPostAuthorId()).get();
        Member occurredBy = memberService.findById(event.getPostCommentAuthorId()).get();

        notiService.send(receiver, occurredBy, event.getMsg());
    }

    @KafkaListener(topics = "service.post.comment.created-dlt", groupId = "1")
    public void consumePostCommentCreatedEventDLT(byte[] in) {
        String message = new String(in);
        System.out.println("failed message: " + message);
    }
}
