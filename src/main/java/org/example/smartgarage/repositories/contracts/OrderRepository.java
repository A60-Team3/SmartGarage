package org.example.smartgarage.repositories.contracts;

import org.example.smartgarage.models.Service;
import org.example.smartgarage.models.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Service, Long> {
    Page<Service> findAllByVisitId(Visit visit, Pageable pageable);
}
