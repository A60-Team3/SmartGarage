package org.example.smartgarage.repositories.contracts;

import org.example.smartgarage.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Service, Long> {

}
