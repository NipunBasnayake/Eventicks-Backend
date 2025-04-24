package edu.icet.eventicks.repository;

import edu.icet.eventicks.entity.QrCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCodeEntity, Integer> {

    QrCodeEntity findByTicketId(Integer ticketId);
}