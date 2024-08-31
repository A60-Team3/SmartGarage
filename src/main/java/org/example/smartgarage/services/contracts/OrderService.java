package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.Service;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.Visit;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    List<Service> getAll();

    Page<Service> getAll(int offset, int pageSize);

    Page<Service> getAllByVisit(long userId, Visit visitId, int offset, int pageSize);

    Service getById(long id);

    Service getById(long userId, Visit visit, long id);

    Service create(Service order, long userId, Visit visit);

    Service update(long id, Service order, long userId, Visit visit);

    void delete(long userId, Visit visit, long id);
}
