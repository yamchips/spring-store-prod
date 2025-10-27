package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.users.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = "items.product")
    @Query("select o from Order o where o.id = :id")
    Optional<Order> findOrderWithItems(@Param("id") Long id);

    @EntityGraph(attributePaths = "items.product")
    @Query("select o from Order o where o.customer = :user")
    List<Order> findAllByCustomer(@Param("user") User user);
}
