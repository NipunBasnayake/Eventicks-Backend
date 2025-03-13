package edu.icet.eventicks.repository;

import edu.icet.eventicks.entity.BidEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<BidEntity, Integer> {
}
