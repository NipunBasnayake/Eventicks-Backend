package edu.icet.eventicks.repository;

import edu.icet.eventicks.entity.FraudDetectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FraudDetectionRepository extends JpaRepository<FraudDetectionEntity, Integer> {
}
