package com.express.user.controller;

import com.express.user.dto.TicketAndCustomerResponse;
import com.express.user.dto.TicketRequest;
import com.express.user.dto.TicketResponse;
import com.express.user.facade.TicketFacade;
import com.express.user.response.ApiResponse;
import com.express.user.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketFacade ticketFacade;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TicketResponse>> createTicket(@RequestBody TicketRequest ticketRequest){
        return ResponseEntity.ok(new ApiResponse<>(ApiResponse.SUCCESS, "Ticket created successfully", ticketFacade.createTicket(ticketRequest)));
    }

    @PutMapping("/update/{ticketId}")
    public ResponseEntity<ApiResponse<TicketResponse>> updateTicket(@RequestBody TicketRequest ticketRequest, @PathVariable Long ticketId){
        return ResponseEntity.ok(new ApiResponse<>(ApiResponse.SUCCESS, "Ticket updated successfully", ticketFacade.updateTicket(ticketRequest, ticketId)));
    }

    @GetMapping("/get/{ticketId}")
    public ResponseEntity<ApiResponse<TicketResponse>> getTicket(@PathVariable Long ticketId){
        return ResponseEntity.ok(new ApiResponse<>(ApiResponse.SUCCESS, "Ticket fetched successfully", ticketService.getTicket(ticketId)));
    }

    @GetMapping("/getAllByDate/{date}")
    public ResponseEntity<ApiResponse<List<TicketResponse>>> getTickets(@PathVariable LocalDate date){
        return ResponseEntity.ok(new ApiResponse<>(ApiResponse.SUCCESS, "Tickets fetched successfully", ticketService.getTicketsByDate(date)));
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<TicketResponse>>> getAllTickets(){
        return ResponseEntity.ok(new ApiResponse<>(ApiResponse.SUCCESS, "Tickets fetched successfully", ticketService.getAllTickets()));
    }

    @GetMapping("/GetTicketSerialNo/{ticketId}")
    public ResponseEntity<ApiResponse<String>> getTicketSerialNo(@PathVariable Long ticketId){
        return ResponseEntity.ok(new ApiResponse<>(ApiResponse.SUCCESS, "Ticket Serial Number fetched successfully", ticketService.getTicketNumber(ticketId)));
    }

    @PutMapping("/updateStatus/{ticketId}/{status}")
    public ResponseEntity<ApiResponse<?>> cancelTicket(@PathVariable Long ticketId, @PathVariable int status){
        ticketService.updateStatus(ticketId, status);
        return ResponseEntity.ok(new ApiResponse<>(ApiResponse.SUCCESS, "Ticket status updated successfully", null));
    }

    @DeleteMapping("/delete/{ticketId}")
    public ResponseEntity<ApiResponse<?>> deleteTicket(@PathVariable Long ticketId){
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.ok(new ApiResponse<>(ApiResponse.SUCCESS, "Ticket deleted successfully", null));
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<ApiResponse<?>> deleteAllTickets(){
        ticketService.deleteAll();
        return ResponseEntity.ok(new ApiResponse<>(ApiResponse.SUCCESS, "All Tickets deleted successfully", null));
    }

    @GetMapping("/getTicketDetails")
    public ResponseEntity<ApiResponse<?>> getTicketDetails(){
        return ResponseEntity.ok(new ApiResponse<>(ApiResponse.SUCCESS, "Ticket details fetched successfully", ticketService.getTicketDetails()));
    }

    @GetMapping("/getAllByDateAndRoute/{date}/{route}")
    public ResponseEntity<ApiResponse<List<TicketAndCustomerResponse>>> getTicketsByDateAndRoute(@PathVariable LocalDate date, @PathVariable String route){
        return ResponseEntity.ok(new ApiResponse<>(ApiResponse.SUCCESS, "Tickets fetched successfully", ticketService.getAllTicketsByDateAndRoute(date, route)));
    }


}
