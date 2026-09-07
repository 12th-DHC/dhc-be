package project.dhc.domain.admin.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.dhc.domain.admin.entity.Admin;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> { // Admin 테이블 관리, 기본키 타입 : Long

    Optional<Admin> findByAdminUsername(String adminUsername);
}
