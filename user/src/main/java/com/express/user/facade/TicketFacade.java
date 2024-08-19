package com.express.user.facade;

import com.express.user.dto.*;
import com.express.user.dtoMaper.AddressDtoMapper;
import com.express.user.exception.ResourceNotFoundException;
import com.express.user.service.AddressService;
import com.express.user.service.CustomerService;
import com.express.user.service.TicketService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketFacade {
    private final TicketService ticketService;
    private final CustomerService customerService;
    private final AddressService addressService;


    /*
         Create Customer if not exists
         Create Address if not exists for the customer
         Create Ticket
     */


    @Transactional
    public TicketResponse createTicket(TicketRequest ticketRequest){
        log.info("TicketFacade::createTicket()::Creating ticket started");
        CustomerResponse customerResponse = null;
        if (customerService.existByMobile(ticketRequest.getMobileNumber())) {
            customerResponse = customerService.findCustomerResponseByMobile(ticketRequest.getMobileNumber());
            log.info("TicketFacade::createTicket()::Customer fetched from database");
        }else {
            CustomerRequest customerRequest = CustomerRequest.builder()
                    .name(ticketRequest.getCustomerName())
                    .mobile(ticketRequest.getMobileNumber())
                    .build();
            customerResponse = customerService.create(customerRequest);
            log.info("TicketFacade::createTicket()::Customer created");
        }
        if (customerResponse == null){
            log.error("TicketFacade::createTicket()::Customer not found or created");
            throw new RuntimeException("Customer not found or created");
        }
        ticketRequest.setCustomerId(customerResponse.getId());

        if (!ticketRequest.getPickupPoint().isEmpty()){
            log.info("TicketFacade::CreateTicket()::Pickup Address is not Empty");
            if (addressService.isAddressExistAndCustomer(ticketRequest.getPickupPoint(),customerResponse.getId())) {
                log.info("TicketFacade::CreateTicket()::PickupAddress is not Empty and isExistAndCustomer");
                AddressResponse addressResponse = addressService.getAddressByAddressAndCustomer(ticketRequest.getPickupPoint(),customerResponse.getId());
                ticketRequest.setPickupPointId(addressResponse.getId());
                log.info("TicketFacade::createTicket()::Pickup Address fetched from database");
            }else {
                log.info("TicketFacade::CreateTicket()::PickupAddress is not Empty and Not ExistAndCustomer");
                AddressResponse pickupAddress =  addressService.createAddress(AddressDtoMapper.toAddressRequest(ticketRequest.getPickupPoint(),customerResponse.getId()));
                ticketRequest.setPickupPointId(pickupAddress.getId());
                log.info("TicketFacade::createTicket()::Pickup Address created with id: {}",pickupAddress.getId());
            }
        }
        if (!ticketRequest.getDropPoint().isEmpty()){
            log.info("TicketFacade::CreateTicket()::Drop Address is not Empty");
            if (addressService.isAddressExistAndCustomer(ticketRequest.getDropPoint(),customerResponse.getId())) {
                log.info("TicketFacade::CreateTicket()::DropAddress is not Empty and isExistAndCustomer");
                AddressResponse addressResponse = addressService.getAddressByAddressAndCustomer(ticketRequest.getDropPoint(),customerResponse.getId());
                ticketRequest.setDropPointId(addressResponse.getId());
                log.info("TicketFacade::createTicket()::Drop Address fetched from database");
            }else {
                log.info("TicketFacade::CreateTicket()::DropAddress is not Empty and Not ExistAndCustomer");
                AddressResponse dropAddress =  addressService.createAddress(AddressDtoMapper.toAddressRequest(ticketRequest.getDropPoint(),customerResponse.getId()));
                ticketRequest.setDropPointId(dropAddress.getId());
                log.info("TicketFacade::createTicket()::Drop Address created with id: {}",dropAddress.getId());
            }
        }
        TicketResponse ticketResponse = ticketService.createTicket(ticketRequest);
        log.info("TicketFacade::createTicket()::Creating ticket completed");
        return ticketResponse;
    }

    /*
        Update Ticket
        0. validate the customer
        1. if pickup id or drop id is not null then check if address is null not create address
        2. update ticket


        Address validation
        1. if address id is not null then check if address exist or not
        2. if address exist with id then update the address for the customer
        3. if address not exist with id then create the address for the customer

        Customer Validation
        1. if customer id is not null then check if customer exist or not
        2. if customer exist with id then check the customer name and mobile number

     */


    @Transactional
    public TicketResponse updateTicket(TicketRequest ticketRequest, Long ticketId){
        log.info("TicketFacade::updateTicket()::Updating ticket started");
        if (!customerService.existById(ticketRequest.getCustomerId())) {
            log.error("TicketFacade::updateTicket()::Customer not found with id: {}", ticketRequest.getCustomerId());
            throw new ResourceNotFoundException("Customer not found with id: " + ticketRequest.getCustomerId());
        }
        if (ticketRequest.getPickupPointId()!=null) {
            log.info("TicketFacade::updateTicket()::Pickup Address exist with id: {}", ticketRequest.getPickupPointId());
            if (!addressService.isExistById(ticketRequest.getPickupPointId())) {
                log.error("TicketFacade::updateTicket()::Pickup Address not found with id: {}", ticketRequest.getPickupPointId());
                throw new ResourceNotFoundException("Pickup Address not found with id: " + ticketRequest.getPickupPointId());
            } else {
                if (!ticketRequest.getPickupPoint().isEmpty()) {
                    log.info("TicketFacade::updateTicket()::Pickup Address is not empty");
                    AddressResponse addressResponse = addressService.getAddress(ticketRequest.getPickupPointId());
                    if (!addressResponse.getAddress().equals(ticketRequest.getPickupPoint()) && addressResponse.getId().equals(ticketRequest.getPickupPointId())) {
                        addressService.updateAddress(ticketRequest.getPickupPointId(), AddressDtoMapper.toAddressRequest(ticketRequest.getPickupPoint(), ticketRequest.getCustomerId()));
                        log.info("TicketFacade::updateTicket()::Pickup Address updated");
                    }
                }
            }
        }else{
            if (!ticketRequest.getPickupPoint().isEmpty()) {
                log.warn("TicketFacade::updateTicket()::Pickup Address is not empty");
                AddressResponse pickupAddress = addressService.createAddress(AddressDtoMapper.toAddressRequest(ticketRequest.getPickupPoint(), ticketRequest.getCustomerId()));
                ticketRequest.setPickupPointId(pickupAddress.getId());
                log.info("TicketFacade::updateTicket()::Pickup Address created with id: {}", pickupAddress.getId());
            }
        }

        if (ticketRequest.getDropPointId()!=null) {
            log.info("TicketFacade::updateTicket()::Drop Address exist with id: {}", ticketRequest.getDropPointId());
            if (!addressService.isExistById(ticketRequest.getDropPointId())) {
                log.error("TicketFacade::updateTicket()::Drop Address not found with id: {}", ticketRequest.getDropPointId());
                throw new ResourceNotFoundException("Drop Address not found with id: " + ticketRequest.getDropPointId());
            } else {
                if (!ticketRequest.getDropPoint().isEmpty()) {
                    log.info("TicketFacade::updateTicket()::Drop Address is not empty");
                    AddressResponse addressResponse = addressService.getAddress(ticketRequest.getDropPointId());
                    if (!addressResponse.getAddress().equals(ticketRequest.getDropPoint()) && addressResponse.getId().equals(ticketRequest.getDropPointId())) {
                        addressService.updateAddress(ticketRequest.getDropPointId(), AddressDtoMapper.toAddressRequest(ticketRequest.getDropPoint(), ticketRequest.getCustomerId()));
                        log.info("TicketFacade::updateTicket()::Drop Address updated");
                    }
                }
            }
        }else{
            if (!ticketRequest.getDropPoint().isEmpty()) {
                log.warn("TicketFacade::updateTicket()::Drop Address is not empty");
                AddressResponse dropAddress = addressService.createAddress(AddressDtoMapper.toAddressRequest(ticketRequest.getDropPoint(), ticketRequest.getCustomerId()));
                ticketRequest.setDropPointId(dropAddress.getId());
                log.info("TicketFacade::updateTicket()::Drop Address created with id: {}", dropAddress.getId());
            }
        }

        CustomerResponse customerResponse = customerService.getCustomer(ticketRequest.getCustomerId());
        if (!customerResponse.getMobile().equals(ticketRequest.getMobileNumber()) || !customerResponse.getName().equals(ticketRequest.getCustomerName())){
            log.warn("TicketFacade::updateTicket()::Customer name or mobile number want to updated");
            CustomerRequest customerRequest = CustomerRequest.builder()
                    .name(ticketRequest.getCustomerName())
                    .mobile(ticketRequest.getMobileNumber())
                    .build();

            customerResponse = customerService.update(ticketRequest.getCustomerId(), customerRequest);
            log.info("TicketFacade::updateTicket()::Customer updated");
        }

        TicketResponse ticketResponse = ticketService.update(ticketRequest, ticketId);
        log.info("TicketFacade::updateTicket()::Updating ticket completed");
        return ticketResponse;
    }


}
