package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.ApiResponseDto;
import edu.icet.eventicks.dto.BidDto;
import edu.icet.eventicks.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<BidDto>> placeBid(@RequestBody BidDto bidDto) {
        BidDto createdBid = bidService.placeBid(bidDto);
        return new ResponseEntity<>(ApiResponseDto.success("Bid placed successfully", createdBid), HttpStatus.CREATED);
    }

    @GetMapping("/{bidId}")
    public ResponseEntity<ApiResponseDto<BidDto>> getBidById(@PathVariable Integer bidId) {
        BidDto bid = bidService.getBidById(bidId);
        return ResponseEntity.ok(ApiResponseDto.success(bid));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDto<List<BidDto>>> getBidsByUser(@PathVariable Integer userId) {
        List<BidDto> bids = bidService.getBidsByUser(userId);
        return ResponseEntity.ok(ApiResponseDto.success(bids));
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<ApiResponseDto<List<BidDto>>> getBidsByTicket(@PathVariable Integer ticketId) {
        List<BidDto> bids = bidService.getBidsByTicket(ticketId);
        return ResponseEntity.ok(ApiResponseDto.success(bids));
    }

    @GetMapping("/ticket/{ticketId}/highest")
    public ResponseEntity<ApiResponseDto<BidDto>> getHighestBidForTicket(@PathVariable Integer ticketId) {
        BidDto highestBid = bidService.getHighestBidForTicket(ticketId);
        return ResponseEntity.ok(ApiResponseDto.success(highestBid));
    }

    @PutMapping("/{bidId}/accept")
    public ResponseEntity<ApiResponseDto<BidDto>> acceptBid(@PathVariable Integer bidId) {
        BidDto acceptedBid = bidService.acceptBid(bidId);
        return ResponseEntity.ok(ApiResponseDto.success("Bid accepted successfully", acceptedBid));
    }

    @PutMapping("/{bidId}/reject")
    public ResponseEntity<ApiResponseDto<BidDto>> rejectBid(@PathVariable Integer bidId) {
        BidDto rejectedBid = bidService.rejectBid(bidId);
        return ResponseEntity.ok(ApiResponseDto.success("Bid rejected successfully", rejectedBid));
    }

    @PutMapping("/{bidId}/cancel")
    public ResponseEntity<ApiResponseDto<BidDto>> cancelBid(@PathVariable Integer bidId) {
        BidDto cancelledBid = bidService.cancelBid(bidId);
        return ResponseEntity.ok(ApiResponseDto.success("Bid cancelled successfully", cancelledBid));
    }
}