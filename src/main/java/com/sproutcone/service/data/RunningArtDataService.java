package com.sproutcone.service.data;

import com.sproutcone.repository.RunningArtJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RunningArtDataService {
    private final RunningArtJpaRepository runningArtJpaRepository;
}