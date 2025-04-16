package edu.icet.eventicks.repository;

import edu.icet.eventicks.entity.EventEntity;
import edu.icet.eventicks.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Integer> {
    Collection<Object> findByCreatedBy(UserEntity createdBy);

    List<EventEntity> findByEventDateAfter(LocalDateTime now);

    List<EventEntity> findByCreatedByUserId(Integer userId);
}
