package com.ll.global.serviceEvent.post.comment;

import com.ll.domain.post.comment.entity.PostComment;
import com.ll.domain.post.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentCreatedEvent {
    private long postId;
    private String postTitle;
    private long postAuthorId;
    private String postAuthorName;
    private long postCommentId;
    private String postCommentContent;
    private long postCommentAuthorId;
    private String postCommentAuthorName;
    private String msg;

    public PostCommentCreatedEvent(PostComment postComment) {
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
