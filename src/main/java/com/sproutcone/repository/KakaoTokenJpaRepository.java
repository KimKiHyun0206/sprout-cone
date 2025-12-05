package com.sproutcone.repository;

import com.sproutcone.domain.KakaoToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KakaoTokenJpaRepository extends JpaRepository<KakaoToken, Long> {
}