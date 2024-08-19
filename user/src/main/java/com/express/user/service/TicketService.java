package com.express.user.service;

import com.express.user.dto.TicketAndCustomerResponse;
import com.express.user.dto.TicketDetails;
import com.express.user.dto.TicketRequest;
import com.express.user.dto.TicketResponse;
import com.express.user.dtoMaper.TicketDtoMapper;
import com.express.user.entity.Address;
import com.express.user.entity.Customer;
import com.express.user.entity.Ticket;
import com.express.user.entity.User;
import com.express.user.exception.DataAccessException;
import com.express.user.projection.TicketProjection;
import com.express.user.repo.TicketRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UsersService usersService;
    private final EntityManager entityManager;
    private final CustomerService customerService;
    private final AddressService addressService;

    @Value("${AppConstants.Confirmed}")        // 1
    private String CONFIRMED;

    @Value("${AppConstants.Cancelled}")    // 2
    private String CANCELLED;

    @Value("${AppConstants.Pending}")     // 3
    private String PENDING;

    @Value("${AppConstants.Deleted}")   // 4
    private String DELETED;


    /*
        Create Ticket
        1. Validate User
        2. Validate Address  (Pickup and Drop) using Address ID
        3. Generate Ticket Number
        4. Create Ticket
     */

    public TicketResponse createTicket(TicketRequest ticketRequest) {
        log.info("TicketService::createTicket()::Creating ticket started");
        User issuer = null;
        Address pickupAdd = null;
        Address dropAdd = null;
        if (ticketRequest.getUserId()!=null){
            issuer = ServiceUtil.validateEntity(usersService.existById(ticketRequest.getUserId()),
                    () -> entityManager.getReference(User.class,ticketRequest.getUserId()), "User", ticketRequest.getUserId());
        }else {
            log.error("TicketService::createTicket()::User id is required to create a ticket");
            throw new IllegalArgumentException("Issuer id is required to create a ticket");
        }

        if (ticketRequest.getPickupPointId()!=null && ticketRequest.getPickupPointId()!=0){
            log.info("TicketService::createTicket()::Pickup point id is provided id:{}", ticketRequest.getPickupPointId());
            pickupAdd = ServiceUtil.validateEntity(addressService.isExistById(ticketRequest.getPickupPointId()),
                    () -> entityManager.getReference(Address.class, (ticketRequest.getPickupPointId())), "Address", ticketRequest.getPickupPointId());
        }
        if (ticketRequest.getDropPointId()!=null && ticketRequest.getDropPointId()!=0){
            log.info("TicketService::createTicket()::Drop point id is provided id:{}", ticketRequest.getDropPointId());
            dropAdd = ServiceUtil.validateEntity(addressService.isExistById(ticketRequest.getDropPointId()),
                    () -> entityManager.getReference(Address.class, ticketRequest.getDropPointId()), "Address", ticketRequest.getDropPointId());
        }
        ticketRequest.setTicketNumber(generateTicketNumber(issuer.getId().toString()));
        if (ticketRepository.existsByTicketNumber(ticketRequest.getTicketNumber())) {
            log.error("TicketService::createTicket()::Ticket number already exists");
            ticketRequest.setTicketNumber(generateTicketNumber(issuer.getId().toString()));
        }

        Customer customer = ServiceUtil.validateEntity(customerService.existById(ticketRequest.getCustomerId()),
                () -> entityManager.getReference(Customer.class, ticketRequest.getCustomerId()), "Customer", ticketRequest.getCustomerId());

        if (ticketRepository.existsByCustomerAndDate(customer, ticketRequest.getDate())) {
            log.warn("TicketService::createTicket()::Ticket already exists for customer on date: {}", ticketRequest.getDate());
            List<TicketProjection> ticketProjectionList = ticketRepository.findByCustomer(customer)
                    .orElseThrow(() -> new DataAccessException("Ticket not found with ticket number: " + ticketRequest.getTicketNumber()));
            ticketRequest.setMsg("Ticket already exists for customer on date: " + ticketRequest.getDate());
            ticketRequest.setStatus(PENDING);
        }else {
            ticketRequest.setStatus(CONFIRMED);
        }
        log.info("TicketService::createTicket()::Customer validated successfully");
        Ticket ticket = TicketDtoMapper.mapToTicket(ticketRequest, issuer,customer,pickupAdd,dropAdd);
        ticket = ticketRepository.save(ticket);
        log.info("TicketService::createTicket()::Creating ticket completed");
        return TicketDtoMapper.mapToTicketResponse(ticket);
    }


    public TicketResponse update(TicketRequest ticketRequest, Long ticketId) {
        log.info("TicketService::update()::Updating ticket started");
        if (!ticketRepository.existsById(ticketId)){
            log.error("TicketService::update()::Ticket not found with id: {}", ticketId);
            throw new DataAccessException("Ticket not found with id: " + ticketId);
        }
        User issuer = null;
        if (ticketRequest.getUserId()!=null) {
            issuer = ServiceUtil.validateEntity(usersService.existById(ticketRequest.getUserId()),
                    () -> entityManager.getReference(User.class, ticketRequest.getUserId()), "User", ticketRequest.getUserId());
        }else {
            log.error("TicketService::update()::User id is required to create a ticket");
            throw new IllegalArgumentException("Issuer id is required to create a ticket");
        }
        Address pickupAdd = ServiceUtil.validateEntity(addressService.isExistById(ticketRequest.getPickupPointId()),
                () -> entityManager.getReference(Address.class, ticketRequest.getPickupPointId()), "Address", ticketRequest.getPickupPointId());

        Address dropAdd = ServiceUtil.validateEntity(addressService.isExistById(ticketRequest.getDropPointId()),
                () -> entityManager.getReference(Address.class, ticketRequest.getDropPointId()), "Address", ticketRequest.getDropPointId());

        Customer customer = ServiceUtil.validateEntity(customerService.existById(ticketRequest.getCustomerId()),
                () -> entityManager.getReference(Customer.class, ticketRequest.getCustomerId()), "Customer", ticketRequest.getCustomerId());

        if (ticketRepository.existsByCustomerAndDate(customer, ticketRequest.getDate())) {
            log.warn("TicketService::update()::Ticket already exists for customer on date: {}", ticketRequest.getDate());
            List<TicketProjection> ticketProjectionList = ticketRepository.findByCustomer(customer)
                    .orElseThrow(() -> new DataAccessException("Ticket not found with ticket number: " + ticketRequest.getTicketNumber()));
            ticketRequest.setMsg("Ticket already exists for customer on date: " + ticketRequest.getDate());
            ticketRequest.setStatus(PENDING);
        }
        String ticketNumber = ticketRepository.findByTicketId(ticketId);
        Ticket ticket = TicketDtoMapper.mapToTicket(ticketRequest, issuer,customer,pickupAdd,dropAdd);
        ticket.setId(ticketId);
        ticket.setTicketNumber(ticketNumber);
        log.info("TicketService::update()::Updating ticket completed");
        return TicketDtoMapper.mapToTicketResponse(ticketRepository.save(ticket));
    }



    public TicketResponse getTicket(Long ticketId) {
        log.info("TicketService::getTicket()::Getting ticket started");
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new DataAccessException("Ticket not found with id: " + ticketId));
        log.info("TicketService::getTicket()::Getting ticket completed");
        return TicketDtoMapper.mapToTicketResponse(ticket);
    }

    public List<TicketResponse> getAllTickets() {
        log.info("TicketService::getTickets()::Getting tickets started");
        List<Ticket> tickets = ticketRepository.findAll();
        if (tickets.isEmpty()) {
            log.error("TicketService::getTickets()::No tickets found");
            throw new DataAccessException("No tickets found");
        }
        log.info("TicketService::getTickets()::Getting tickets completed");
        return tickets.stream().map(TicketDtoMapper::mapToTicketResponse).toList();
    }


    public List<TicketResponse> getTicketsByDate(LocalDate date) {
        log.info("TicketService::getTicketsByDate()::Getting tickets by date started");
        List<Ticket> tickets = ticketRepository.findByDate(date)
                .orElseThrow(() -> new DataAccessException("No tickets found for date: " + date));
        log.info("TicketService::getTicketsByDate()::Getting tickets by date completed");
        return tickets.stream().map(TicketDtoMapper::mapToTicketResponse).toList();
    }



    public String generateTicketNumber(String userId) {
        log.info("TicketService::generateTicketNumber()::Generating ticket number started");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateTimeString = now.format(formatter);
        int randomNum = ThreadLocalRandom.current().nextInt(100, 1000);
        log.info("TicketService::generateTicketNumber()::Generating ticket number completed");
        return dateTimeString+userId+randomNum;
    }


    public String getTicketNumber(Long ticketId) {
        log.info("TicketService::getTicketNumber()::Getting ticket number started");
        String ticketNumber = ticketRepository.findByTicketId(ticketId);
        log.info("TicketService::getTicketNumber()::Getting ticket number completed successfully with ticket number: {}", ticketNumber);
        return ticketNumber;
    }

    public void deleteTicket(Long ticketId) {
        log.info("TicketService::deleteTicket()::Deleting ticket started");
        if (!ticketRepository.existsById(ticketId)){
            log.error("TicketService::deleteTicket()::Ticket not found with id: {}", ticketId);
            throw new DataAccessException("Ticket not found with id: " + ticketId);
        }
        ticketRepository.deleteById(ticketId);
        log.info("TicketService::deleteTicket()::Deleting ticket completed successfully");
    }



    public void updateStatus(Long ticketId, int statusId) {
        log.info("TicketService::updateStatus()::Updating ticket status started");

        String status = switch (statusId) {
            case 1 -> CONFIRMED;
            case 2 -> CANCELLED;
            case 3 -> PENDING;
            case 4 -> DELETED;
            default -> {
                log.error("TicketService::updateStatus()::Invalid status id: {}", statusId);
                throw new IllegalArgumentException("Invalid status id: " + statusId);
            }
        };

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> {
                    log.error("TicketService::updateStatus()::Ticket not found with id: {}", ticketId);
                    return new DataAccessException("Ticket not found with id: " + ticketId);
                });

        ticket.setStatus(status);
        ticketRepository.save(ticket);
        log.info("TicketService::updateStatus()::Updating ticket status completed successfully");
    }


    // delete all
    public void deleteAll() {
        log.info("TicketService::deleteAll()::Deleting all tickets started");
        ticketRepository.deleteAll();
        log.info("TicketService::deleteAll()::Deleting all tickets completed successfully");
    }

    public List<TicketDetails> getTicketDetails(){
        log.info("TicketService::getTicketDetails()::Getting ticket details started");
        List<TicketProjection> ticketProjections = ticketRepository.findAllByDate(LocalDate.now());
        if (ticketProjections.isEmpty()){
            log.error("TicketService::getTicketDetails()::No tickets found");
            throw new DataAccessException("No tickets found");
        }

        Map<String, TicketDetails> ticketDetailsMap = new HashMap<>();

        for (TicketProjection ticket : ticketProjections) {
            String key = ticket.getDate().toString() + "-" + ticket.getToWhere();
            TicketDetails details = ticketDetailsMap.getOrDefault(key, new TicketDetails(ticket.getToWhere(), ticket.getDate(), 0, 0, 0, 0, 0));

            if (ticket.getStatus().equals("PENDING")) {
                details.setPending(details.getPending() + 1);
                details.setPendingCount(details.getPendingCount() + ticket.getMaleCount() + ticket.getFemaleCount());
            }else{
                details.setMaleCount(details.getMaleCount() + ticket.getMaleCount());
                details.setFemaleCount(details.getFemaleCount() + ticket.getFemaleCount());
                details.setTotal(details.getTotal() + ticket.getMaleCount() + ticket.getFemaleCount());
            }

            ticketDetailsMap.put(key, details);
        }

        List<TicketDetails> ticketDetails = new ArrayList<>(ticketDetailsMap.values());
        log.info("TicketService::getTicketDetails()::Getting ticket details completed successfully");
        return ticketDetails;
    }

    public List<TicketAndCustomerResponse> getAllTicketsByDateAndRoute(LocalDate date, String route) {
        log.info("TicketService::getAllTicketsByDateAndRoute()::Getting tickets by date and route started");
        List<Ticket> tickets = ticketRepository.findByDateAndToWhere(date, route);
        if (tickets.isEmpty()){
            log.error("TicketService::getAllTicketsByDateAndRoute()::No tickets found for date: {} and route: {}", date, route);
            throw new DataAccessException("No tickets found for date: " + date + " and route: " + route);
        }
        log.info("TicketService::getAllTicketsByDateAndRoute()::Getting tickets by date and route completed successfully");
        return tickets.stream().map(TicketDtoMapper::mapToTicketAndCustomerResponse).toList();
    }
}
