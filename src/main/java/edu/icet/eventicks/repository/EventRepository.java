package edu.icet.eventicks.repository;

import edu.icet.eventicks.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Integer> {

}
