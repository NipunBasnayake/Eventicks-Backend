package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.EventDto;
import edu.icet.eventicks.dto.UserDto;
import edu.icet.eventicks.entity.EventEntity;
import edu.icet.eventicks.entity.TicketEntity;
import edu.icet.eventicks.entity.UserEntity;
import edu.icet.eventicks.repository.EventRepository;
import edu.icet.eventicks.service.EventService;
import edu.icet.eventicks.service.UserService;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public EventDto createEvent(EventDto eventDto) {
        if (eventDto.getEventId() != null) {
            return null;
        }

        UserDto userDto = userService.getUserById(eventDto.getCreatedById());
        if (userDto == null) {
            return null;
        }

        EventEntity eventEntity = modelMapper.map(eventDto, EventEntity.class);
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        eventEntity.setCreatedBy(userEntity);

        eventEntity.setCreatedAt(LocalDateTime.now());

        EventEntity savedEntity = eventRepository.save(eventEntity);
        EventDto resultDto = modelMapper.map(savedEntity, EventDto.class);

        resultDto.setCreatedById(userDto.getUserId());
        resultDto.setCreatedByUsername(userDto.getName());

        if (savedEntity.getTotalTickets() != null) {
            resultDto.setAvailableTickets(savedEntity.getTotalTickets());
        }

        return resultDto;
    }

    @Override
    public EventDto getEventById(Integer eventId) {
        if (eventId == null) {
            return null;
        }

        return eventRepository.findById(eventId)
                .map(entity -> {
                    EventDto dto = modelMapper.map(entity, EventDto.class);

                    if (entity.getCreatedBy() != null) {
                        dto.setCreatedById(entity.getCreatedBy().getUserId());
                        dto.setCreatedByUsername(entity.getCreatedBy().getName());
                    }

                    if (entity.getTotalTickets() != null) {
                        int ticketsSold = entity.getTickets() != null ? entity.getTickets().size() : 0;
                        dto.setAvailableTickets(entity.getTotalTickets() - ticketsSold);
                    }

                    return dto;
                })
                .orElse(null);
    }

    @Override
    public List<EventDto> getFilteredEvents(String category, String venueLocation, String searchTerm) {
        if ((category == null || category.isEmpty()) &&
                (venueLocation == null || venueLocation.isEmpty()) &&
                (searchTerm == null || searchTerm.isEmpty())) {
            return eventRepository.findAll().stream()
                    .map(entity -> modelMapper.map(entity, EventDto.class))
                    .toList();
        }

        List<EventEntity> allEvents = eventRepository.findAll();
        List<EventDto> filteredEvents = new ArrayList<>();

        for (EventEntity entity : allEvents) {
            boolean categoryMatch = category == null || category.isEmpty() || entity.getCategory().equals(category);
            boolean locationMatch = venueLocation == null || venueLocation.isEmpty() || entity.getVenueLocation().equals(venueLocation);
            boolean searchMatch = searchTerm == null || searchTerm.isEmpty() ||
                    entity.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    entity.getDescription().toLowerCase().contains(searchTerm.toLowerCase());

            if (categoryMatch && locationMatch && searchMatch) {
                filteredEvents.add(modelMapper.map(entity, EventDto.class));
            }
        }
        return filteredEvents;
    }

    @Override
    public List<EventDto> getUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();

        return eventRepository.findAll().stream()
                .filter(event -> event.getEventDate() != null && event.getEventDate().isAfter(now))
                .sorted(Comparator.comparing(EventEntity::getEventDate))
                .map(entity -> {
                    EventDto dto = modelMapper.map(entity, EventDto.class);

                    if (entity.getCreatedBy() != null) {
                        dto.setCreatedById(entity.getCreatedBy().getUserId());
                        dto.setCreatedByUsername(entity.getCreatedBy().getName());
                    }

                    if (entity.getTotalTickets() != null) {
                        int ticketsSold = entity.getTickets() != null ? entity.getTickets().size() : 0;
                        dto.setAvailableTickets(entity.getTotalTickets() - ticketsSold);
                    }

                    return dto;
                })
                .toList();
    }

    @Override
    public EventDto updateEvent(Integer eventId, EventDto eventDto) {
        if (eventId == null || eventDto == null || !Objects.equals(eventId, eventDto.getEventId())) {
            return null;
        }

        return eventRepository.findById(eventId)
                .map(existingEntity -> {
                    UserEntity createdBy = existingEntity.getCreatedBy();
                    LocalDateTime createdAt = existingEntity.getCreatedAt();
                    Set<TicketEntity> tickets = existingEntity.getTickets();

                    modelMapper.map(eventDto, existingEntity);

                    existingEntity.setCreatedBy(createdBy);
                    existingEntity.setCreatedAt(createdAt);
                    existingEntity.setTickets(tickets);

                    EventEntity updatedEntity = eventRepository.save(existingEntity);

                    EventDto resultDto = modelMapper.map(updatedEntity, EventDto.class);

                    if (createdBy != null) {
                        resultDto.setCreatedById(createdBy.getUserId());
                        resultDto.setCreatedByUsername(createdBy.getName());
                    }

                    if (updatedEntity.getTotalTickets() != null) {
                        int ticketsSold = tickets != null ? tickets.size() : 0;
                        resultDto.setAvailableTickets(updatedEntity.getTotalTickets() - ticketsSold);
                    }

                    return resultDto;
                })
                .orElse(null);
    }

    @Override
    public Boolean deleteEvent(Integer eventId) {
        if (eventId == null) {
            return false;
        }

        if (!eventRepository.existsById(eventId)) {
            return false;
        }

        eventRepository.deleteById(eventId);
        return !eventRepository.existsById(eventId);
    }

    @Override
    public List<EventDto> getEventsByCreator(Integer userId) {
        if (userId == null) {
            return Collections.emptyList();
        }

        UserDto userDto = userService.getUserById(userId);
        if (userDto == null) {
            return Collections.emptyList();
        }

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

        return eventRepository.findByCreatedBy(userEntity).stream()
                .map(entity -> {
                    EventDto dto = modelMapper.map(entity, EventDto.class);

                    dto.setCreatedById(userId);
                    dto.setCreatedByUsername(userDto.getName());

                    return dto;
                })
                .toList();
    }

    @Override
    public List<String> getAllCategories() {
        return eventRepository.findAll().stream()
                .map(EventEntity::getCategory)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    @Override
    public List<String> getAllLocations() {
        return eventRepository.findAll().stream()
                .map(EventEntity::getVenueLocation)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }
}