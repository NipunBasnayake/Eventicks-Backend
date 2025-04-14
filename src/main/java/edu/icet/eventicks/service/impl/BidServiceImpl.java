package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.BidDto;
import edu.icet.eventicks.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    @Override
    public BidDto placeBid(BidDto bidDto) {
        return null;
    }

    @Override
    public BidDto getBidById(Integer bidId) {
        return null;
    }

    @Override
    public List<BidDto> getBidsByUser(Integer userId) {
        return List.of();
    }

    @Override
    public List<BidDto> getBidsByTicket(Integer ticketId) {
        return List.of();
    }

    @Override
    public BidDto getHighestBidForTicket(Integer ticketId) {
        return null;
    }

    @Override
    public BidDto acceptBid(Integer bidId) {
        return null;
    }

    @Override
    public BidDto rejectBid(Integer bidId) {
        return null;
    }

    @Override
    public BidDto cancelBid(Integer bidId) {
        return null;
    }
}