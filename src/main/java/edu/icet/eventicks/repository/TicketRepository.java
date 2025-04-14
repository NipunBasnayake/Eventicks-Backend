package edu.icet.eventicks.repository;

import edu.icet.eventicks.dto.TicketDto;
import edu.icet.eventicks.entity.TicketEntity;
import edu.icet.eventicks.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<TicketEntity, Integer> {

    List<TicketEntity> findByEventId(Integer eventId);


    List<TicketDto> findBySeller(Optional<UserEntity> seller);
}
