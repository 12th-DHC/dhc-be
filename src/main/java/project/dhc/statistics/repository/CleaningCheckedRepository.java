package project.dhc.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.dhc.statistics.entity.CleaningChecked;

import java.time.LocalDate;
import java.util.List;

public interface CleaningCheckedRepository extends JpaRepository<CleaningChecked, Long> {

    // 기간 내 청소 검사 결과 조회
    List<CleaningChecked> findByCheckDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );
} // findByCheckDateBetween : Spring Data JPA가 이름을 보고 자동으로 만들어주는 조회 메서드 / 통계를 계산할 데이터를 가져오는 역할
