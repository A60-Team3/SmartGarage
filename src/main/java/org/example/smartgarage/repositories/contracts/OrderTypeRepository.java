package org.example.smartgarage.repositories.contracts;

import org.example.smartgarage.models.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTypeRepository extends JpaRepository<ServiceType, Long> {

}
