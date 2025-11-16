package com.sproutcone.service.data;

import com.sproutcone.repository.RunnerJapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RunnerDataService {
    private final RunnerJapRepository runnerJapRepository;
}