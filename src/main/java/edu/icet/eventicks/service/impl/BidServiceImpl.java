package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.BidDto;
import edu.icet.eventicks.entity.BidEntity;
import edu.icet.eventicks.entity.TicketEntity;
import edu.icet.eventicks.entity.UserEntity;
import edu.icet.eventicks.repository.BidRepository;
import edu.icet.eventicks.service.BidService;
import edu.icet.eventicks.service.TicketService;
import edu.icet.eventicks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final UserService userService;
    private final TicketService ticketService;
    private final ModelMapper modelMapper;

    @Override
    public BidDto placeBid(BidDto bidDto) {
        if (bidDto == null) {
            return null;
        }
        if (bidRepository.existsById(bidDto.getBidId())) {
            return null;
        }
        BidEntity bidEntity = modelMapper.map(bidDto, BidEntity.class);
        BidEntity savedEntity = bidRepository.save(bidEntity);
        return modelMapper.map(savedEntity, BidDto.class);
    }

    @Override
    public BidDto getBidById(Integer bidId) {
        if (bidId == null) {
            return null;
        }

        Optional<BidEntity> bidEntity = bidRepository.findById(bidId);
        return bidEntity.map(entity -> modelMapper.map(entity, BidDto.class)).orElse(null);
    }

    @Override
    public List<BidDto> getBidsByUser(Integer userId) {
        if (userId == null) {
            return Collections.emptyList();
        }

        UserEntity user = modelMapper.map(userService.getUserById(userId), UserEntity.class);
        if (user == null) {
            return Collections.emptyList();
        }

        return bidRepository.findByUser(user).stream()
                .map(entity -> modelMapper.map(entity, BidDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<BidDto> getBidsByTicket(Integer ticketId) {
        if (ticketId == null) {
            return Collections.emptyList();
        }

        TicketEntity ticket = modelMapper.map(ticketService.getTicketById(ticketId), TicketEntity.class);
        if (ticket == null) {
            return Collections.emptyList();
        }

        return bidRepository.findByTicket(ticket).stream()
                .map(entity -> modelMapper.map(entity, BidDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public BidDto getHighestBidForTicket(Integer ticketId) {
        if (ticketId == null) {
            return null;
        }

        List<BidDto> bids = getBidsByTicket(ticketId);
        if (bids.isEmpty()) {
            return null;
        }

        return bids.stream()
                .filter(bid -> "PENDING".equals(bid.getStatus()))
                .max(Comparator.comparing(BidDto::getAmount))
                .orElse(null);
    }

    @Override
    public BidDto acceptBid(Integer bidId) {
        if (bidId == null) {
            return null;
        }

        return bidRepository.findById(bidId)
                .filter(bid -> !"ACCEPTED".equals(bid.getStatus()))
                .map(bid -> {
                    bid.setStatus("ACCEPTED");
                    bid.setExpiresAt(LocalDateTime.now());
                    return modelMapper.map(bidRepository.save(bid), BidDto.class);
                })
                .orElse(null);
    }

    @Override
    public BidDto rejectBid(Integer bidId) {
        if (bidId == null) {
            return null;
        }

        Optional<BidEntity> bidOptional = bidRepository.findById(bidId);
        if (bidOptional.isEmpty()) {
            return null;
        }

        BidEntity bidEntity = bidOptional.get();
        bidEntity.setStatus("REJECTED");
        bidEntity.setExpiresAt(LocalDateTime.now());
        BidEntity updatedEntity = bidRepository.save(bidEntity);

        return modelMapper.map(updatedEntity, BidDto.class);
    }

    @Override
    public BidDto cancelBid(Integer bidId) {
        if (bidId == null) {
            return null;
        }

        Optional<BidEntity> bidOptional = bidRepository.findById(bidId);
        if (bidOptional.isEmpty() || !"PENDING".equals(bidOptional.get().getStatus())) {
            return null;
        }

        BidEntity bidEntity = bidOptional.get();
        bidEntity.setStatus("CANCELLED");
        bidEntity.setExpiresAt(LocalDateTime.now());
        BidEntity updatedEntity = bidRepository.save(bidEntity);

        return modelMapper.map(updatedEntity, BidDto.class);
    }
}