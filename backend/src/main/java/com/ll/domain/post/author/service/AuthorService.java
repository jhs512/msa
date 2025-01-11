package com.ll.domain.post.author.service;

import com.ll.domain.post.author.repository.AuthorRepository;
import com.ll.domain.post.author.entity.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public Author getReferenceById(Long id) {
        return authorRepository.getReferenceById(id);
    }
}
