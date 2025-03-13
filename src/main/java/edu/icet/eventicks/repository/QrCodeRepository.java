package edu.icet.eventicks.repository;

import edu.icet.eventicks.entity.QrCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QrCodeRepository extends JpaRepository<QrCodeEntity, Integer> {
}
