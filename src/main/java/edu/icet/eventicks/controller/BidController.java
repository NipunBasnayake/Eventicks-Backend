package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.BidDto;
import edu.icet.eventicks.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bid")
@RequiredArgsConstructor
@CrossOrigin
public class BidController {

    final BidService bidService;

    @PostMapping("/add")
    public ResponseEntity<Boolean> addBid (@RequestBody BidDto bidDto) {
        return bidService.addBid(bidDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BidDto>> getAllBids() {
        return bidService.getAllbids();
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateBid (@RequestBody BidDto bidDto) {
        return bidService.updateBid(bidDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteBid (@PathVariable Long id) {
        return bidService.deleteBid(id);
    }
}
