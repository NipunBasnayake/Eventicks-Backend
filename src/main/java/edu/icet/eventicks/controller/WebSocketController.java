package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.BidDto;
import edu.icet.eventicks.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final BidService bidService;

    @MessageMapping("/bid/{ticketId}")
    @SendTo("/topic/bids/{ticketId}")
    public BidDto processBid(@DestinationVariable Integer ticketId, BidDto bidDto) {
        // Set the ticket ID from the path
        bidDto.setTicketId(ticketId);
        return bidService.placeBid(bidDto);
    }

    @MessageMapping("/bid-update/{bidId}")
    @SendTo("/topic/bid-updates/{bidId}")
    public BidDto updateBidStatus(@DestinationVariable Integer bidId, String status) {
        if ("ACCEPTED".equals(status)) {
            return bidService.acceptBid(bidId);
        } else if ("REJECTED".equals(status)) {
            return bidService.rejectBid(bidId);
        } else {
            return bidService.cancelBid(bidId);
        }
    }
}