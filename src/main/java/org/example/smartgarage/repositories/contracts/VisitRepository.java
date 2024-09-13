package org.example.smartgarage.repositories.contracts;

import org.example.smartgarage.models.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long>, JpaSpecificationExecutor<Visit> {

    List<Visit> findAllByScheduleDate(LocalDate date);
}
