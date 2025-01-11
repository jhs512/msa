package com.ll.domain.member.noti.repository;

import com.ll.domain.member.noti.entity.Noti;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotiRepository extends JpaRepository<Noti, Long> {
}
