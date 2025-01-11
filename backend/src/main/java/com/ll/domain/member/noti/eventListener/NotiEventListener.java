package com.ll.domain.member.noti.eventListener;

import com.ll.domain.member.member.entity.Member;
import com.ll.domain.member.member.service.MemberService;
import com.ll.domain.member.noti.service.NotiService;
import com.ll.global.serviceEvent.post.comment.PostCommentCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class NotiEventListener {
    private final MemberService memberService;
    private final NotiService notiService;

    @EventListener
    @Async
    public void listen(PostCommentCreatedEvent event) {
        Member receiver = memberService.findById(event.getPostAuthorId()).get();
        Member occurredBy = memberService.findById(event.getPostCommentAuthorId()).get();

        notiService.send(receiver, occurredBy, event.getMsg());
    }
}
