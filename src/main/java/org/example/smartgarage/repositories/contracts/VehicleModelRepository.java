package org.example.smartgarage.repositories.contracts;

import org.example.smartgarage.models.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleModelRepository extends JpaRepository<VehicleModel, Long>, JpaSpecificationExecutor<VehicleModel> {
    Optional<VehicleModel> findByModelNameIgnoreCase(String modelName);
}
