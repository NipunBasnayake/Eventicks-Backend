package edu.icet.eventicks.repository;

import edu.icet.eventicks.entity.EventEntity;
import edu.icet.eventicks.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface EventRepository extends JpaRepository<EventEntity, Integer> {
    Collection<Object> findByCreatedBy(UserEntity createdBy);
}
