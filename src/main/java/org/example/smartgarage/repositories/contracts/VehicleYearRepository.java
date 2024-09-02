package org.example.smartgarage.repositories.contracts;

import org.example.smartgarage.models.VehicleYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleYearRepository extends JpaRepository<VehicleYear, Long> {
    Optional<VehicleYear> findByYear(int year);
}
