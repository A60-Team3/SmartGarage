package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.ServiceType;

public interface OrderTypeService {
    ServiceType getById(long id);
}
