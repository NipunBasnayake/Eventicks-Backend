package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.BidDto;
import edu.icet.eventicks.entity.BidEntity;
import edu.icet.eventicks.repository.BidRepository;
import edu.icet.eventicks.service.BidService;
import edu.icet.eventicks.util.enums.BidStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
    private final BidRepository bidRepository;
    private final ModelMapper modelMapper;

    @Override
    public BidDto placeBid(BidDto bidDto) {
        if (bidDto == null) {
            return null;
        }
        if (bidDto.getBidId() == null || !bidRepository.existsById(bidDto.getBidId())) {
            BidEntity bidEntity = modelMapper.map(bidDto, BidEntity.class);
            bidEntity.setStatus(BidStatus.ACTIVE.name());
            bidEntity = bidRepository.save(bidEntity);
            return modelMapper.map(bidEntity, BidDto.class);
        }
        return null;
    }

    @Override
    public BidDto getBidById(Integer bidId) {
        Optional<BidEntity> bidEntity = bidRepository.findById(bidId);
        return bidEntity.map(entity -> modelMapper.map(entity, BidDto.class))
                .orElse(null);
    }

    @Override
    public List<BidDto> getBidsByUser(Integer userId) {
        List<BidEntity> bidEntities = bidRepository.findAll();
        return bidEntities.stream()
                .filter(entity -> entity.getUserId().equals(userId))
                .map(bidEntity -> modelMapper.map(bidEntity, BidDto.class))
                .toList();
    }

    @Override
    public List<BidDto> getBidsByTicket(Integer ticketId) {
        List<BidEntity> bidEntities = bidRepository.findAll();
        return bidEntities.stream()
                .filter(entity -> entity.getTicketId().equals(ticketId))
                .map(bidEntity -> modelMapper.map(bidEntity, BidDto.class))
                .toList();
    }

    @Override
    public BidDto getHighestBidForTicket(Integer ticketId) {
        List<BidEntity> bidEntities = bidRepository.findAll();

        Optional<BidEntity> highestBid = bidEntities.stream()
                .filter(bid -> bid.getTicketId().equals(ticketId))
                .max(Comparator.comparing(BidEntity::getAmount));

        return highestBid.map(bidEntity -> modelMapper.map(bidEntity, BidDto.class))
                .orElse(null);
    }


    @Override
    public BidDto acceptBid(Integer bidId) {
        if (bidId == null || !bidRepository.existsById(bidId)) {
            return null;
        }
        BidEntity bidEntity = bidRepository.findById(bidId).orElse(null);
        assert bidEntity != null;
        bidEntity.setStatus(BidStatus.ACCEPTED.name());
        BidEntity saved = bidRepository.save(bidEntity);
        return modelMapper.map(saved, BidDto.class);
    }

    @Override
    public BidDto cancelBid(Integer bidId) {
        if (bidId == null || !bidRepository.existsById(bidId)) {
            return null;
        }
        BidEntity bidEntity = bidRepository.findById(bidId).orElse(null);
        assert bidEntity != null;
        bidEntity.setStatus(BidStatus.OUTBID.name());
        BidEntity saved = bidRepository.save(bidEntity);
        return modelMapper.map(saved, BidDto.class);
    }

    @Override
    public Boolean rejectBid(Integer bidId) {
        if (bidId == null || !bidRepository.existsById(bidId)) {
            return false;
        }
        bidRepository.deleteById(bidId);
        return !bidRepository.existsById(bidId);
    }
}
