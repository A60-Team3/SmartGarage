package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.Order;
import org.example.smartgarage.models.Visit;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    List<Order> getAll();

    Page<Order> getAll(int offset, int pageSize);

    Page<Order> getAllByVisit(long userId, Visit visitId, int offset, int pageSize);

    Order getById(long id);

    Order getById(long userId, Visit visit, long id);

    Order create(Order order, long userId, Visit visit);

    Order update(long id, Order order, long userId, Visit visit);

    void delete(long userId, Visit visit, long id);
}
