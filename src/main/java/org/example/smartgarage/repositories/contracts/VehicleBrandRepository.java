package org.example.smartgarage.repositories.contracts;

import org.example.smartgarage.models.VehicleBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleBrandRepository extends JpaRepository<VehicleBrand, Long>, JpaSpecificationExecutor<VehicleBrand> {
    Optional<VehicleBrand> findByBrandNameIgnoreCase(String brandName);
}
