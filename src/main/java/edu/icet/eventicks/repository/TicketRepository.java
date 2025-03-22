package edu.icet.eventicks.repository;

import edu.icet.eventicks.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<TicketEntity, Integer> {

}
