package org.example.smartgarage.repositories.contracts;

import org.example.smartgarage.models.VehicleBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleBrandRepository extends JpaRepository<VehicleBrand, Long> {

}
