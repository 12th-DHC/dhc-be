package project.dhc.cleaning;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface CleaningCheckRepository extends JpaRepository<CleaningCheck, Long> {
    Optional<CleaningCheck> roomNumberAndDate(
            Integer roomNumber,
            LocalDate date
    );
}
