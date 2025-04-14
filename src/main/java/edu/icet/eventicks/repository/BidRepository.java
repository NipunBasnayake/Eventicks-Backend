package edu.icet.eventicks.repository;

import edu.icet.eventicks.dto.BidDto;
import edu.icet.eventicks.entity.BidEntity;
import edu.icet.eventicks.entity.TicketEntity;
import edu.icet.eventicks.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface BidRepository extends JpaRepository<BidEntity, Integer> {
    List<BidEntity> findByUser(UserEntity user);

    List<BidEntity> findByTicket(TicketEntity ticket);

    List<BidEntity> findByTicketOrderByBidAmountDesc(TicketEntity ticket);
}
