package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.exceptions.UserMismatchException;
import org.example.smartgarage.models.Order;
import org.example.smartgarage.models.Visit;
import org.example.smartgarage.repositories.contracts.OrderRepository;
import org.example.smartgarage.services.contracts.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Page<Order> getAll(int offset, int pageSize) {
        return orderRepository.findAll(PageRequest.of(offset, pageSize));
    }

    @Override
    public Page<Order> getAllByVisit(long userId, Visit visit, int offset, int pageSize) {

        if(visit.getClient().getId() != userId){
            throw new UserMismatchException("Client has no such visit");
        }

        return orderRepository.findAllByVisitId(visit, PageRequest.of(offset, pageSize));
    }

    @Override
    public Order getById(long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    @Override
    public Order getById(long userId, Visit visit, long id) {
        checkForVisit(userId, visit);

        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    @Override
    public Order create(Order order, long userId, Visit visit) {
        checkForVisit(userId, visit);
        order.setVisitId(visit);

        orderRepository.save(order);
        return orderRepository.findById(order.getId()).get();
    }

    @Override
    public Order update(long id,
                        Order order,
                        long userId,
                        Visit visit) {
        checkForVisit(userId, visit);

        Order repoOrder = orderRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Order not found"));

        repoOrder.setServiceType(order.getServiceType());

        orderRepository.save(repoOrder);
        return orderRepository.findById(id).get();
    }

    @Override
    public void delete(long userId, Visit visit, long id) {

        checkForVisit(userId, visit);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        orderRepository.delete(order);
    }

    private void checkForVisit(long userId, Visit visit) {
        if(visit.getClient().getId() != userId){
            throw new UserMismatchException("Client has no such visit");
        }
    }
}
