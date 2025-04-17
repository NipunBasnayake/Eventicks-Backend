package edu.icet.eventicks.repository;

import edu.icet.eventicks.entity.EventEntity;

import edu.icet.eventicks.entity.TicketEntity;
import edu.icet.eventicks.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Integer> {

    Collection<Object> findByEvent(EventEntity event);

    Collection<Object> findBySeller(UserEntity seller);

    List<TicketEntity> findByEventEventId(Integer eventId);

    List<TicketEntity> findBySellerUserId(Integer userId);
}
