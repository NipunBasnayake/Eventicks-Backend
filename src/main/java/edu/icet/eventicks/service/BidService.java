package edu.icet.eventicks.service;

import edu.icet.eventicks.dto.BidDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BidService {

    ResponseEntity<Boolean> addBid(BidDto bidDto);

    ResponseEntity<List<BidDto>> getAllbids();

    ResponseEntity<Boolean> updateBid(BidDto bidDto);

    ResponseEntity<Boolean> deleteBid(Long id);
}
