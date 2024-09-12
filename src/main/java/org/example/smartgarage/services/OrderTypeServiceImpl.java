package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.models.ServiceType;
import org.example.smartgarage.repositories.contracts.OrderTypeRepository;
import org.example.smartgarage.services.contracts.OrderTypeService;
import org.example.smartgarage.utils.filtering.OrderTypeFilterOptions;
import org.example.smartgarage.utils.filtering.OrderTypeSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderTypeServiceImpl implements OrderTypeService {

    private static final String SERVICE_ALREADY_EXISTS = "Service already exists";
    private static final String SERVICE_NOT_FOUND = "Service not found";
    private final OrderTypeRepository orderTypeRepository;

    @Autowired
    public OrderTypeServiceImpl(OrderTypeRepository orderTypeRepository) {
        this.orderTypeRepository = orderTypeRepository;
    }

    @Override
    public List<ServiceType> getAll() {
        return orderTypeRepository.findAll();
    }

    @Override
    public Page<ServiceType> getAll(int offset, int pageSize) {
        return orderTypeRepository.findAll(PageRequest.of(offset, pageSize));
    }

    @Override
    public Page<ServiceType> getAll(int offset, int pageSize, OrderTypeFilterOptions orderTypeFilterOptions) {
        OrderTypeSpecification orderTypeSpecification = new OrderTypeSpecification(orderTypeFilterOptions);

        Pageable pageable = PageRequest.of(offset, pageSize);
        return orderTypeRepository.findAll(orderTypeSpecification, pageable);
    }

    @Override
    public ServiceType getById(long id) {
        return orderTypeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(SERVICE_NOT_FOUND));
    }

    @Override
    public ServiceType findByName(String serviceName) {
        return orderTypeRepository.findByServiceName(serviceName);
    }

    @Override
    public ServiceType getByPrice(BigDecimal price) {
        return orderTypeRepository.findByServicePrice(price);
    }

    @Override
    public ServiceType create(ServiceType serviceType) {

        ServiceType existingOrderType = orderTypeRepository.findByServiceName(serviceType.getServiceName());
        if (existingOrderType != null){
            throw new EntityDuplicateException(SERVICE_ALREADY_EXISTS);
        }

        orderTypeRepository.save(serviceType);
        return orderTypeRepository.findById(serviceType.getId()).get();
    }

    @Override
    public ServiceType update(long id, ServiceType serviceType) {
        ServiceType repoOrderType = orderTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(SERVICE_NOT_FOUND));

        ServiceType existingOrderType = orderTypeRepository.findByServiceName(serviceType.getServiceName());
        if (existingOrderType != null){
            if (existingOrderType.getId() != repoOrderType.getId()){
                throw new EntityDuplicateException(SERVICE_ALREADY_EXISTS);
            }
        }

        repoOrderType.setServiceName(serviceType.getServiceName());
        repoOrderType.setServicePrice(serviceType.getServicePrice());
        orderTypeRepository.save(repoOrderType);

        return orderTypeRepository.findById(id).get();
    }

    @Override
    public void delete(long id) {
        ServiceType orderType = orderTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(SERVICE_NOT_FOUND));

        orderTypeRepository.delete(orderType);
    }
}
