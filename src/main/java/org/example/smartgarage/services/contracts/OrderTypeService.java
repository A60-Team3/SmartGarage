package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.ServiceType;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface OrderTypeService {
    List<ServiceType> getAll();

    Page<ServiceType> getAll(int offset, int pageSize);

    ServiceType getById(long id);

    ServiceType getByName(String serviceName);

    ServiceType getByPrice(BigDecimal price);

    ServiceType create(ServiceType serviceType);

    ServiceType update(long id, ServiceType serviceType);

    void delete(long id);

}
