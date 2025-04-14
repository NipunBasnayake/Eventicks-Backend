package edu.icet.eventicks.repository;

import edu.icet.eventicks.entity.BidEntity;
import edu.icet.eventicks.entity.TicketEntity;
import edu.icet.eventicks.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface BidRepository extends JpaRepository<BidEntity, Integer> {
    Collection<Object> findByUser(UserEntity user);

    Collection<Object> findByTicket(TicketEntity ticket);
}
