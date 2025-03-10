package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.BidDto;
import edu.icet.eventicks.service.BidService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class BidServiceImpl implements BidService {
    @Override
    public ResponseEntity<Boolean> addBid(BidDto bidDto) {
        return null;
    }

    @Override
    public ResponseEntity<List<BidDto>> getAllbids() {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> updateBid(BidDto bidDto) {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> deleteBid(Long id) {
        return null;
    }
}
