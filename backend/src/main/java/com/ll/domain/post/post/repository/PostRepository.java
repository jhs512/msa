package com.ll.domain.post.post.repository;

import com.ll.domain.post.author.entity.Author;
import com.ll.domain.post.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByIdDesc();

    Optional<Post> findFirstByOrderByIdDesc();

    Page<Post> findByListed(boolean listed, PageRequest pageRequest);

    Page<Post> findByListedAndTitleLike(boolean listed, String titleLike, PageRequest pageRequest);

    Page<Post> findByListedAndContentLike(boolean listed, String contentLike, PageRequest pageRequest);

    Page<Post> findByAuthor(Author author, PageRequest pageRequest);

    Page<Post> findByAuthorAndTitleLike(Author author, String titleLike, PageRequest pageRequest);

    Page<Post> findByAuthorAndContentLike(Author author, String contentLike, PageRequest pageRequest);
}