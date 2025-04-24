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
        if (createdBid == null) {
            return new ResponseEntity<>(ApiResponseDto.error("Failed to place bid"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(ApiResponseDto.success("Bid placed successfully", createdBid), HttpStatus.CREATED);
    }

    @GetMapping("/{bidId}")
    public ResponseEntity<ApiResponseDto<BidDto>> getBidById(@PathVariable Integer bidId) {
        BidDto bid = bidService.getBidById(bidId);
        if (bid == null) {
            return new ResponseEntity<>(ApiResponseDto.error("Bid not found"), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ApiResponseDto.success(bid));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDto<List<BidDto>>> getBidsByUser(@PathVariable Integer userId) {
        List<BidDto> bids = bidService.getBidsByUser(userId);
        if (bids == null || bids.isEmpty()) {
            return new ResponseEntity<>(ApiResponseDto.error("No bids found for this user"), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ApiResponseDto.success(bids));
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<ApiResponseDto<List<BidDto>>> getBidsByTicket(@PathVariable Integer ticketId) {
        List<BidDto> bids = bidService.getBidsByTicket(ticketId);
        if (bids == null || bids.isEmpty()) {
            return new ResponseEntity<>(ApiResponseDto.error("No bids found for this ticket"), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ApiResponseDto.success(bids));
    }

    @GetMapping("/ticket/{ticketId}/highest")
    public ResponseEntity<ApiResponseDto<BidDto>> getHighestBidForTicket(@PathVariable Integer ticketId) {
        BidDto highestBid = bidService.getHighestBidForTicket(ticketId);
        if (highestBid == null) {
            return new ResponseEntity<>(ApiResponseDto.error("No bids placed on this ticket yet"), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ApiResponseDto.success(highestBid));
    }

    @PutMapping("/{bidId}/accept")
    public ResponseEntity<ApiResponseDto<BidDto>> acceptBid(@PathVariable Integer bidId) {
        BidDto acceptedBid = bidService.acceptBid(bidId);
        if (acceptedBid == null) {
            return new ResponseEntity<>(ApiResponseDto.error("Failed to accept bid"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(ApiResponseDto.success("Bid accepted successfully", acceptedBid));
    }

    @PutMapping("/{bidId}/reject")
    public ResponseEntity<ApiResponseDto<Boolean>> rejectBid(@PathVariable Integer bidId) {
        boolean isRejected = bidService.rejectBid(bidId); // Assume rejectBid now returns a boolean
        if (!isRejected) {
            return new ResponseEntity<>(ApiResponseDto.error("Failed to reject bid"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(ApiResponseDto.success("Bid rejected successfully", true));
    }


    @PutMapping("/{bidId}/cancel")
    public ResponseEntity<ApiResponseDto<BidDto>> cancelBid(@PathVariable Integer bidId) {
        BidDto cancelledBid = bidService.cancelBid(bidId);
        if (cancelledBid == null) {
            return new ResponseEntity<>(ApiResponseDto.error("Failed to cancel bid"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(ApiResponseDto.success("Bid cancelled successfully", cancelledBid));
    }
}
