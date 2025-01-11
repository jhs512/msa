package com.ll.global.rq;

import com.ll.domain.member.member.entity.Member;
import com.ll.domain.member.member.service.MemberService;
import com.ll.domain.post.author.entity.Author;
import com.ll.domain.post.author.service.AuthorService;
import com.ll.global.security.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Arrays;
import java.util.Optional;

// Request/Response 를 추상화한 객체
// Request, Response, Cookie, Session 등을 다룬다.
@RequestScope
@Component
@RequiredArgsConstructor
public class Rq {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final MemberService memberService;
    private final AuthorService authorService;
    private Member _actor;

    public void setLogin(Member member) {
        UserDetails user = new SecurityUser(
                member.getId(),
                member.getUsername(),
                "",
                member.getAuthorities()
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                user.getPassword(),
                user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Author getAuthorActor() {
        if (getActor() == null) return null;

        return new Author(getActor());
    }

    public Member getActor() {
        if (_actor != null) return _actor;

        _actor = Optional.ofNullable(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                )
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof SecurityUser)
                .map(principal -> (SecurityUser) principal)
                .map(securityUser -> new Member(securityUser.getId(), securityUser.getUsername()))
                .orElse(null);

        return _actor;
    }

    public void setCookie(String name, String value) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .domain("localhost")
                .sameSite("Strict")
                .secure(true)
                .httpOnly(true)
                .build();
        resp.addHeader("Set-Cookie", cookie.toString());
    }

    public String getCookieValue(String name) {
        return Optional
                .ofNullable(req.getCookies())
                .stream() // 1 ~ 0
                .flatMap(cookies -> Arrays.stream(cookies))
                .filter(cookie -> cookie.getName().equals(name))
                .map(cookie -> cookie.getValue())
                .findFirst()
                .orElse(null);
    }

    public void deleteCookie(String name) {
        ResponseCookie cookie = ResponseCookie.from(name, null)
                .path("/")
                .domain("localhost")
                .sameSite("Strict")
                .secure(true)
                .httpOnly(true)
                .maxAge(0)
                .build();

        resp.addHeader("Set-Cookie", cookie.toString());
    }

    public void setHeader(String name, String value) {
        resp.setHeader(name, value);
    }

    public String getHeader(String name) {
        return req.getHeader(name);
    }

    // getActor 와 다르게
    // 이 함수에서 리턴하는 것은 야매가 아니다.
    public Optional<Member> findByActor() {
        Member actor = getActor();

        if (actor == null) return Optional.empty();

        return Optional.ofNullable(memberService.getReferenceById(actor.getId()));
    }

    public Optional<Author> findByAuthorActor() {
        Author author = getAuthorActor();

        if (author == null) return Optional.empty();

        return Optional.ofNullable(authorService.getReferenceById(author.getId()));
    }


    public record AuthTokens(String apiKey, String accessToken) {
    }

    public AuthTokens makeAuthCookies(Member member) {
        String accessToken = memberService.genAccessToken(member);

        setHeader("Authorization", "Bearer " + member.getApiKey() + " " + accessToken);

        setCookie("accessToken", accessToken);
        setCookie("apiKey", member.getApiKey());

        return new AuthTokens(member.getApiKey(), accessToken);
    }
}
