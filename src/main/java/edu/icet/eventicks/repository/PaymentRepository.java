package edu.icet.eventicks.repository;

import edu.icet.eventicks.dto.PaymentDto;
import edu.icet.eventicks.entity.PaymentEntity;
import edu.icet.eventicks.entity.TicketEntity;
import edu.icet.eventicks.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {
    List<PaymentDto> findByBuyer(UserEntity buyer);

    List<PaymentDto> findByTicket(TicketEntity ticket);

    List<PaymentEntity> findByBuyerUserId(Integer userId);

    Collection<PaymentEntity> findByTicketTicketId(Integer ticketId);

    List<PaymentEntity> findByPaidAtAfter(LocalDateTime date);

    List<PaymentEntity> findTop5ByOrderByPaidAtDesc();
}
