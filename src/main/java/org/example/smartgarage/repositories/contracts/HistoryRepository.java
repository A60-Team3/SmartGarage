package org.example.smartgarage.repositories.contracts;

import org.example.smartgarage.models.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<EventLog, Long> {
}
