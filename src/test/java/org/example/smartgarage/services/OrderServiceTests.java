package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.UserMismatchException;
import org.example.smartgarage.models.Order;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.Visit;
import org.example.smartgarage.repositories.contracts.OrderRepository;
import org.example.smartgarage.services.contracts.HistoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import static org.example.smartgarage.helpers.CreationHelper.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private HistoryService historyService;

    private Order order;
    private Visit visit;
    private UserEntity user;

    @BeforeEach
    public void setup(){
        order = createMockOrder();
        visit = createMockVisit();
        user = createMockEmployee();
    }

    @Test
    public void getAll_Should_CallRepository(){
        int offset = 0;
        int pageSize = 10;

        orderService.getAll(offset, pageSize);

        Mockito.verify(orderRepository, Mockito.times(1))
                .findAll(Mockito.any(Pageable.class));
    }

    @Test
    public void getAllByVisit_Should_CallRepository(){
        int offset = 0;
        int pageSize = 10;
        orderService.getAllByVisit(user.getId(), visit, offset, pageSize);

        Mockito.verify(orderRepository, Mockito.times(1))
                .findAllByVisitId(Mockito.any(Visit.class), Mockito.any(Pageable.class));
    }

    @Test
    public void getAllByVisit_Should_Throw_When_VisitAndUserDoNotMatch(){
        int offset = 0;
        int pageSize = 10;
        UserEntity customer = createMockCustomer();
        customer.setId(50);
        visit.setClient(customer);

        Assertions.assertThrows(UserMismatchException.class,
                () -> orderService.getAllByVisit(user.getId(), visit, offset, pageSize));
    }

    @Test
    public void getAllByUser_Should_CallRepository(){
        int offset = 0;
        int pageSize = 10;
    }
}
