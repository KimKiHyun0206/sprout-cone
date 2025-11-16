package com.sproutcone.repository;

import com.sproutcone.domain.Runner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunnerJapRepository extends JpaRepository<Runner, Long> {
}