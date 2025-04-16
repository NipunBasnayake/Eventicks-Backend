package edu.icet.eventicks.repository;

import edu.icet.eventicks.dto.FraudDetectionDto;
import edu.icet.eventicks.entity.FraudDetectionEntity;
import edu.icet.eventicks.entity.TicketEntity;
import edu.icet.eventicks.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FraudDetectionRepository extends JpaRepository<FraudDetectionEntity, Integer> {
    List<FraudDetectionEntity> findByStatus(String status);

    List<FraudDetectionEntity> findByUser(UserEntity user);

    List<FraudDetectionDto> findByTicket(TicketEntity ticket);
}
