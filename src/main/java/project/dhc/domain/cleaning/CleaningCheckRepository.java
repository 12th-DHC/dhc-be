package project.dhc.domain.cleaning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface CleaningCheckRepository extends JpaRepository<CleaningCheck, Long> {
    @Query("""
            select c from CleaningCheck c
            join fetch c.room
            where c.date between :startDate and :endDate
            order by c.room.roomNumber, c.date, c.recordId
            """)
    List<CleaningCheck> findWeeklyChecks(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    Optional<CleaningCheck> findByRoomRoomNumberAndDate(
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
    from CleaningCheck c
    where c.room.roomNumber = :roomNumber
      and c.date = :date
    """)
    Optional<CleaningSearch> findCleaningSearch(
            @Param("roomNumber") int roomNumber,
            @Param("date") LocalDate date
    );
}
