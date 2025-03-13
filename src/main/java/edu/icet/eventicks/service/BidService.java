package edu.icet.eventicks.service;

import edu.icet.eventicks.dto.BidDto;

import java.util.List;

public interface BidService {
    BidDto placeBid(BidDto bidDto);

    BidDto getBidById(Integer bidId);

    List<BidDto> getBidsByUser(Integer userId);

    List<BidDto> getBidsByTicket(Integer ticketId);

    BidDto getHighestBidForTicket(Integer ticketId);

    BidDto acceptBid(Integer bidId);

    BidDto rejectBid(Integer bidId);

    BidDto cancelBid(Integer bidId);
}
