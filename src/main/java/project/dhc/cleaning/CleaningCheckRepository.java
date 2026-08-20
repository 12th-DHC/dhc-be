package project.dhc.cleaning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface CleaningCheckRepository extends JpaRepository<CleaningCheck, Long> {
    Optional<CleaningCheck> findByRoomRoomNumberAndDate(
            Integer roomNumber,
            LocalDate date
    );
    @Query("""
    select new project.dhc.cleaning.CleaningSearch(
        c.date,
        c.aPassed, c.aNotpassReason,
        c.aIndPassed, c.aIndNotpassReason,
        c.bPassed, c.bNotpassReason,
        c.bIndPassed, c.bIndNotpassReason
    )
    from CleaningCheck c
    where c.room.roomNumber = :roomNumber
      and c.date = :date
    """)
    Optional<CleaningSearch> findCleaningSearch(
            @Param("roomNumber") int roomNumber,
            @Param("date") LocalDate date
    );
}
