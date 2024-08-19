package com.express.user.repo;

import com.express.user.entity.Customer;
import com.express.user.entity.Ticket;
import com.express.user.projection.TicketProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {



    Optional<List<Ticket>> findByDate(LocalDate date);

    boolean existsByTicketNumber(String ticketNumber);

    boolean existsByCustomerAndDate(Customer customer, LocalDate date);

    Optional<List<TicketProjection>> findByCustomer(Customer customer);

    @Query("select t.ticketNumber from Ticket t where t.id = :ticketId")
    String findByTicketId(Long ticketId);

    @Query("select t from Ticket t where t.date >= :date")
    List<TicketProjection> findAllByDate(LocalDate date);

    List<Ticket> findByDateAndToWhere(LocalDate date, String toWhere);
}
