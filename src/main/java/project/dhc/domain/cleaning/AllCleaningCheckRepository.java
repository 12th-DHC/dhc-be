package project.dhc.domain.cleaning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface AllCleaningCheckRepository extends JpaRepository<AllCleaningCheck, Long> {
    Optional<AllCleaningCheck> findByRoomRoomNumberAndDate(
            Integer roomNumber,
            LocalDate date
    );
    @Query("""
    select new project.dhc.domain.cleaning.CleaningSearch(
        c.date,
        c.aPassed, c.aNotpassReason,
        c.aIndPassed, c.aIndNotpassReason,
        c.bPassed, c.bNotpassReason,
        c.bIndPassed, c.bIndNotpassReason
    )
    from AllCleaningCheck c
    where c.room.roomNumber = :roomNumber
      and c.date = :date
    """)
    Optional<CleaningSearch> findCleaningSearch(
            @Param("roomNumber") int roomNumber,
            @Param("date") LocalDate date
    );
}
