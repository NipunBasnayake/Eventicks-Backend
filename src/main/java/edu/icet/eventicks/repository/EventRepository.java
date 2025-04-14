package edu.icet.eventicks.repository;

import edu.icet.eventicks.entity.EventEntity;
import edu.icet.eventicks.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<EventEntity, Integer> {
    List<EventEntity> findByCreatedBy(Optional<UserEntity> createdBy);
}
