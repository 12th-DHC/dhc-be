package project.dhc.domain.cleaning;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AllCleaningCheckRepository extends JpaRepository<AllCleaningCheck, Long> {
    Optional<AllCleaningCheck> findByRoomRoomNumberAndDate(
            Integer roomNumber,
            LocalDate date
    );
}
