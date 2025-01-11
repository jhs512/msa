package com.ll.domain.post.author.repository;

import com.ll.domain.post.author.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
