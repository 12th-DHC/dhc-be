package project.dhc.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.dhc.Entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> { // Admin 테이블 관리, 기본키 타입 : Long

}
