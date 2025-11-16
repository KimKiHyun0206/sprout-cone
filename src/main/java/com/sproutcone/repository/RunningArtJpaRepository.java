package com.sproutcone.repository;

import com.sproutcone.domain.RunningArt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunningArtJpaRepository extends JpaRepository<RunningArt, Long> {
    List<RunningArt> findByRunnerId(Long runnerId);
}