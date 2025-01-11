package com.ll.global.serviceEvent.post.comment;

import com.ll.domain.post.comment.entity.PostComment;
import com.ll.domain.post.post.entity.Post;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PostCommentCreatedEvent extends ApplicationEvent {
    private final long postId;
    private final String postTitle;
    private final long postAuthorId;
    private final String postAuthorName;
    private final long postCommentId;
    private final String postCommentContent;
    private final long postCommentAuthorId;
    private final String postCommentAuthorName;
    private final String msg;

    public PostCommentCreatedEvent(PostComment postComment) {
        super(postComment);

        Post post = postComment.getPost();
        this.postId = post.getId();
        this.postTitle = post.getTitle();
        this.postAuthorId = post.getAuthor().getId();
        this.postAuthorName = post.getAuthor().getName();
        this.postCommentId = postComment.getId();
        this.postCommentContent = postComment.getContent();
        this.postCommentAuthorId = postComment.getAuthor().getId();
        this.postCommentAuthorName = postComment.getAuthor().getName();
        this.msg = """
                %s님이 당신의 %d번글(%s)에 댓글을 달았습니다.
                """
                .formatted(postAuthorName, postId, postTitle)
                .stripIndent()
                .trim();
    }
}
