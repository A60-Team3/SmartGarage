package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.models.ServiceType;
import org.example.smartgarage.repositories.contracts.OrderTypeRepository;
import org.example.smartgarage.utils.filtering.OrderTypeFilterOptions;
import org.example.smartgarage.utils.filtering.OrderTypeSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.example.smartgarage.helpers.CreationHelper.createMockServiceType;

@ExtendWith(MockitoExtension.class)
public class OrderTypeServiceTests {

    @InjectMocks
    private OrderTypeServiceImpl orderTypeService;

    @Mock
    private OrderTypeRepository orderTypeRepository;

    private ServiceType orderType;

    @BeforeEach
    public void setup() {
        orderType = createMockServiceType();
    }

    @Test
    public void getAll_Should_CallRepository() {
        int offset = 0;
        int pageSize = 10;
        OrderTypeFilterOptions filterOptions = new OrderTypeFilterOptions(null,
                                                                            null,
                                                                            null,
                                                                            null,
                null);

        orderTypeService.getAll(offset, pageSize, filterOptions);

        Mockito.verify(orderTypeRepository, Mockito.times(1))
                .findAll(Mockito.any(OrderTypeSpecification.class), Mockito.any(Pageable.class));
    }

    @Test
    public void getById_Should_ReturnOrderType_When_IdIsValid() {
        Mockito.when(orderTypeRepository.findById(orderType.getId()))
                .thenReturn(Optional.of(orderType));

        ServiceType result = orderTypeService.getById(orderType.getId());

        Assertions.assertEquals(orderType, result);
    }

    @Test
    public void getById_Should_Throw_When_OrderTypeNotFound() {
        Mockito.when(orderTypeRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> orderTypeService.getById(Mockito.anyLong()));
    }

    @Test
    public void create_Should_CreateOrderType(){
        Mockito.when(orderTypeRepository.findByServiceName(orderType.getServiceName()))
                .thenReturn(null);
        Mockito.when(orderTypeRepository.findById(orderType.getId()))
                .thenReturn(Optional.of(orderType));

        orderTypeService.create(orderType);

        Mockito.verify(orderTypeRepository, Mockito.times(1))
                .save(orderType);
    }

    @Test
    public void create_Should_Throw_When_DuplicateExists(){
        Mockito.when(orderTypeRepository.findByServiceName(orderType.getServiceName()))
                .thenReturn(orderType);

        Assertions.assertThrows(EntityDuplicateException.class, () -> orderTypeService.create(orderType));
    }

    @Test
    public void update_Should_UpdateOrderType(){
        Mockito.when(orderTypeRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(orderType));
        Mockito.when(orderTypeRepository.findByServiceName(orderType.getServiceName()))
                .thenReturn(orderType);

        orderTypeService.update(orderType.getId(), orderType);

        Mockito.verify(orderTypeRepository, Mockito.times(1))
                .save(orderType);
    }

    @Test
    public void update_Should_Throw_When_OrderTypeNotFound(){
        Mockito.when(orderTypeRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () ->orderTypeService.update(orderType.getId(), orderType));
    }

    @Test
    public void update_Should_Throw_When_DuplicateExists(){
        ServiceType duplicate = createMockServiceType();
        duplicate.setId(2);

        Mockito.when(orderTypeRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(orderType));
        Mockito.when(orderTypeRepository.findByServiceName(orderType.getServiceName()))
                .thenReturn(duplicate);

        Assertions.assertThrows(EntityDuplicateException.class,
                () -> orderTypeService.update(orderType.getId(), orderType));
    }

    @Test
    public void delete_Should_DeleteOrderType(){
        Mockito.when(orderTypeRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(orderType));

        orderTypeService.delete(orderType.getId());

        Mockito.verify(orderTypeRepository, Mockito.times(1))
                .delete(orderType);
    }

    @Test
    public void delete_Should_Throw_When_OrderTypeNotFound(){
        Mockito.when(orderTypeRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> orderTypeService.delete(orderType.getId()));
    }
}
