package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.Visit;
import org.example.smartgarage.repositories.contracts.OrderRepository;
import org.example.smartgarage.services.contracts.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<org.example.smartgarage.models.Service> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Page<org.example.smartgarage.models.Service> getAll(int offset, int pageSize) {
        return orderRepository.findAll(PageRequest.of(offset, pageSize));
    }

    @Override
    public org.example.smartgarage.models.Service getById(long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    @Override
    public org.example.smartgarage.models.Service create(org.example.smartgarage.models.Service order, Visit visit) {
        order.setVisitId(visit);

        orderRepository.save(order);
        return orderRepository.findById(order.getId()).get();
    }

    @Override
    public org.example.smartgarage.models.Service update(long id, org.example.smartgarage.models.Service order, Visit visit) {
        org.example.smartgarage.models.Service repoOrder = orderRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Order not found"));

        repoOrder.setServiceType(order.getServiceType());
        repoOrder.setVisitId(order.getVisitId());

        orderRepository.save(repoOrder);
        return orderRepository.findById(id).get();
    }

    @Override
    public void delete(long id, UserEntity user) {
        org.example.smartgarage.models.Service order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        orderRepository.delete(order);
    }
}
