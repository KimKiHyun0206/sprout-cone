package com.sproutcone.repository;

import com.sproutcone.domain.Runner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RunnerJapRepository extends JpaRepository<Runner, Long> {
    Optional<Runner> findByKakaoTokenId(Long kakaoTokenId);
    boolean existsByKakaoTokenId(Long kakaoTokenId);
    boolean deleteByKakaoTokenId(Long kakaoTokenId);
    Optional<Runner> findByEmail(String email);
}