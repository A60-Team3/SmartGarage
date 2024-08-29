package org.example.smartgarage.services;

import org.example.smartgarage.models.ServiceType;
import org.example.smartgarage.repositories.contracts.OrderTypeRepository;
import org.example.smartgarage.services.contracts.OrderTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderTypeServiceImpl implements OrderTypeService {

    private final OrderTypeRepository orderTypeRepository;

    @Autowired
    public OrderTypeServiceImpl(OrderTypeRepository orderTypeRepository) {
        this.orderTypeRepository = orderTypeRepository;
    }

    @Override
    public ServiceType getById(long id) {
        return orderTypeRepository.findById(id).orElseThrow();
    }
}
