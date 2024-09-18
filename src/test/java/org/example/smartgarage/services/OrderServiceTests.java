package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.exceptions.UserMismatchException;
import org.example.smartgarage.exceptions.VisitMismatchException;
import org.example.smartgarage.models.*;
import org.example.smartgarage.repositories.contracts.HistoryRepository;
import org.example.smartgarage.repositories.contracts.OrderRepository;
import org.example.smartgarage.services.contracts.HistoryService;
import org.example.smartgarage.utils.filtering.OrderFilterOptions;
import org.example.smartgarage.utils.filtering.OrderSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.smartgarage.helpers.CreationHelper.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private HistoryService historyService;

    private Order order;
    private Visit visit;
    private UserEntity user;

    @BeforeEach
    public void setup() {
        order = createMockOrder();
        visit = createMockVisit();
        user = createMockEmployee();
    }

    @Test
    public void getAll_Should_CallRepository() {
        int offset = 0;
        int pageSize = 10;

        orderService.getAll(offset, pageSize);

        Mockito.verify(orderRepository, Mockito.times(1))
                .findAll(Mockito.any(Pageable.class));
    }

    @Test
    public void getAll_Should_CallRepository_When_NoFilter() {

        orderService.getAll();

        Mockito.verify(orderRepository, Mockito.times(1))
                .findAll();
    }

    @Test
    public void getAllByVisit_Should_CallRepository() {
        int offset = 0;
        int pageSize = 10;
        orderService.getAllByVisit(user.getId(), visit, offset, pageSize);

        Mockito.verify(orderRepository, Mockito.times(1))
                .findAllByVisitId(Mockito.any(Visit.class), Mockito.any(Pageable.class));
    }

    @Test
    public void getAllByVisit_Should_Throw_When_VisitAndUserDoNotMatch() {
        int offset = 0;
        int pageSize = 10;
        UserEntity customer = createMockCustomer();
        customer.setId(50);
        visit.setClient(customer);

        Assertions.assertThrows(UserMismatchException.class,
                () -> orderService.getAllByVisit(user.getId(), visit, offset, pageSize));
    }

    @Test
    public void getAllByUser_Should_CallRepository() {
        int offset = 0;
        int pageSize = 10;
        UserEntity mockUser = createMockUser();
        OrderFilterOptions filter = new OrderFilterOptions(null, null,
                null, null, null);

        orderService.getAllByUser(mockUser, offset, pageSize, filter);
        Mockito.verify(orderRepository, Mockito.times(1))
                .findAll(Mockito.any(OrderSpecification.class), Mockito.any(Pageable.class));
    }

    @Test
    public void getById_Should_ReturnOrder_When_IdIsValid() {
        Mockito.when(orderRepository.findById(order.getId()))
                .thenReturn(Optional.of(order));

        Order result = orderService.getById(order.getId());

        Assertions.assertEquals(order, result);
    }

    @Test
    public void getById_Should_Throw_When_OrderTypeNotFound() {
        Mockito.when(orderRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> orderService.getById(Mockito.anyLong()));
    }

    @Test
    public void getById_Should_ReturnOrder_When_UserAndVisitAreValid() {
        Mockito.when(orderRepository.findById(order.getId()))
                .thenReturn(Optional.of(order));

        Order result = orderService.getById(order.getId());

        Assertions.assertEquals(order, result);
        Mockito.verify(orderRepository, Mockito.times(1))
                .findById(Mockito.anyLong());

    }

    @Test
    public void getById_Should_Throw_When_NoSuchOrder() {
        visit.setServices(List.of(order));
        Mockito.when(orderRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(order))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> orderService.getById(1, visit, 1));
    }

    @Test
    public void getById_Should_Throw_When_UserMismatch() {

        Assertions.assertThrows(UserMismatchException.class,
                () -> orderService.getById(2, visit, 2));
    }

    @Test
    public void getById_Should_Throw_When_VisitMismatch() {
        visit.setServices(new ArrayList<>());
        Mockito.when(orderRepository.findById(Mockito.anyLong()))
                        .thenReturn(Optional.of(order));

        Assertions.assertThrows(VisitMismatchException.class,
                () -> orderService.getById(1, visit, 2));
    }

    @Test
    public void create_Should_Throw_When_UserMismatch() {
        Assertions.assertThrows(UserMismatchException.class,
                () -> orderService.create(order, 2, visit));
    }

    @Test
    public void create_Should_Throw_When_VisitAlreadyHasTheService() {
        Mockito.when(orderRepository
                .findByVisitIdAndServiceType(Mockito.any(Visit.class), Mockito.any(ServiceType.class)))
                        .thenReturn(order);

        Assertions.assertThrows(EntityDuplicateException.class,
                () -> orderService.create(order, 1, visit));
        Mockito.verify(orderRepository, Mockito.times(1))
                .findByVisitIdAndServiceType(visit, order.getServiceType());
    }

    @Test
    public void create_Should_ReturnOrder_When_InputIsValid() {
        Mockito.when(orderRepository
                        .findByVisitIdAndServiceType(Mockito.any(Visit.class), Mockito.any(ServiceType.class)))
                .thenReturn(null);

        Mockito.when(orderRepository
                        .findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(order));

        Order result = orderService.create(order, 1, visit);

        Assertions.assertEquals(result, order);
        Mockito.verify(orderRepository, Mockito.times(1))
                .findByVisitIdAndServiceType(visit, order.getServiceType());
        Mockito.verify(orderRepository, Mockito.times(1))
                .saveAndFlush(order);
    }

    @Test
    public void update_Should_Throw_When_VisitMismatch() {
        visit.setServices(new ArrayList<>());
        Mockito.when(orderRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(order));

        Assertions.assertThrows(VisitMismatchException.class,
                () -> orderService.update(1, order,1, visit));
    }

    @Test
    public void update_Should_Throw_When_UserMismatch() {
        Assertions.assertThrows(UserMismatchException.class,
                () -> orderService.update(1,order, 2, visit));
    }

    @Test
    public void update_Should_Throw_When_OrderNotFound(){
        Mockito.when(orderRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> orderService.update(1, order,1, visit));
    }

    @Test
    public void update_Should_Return_When_InputIsValid(){
        Mockito.when(orderRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(order))
                .thenReturn(Optional.of(order));

        visit.setServices(List.of(order));

        Order result = orderService.update(1, order, 1, visit);

        Assertions.assertEquals(result, order);
        Mockito.verify(orderRepository, Mockito.times(3))
                .findById(order.getId());
        Mockito.verify(orderRepository, Mockito.times(1))
                .save(order);
    }

    @Test
    public void delete_Should_Throw_When_VisitMismatch() {
        visit.setServices(new ArrayList<>());
        Mockito.when(orderRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(order));

        Assertions.assertThrows(VisitMismatchException.class,
                () -> orderService.delete(1, visit,1));
    }

    @Test
    public void delete_Should_Throw_When_UserMismatch() {
        Assertions.assertThrows(UserMismatchException.class,
                () -> orderService.delete(2,visit, 2));
    }

    @Test
    public void delete_Should_Throw_When_OrderNotFound(){
        Mockito.when(orderRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> orderService.update(1, order,1, visit));
    }

    @Test
    public void delete_Should_Remove_When_InputIsValid(){
        Mockito.when(orderRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(order))
                .thenReturn(Optional.of(order));

        visit.setServices(List.of(order));

        orderService.delete(1, visit, 1);

        Mockito.verify(orderRepository, Mockito.times(2))
                .findById(order.getId());
        Mockito.verify(orderRepository, Mockito.times(1))
                .delete(order);
    }
}
