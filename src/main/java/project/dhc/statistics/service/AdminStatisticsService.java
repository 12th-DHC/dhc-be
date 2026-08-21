package project.dhc.statistics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.dhc.statistics.repository.CleaningCheckedRepository;

@Service
@RequiredArgsConstructor
public class AdminStatisticsService {

    private final CleaningCheckedRepository cleaningCheckedRepository;
}
