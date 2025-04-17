package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.entity.*;
import edu.icet.eventicks.repository.*;
import edu.icet.eventicks.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final BidRepository bidRepository;
    private final FraudDetectionRepository fraudDetectionRepository;

    @Override
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();

        long totalEvents = eventRepository.count();
        long totalTickets = ticketRepository.count();
        long totalUsers = userRepository.count();
        long totalSales = paymentRepository.count();

        BigDecimal totalRevenue = paymentRepository.findAll().stream()
                .map(PaymentEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<EventEntity> upcomingEvents = eventRepository.findByEventDateAfter(LocalDateTime.now());
        List<Map<String, Object>> upcomingEventsData = upcomingEvents.stream()
                .limit(5)
                .map(this::convertEventToMap)
                .toList();

        List<PaymentEntity> recentSales = paymentRepository.findTop5ByOrderByPaidAtDesc();
        List<Map<String, Object>> recentSalesData = recentSales.stream()
                .map(this::convertPaymentToMap)
                .toList();

        Map<Integer, Long> ticketsSoldByEvent = paymentRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        payment -> payment.getTicket().getEvent().getEventId(),
                        Collectors.summingLong(PaymentEntity::getQuantity)
                ));

        List<Map.Entry<Integer, Long>> topSellingEvents = ticketsSoldByEvent.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(5)
                .toList();

        List<Map<String, Object>> topEventsData = topSellingEvents.stream()
                .map(entry -> {
                    EventEntity event = eventRepository.findById(entry.getKey()).orElse(null);
                    if (event == null) return null;
                    Map<String, Object> eventMap = convertEventToMap(event);
                    eventMap.put("tickets Sold", entry.getValue());
                    return eventMap;
                })
                .filter(Objects::nonNull)
                .toList();

        long totalFraudCases = fraudDetectionRepository.count();

        summary.put("totalEvents", totalEvents);
        summary.put("total Tickets", totalTickets);
        summary.put("totalUsers", totalUsers);
        summary.put("totalSales", totalSales);
        summary.put("total Revenue", totalRevenue);
        summary.put("upcomingEvents", upcomingEventsData);
        summary.put("recentSales", recentSalesData);
        summary.put("topSellingEvents", topEventsData);
        summary.put("totalFraudCases", totalFraudCases);

        return summary;
    }

    @Override
    public Map<String, Object> getEventStatistics(Integer eventId) {
        Map<String, Object> statistics = new HashMap<>();

        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));

        List<TicketEntity> eventTickets = ticketRepository.findByEventEventId(eventId);

        long totalTickets = eventTickets.size();
        long soldTickets = eventTickets.stream()
                .filter(ticket -> "SOLD".equals(ticket.getStatus()))
                .count();
        long availableTickets = eventTickets.stream()
                .filter(ticket -> "AVAILABLE".equals(ticket.getStatus()))
                .count();

        List<PaymentEntity> eventPayments = new ArrayList<>();
        for (TicketEntity ticket : eventTickets) {
            eventPayments.addAll(paymentRepository.findByTicketTicketId(ticket.getTicketId()));
        }

        BigDecimal totalRevenue = eventPayments.stream()
                .map(PaymentEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OptionalDouble avgTicketPrice = eventTickets.stream()
                .map(TicketEntity::getPrice)
                .mapToDouble(BigDecimal::doubleValue)
                .average();

        List<BidEntity> eventBids = new ArrayList<>();
        for (TicketEntity ticket : eventTickets) {
            eventBids.addAll(bidRepository.findByTicketTicketId(ticket.getTicketId()));
        }

        long totalBids = eventBids.size();
        OptionalDouble avgBidAmount = eventBids.stream()
                .map(BidEntity::getAmount)
                .mapToDouble(BigDecimal::doubleValue)
                .average();

        Map<LocalDate, BigDecimal> salesByDay = eventPayments.stream()
                .collect(Collectors.groupingBy(
                        payment -> payment.getPaidAt().toLocalDate(),
                        Collectors.mapping(
                                PaymentEntity::getTotalAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        List<Map<String, Object>> salesTimelineData = salesByDay.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    Map<String, Object> dataPoint = new HashMap<>();
                    dataPoint.put("date", entry.getKey().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    dataPoint.put("revenue ", entry.getValue());
                    return dataPoint;
                })
                .toList();

        statistics.put("event Id", event.getEventId());
        statistics.put("eventName", event.getName());
        statistics.put("eventDate", event.getEventDate());
        statistics.put("totalTickets", totalTickets);
        statistics.put("soldTickets", soldTickets);
        statistics.put("availableTickets", availableTickets);
        statistics.put("totalRevenue", totalRevenue);
        statistics.put("averageTicketPrice", avgTicketPrice.orElse(0));
        statistics.put("totalBids", totalBids);
        statistics.put("averageBidAmount", avgBidAmount.orElse(0));
        statistics.put("salesTimeline", salesTimelineData);

        return statistics;
    }

    @Override
    public Map<String, Object> getTicketSalesStatistics(String period) {
        Map<String, Object> statistics = new HashMap<>();

        LocalDateTime startDate;
        LocalDateTime now = LocalDateTime.now();

        switch (period.toLowerCase()) {
            case "daily":
                startDate = now.minusDays(30);
                break;
            case "weekly":
                startDate = now.minusWeeks(12);
                break;
            case "monthly":
                startDate = now.minusMonths(12);
                break;
            case "yearly":
                startDate = now.minusYears(5);
                break;
            default:
                startDate = now.minusDays(30);
        }

        List<PaymentEntity> paymentsInPeriod = paymentRepository.findByPaidAtAfter(startDate);

        Map<String, Object> salesByPeriod;

        switch (period.toLowerCase()) {
            case "daily":
                salesByPeriod = getDailySalesData(paymentsInPeriod);
                break;
            case "weekly":
                salesByPeriod = getWeeklySalesData(paymentsInPeriod);
                break;
            case "monthly":
                salesByPeriod = getMonthlySalesData(paymentsInPeriod);
                break;
            case "yearly":
                salesByPeriod = getYearlySalesData(paymentsInPeriod);
                break;
            default:
                salesByPeriod = getDailySalesData(paymentsInPeriod);
        }

        BigDecimal totalRevenue = paymentsInPeriod.stream()
                .map(PaymentEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalTicketsSold = paymentsInPeriod.stream()
                .mapToLong(PaymentEntity::getQuantity)
                .sum();

        Map<String, BigDecimal> revenueByCategory = new HashMap<>();
        Map<String, Long> ticketsByCategory = new HashMap<>();

        for (PaymentEntity payment : paymentsInPeriod) {
            String category = payment.getTicket().getEvent().getCategory();
            if (category == null) {
                category = "Uncategorized";
            }

            revenueByCategory.put(
                    category,
                    revenueByCategory.getOrDefault(category, BigDecimal.ZERO)
                            .add(payment.getTotalAmount())
            );

            ticketsByCategory.put(
                    category,
                    ticketsByCategory.getOrDefault(category, 0L) + payment.getQuantity()
            );
        }

        statistics.put("period", period);
        statistics.put("totalRevenue", totalRevenue);
        statistics.put("totalTicketsSold", totalTicketsSold);
        statistics.put("salesData", salesByPeriod);
        statistics.put("revenueByCategory", revenueByCategory);
        statistics.put("ticketsByCategory", ticketsByCategory);

        return statistics;
    }

    @Override
    public Map<String, Object> getUserActivityStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        List<UserEntity> users = userRepository.findAll();

        Map<LocalDate, Long> registrationsByDay = users.stream()
                .collect(Collectors.groupingBy(
                        user -> user.getRegisteredAt().toLocalDate(),
                        Collectors.counting()
                ));

        List<Map<String, Object>> registrationsTimeline = registrationsByDay.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    Map<String, Object> dataPoint = new HashMap<>();
                    dataPoint.put("date", entry.getKey().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    dataPoint.put("count", entry.getValue());
                    return dataPoint;
                })
                .toList();

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        long activeUsers = users.stream()
                .filter(user -> user.getLastLoginAt() != null && user.getLastLoginAt().isAfter(oneMonthAgo))
                .count();

        Map<String, Long> usersByRole = users.stream()
                .collect(Collectors.groupingBy(
                        UserEntity::getRole,
                        Collectors.counting()
                ));

        long eventCreators = users.stream()
                .filter(user -> !user.getCreatedEvents().isEmpty())
                .count();

        long ticketSellers = users.stream()
                .filter(user -> !user.getSellingTickets().isEmpty())
                .count();

        long ticketBuyers = users.stream()
                .filter(user -> !user.getPayments().isEmpty())
                .count();

        long bidders = users.stream()
                .filter(user -> !user.getBids().isEmpty())
                .count();

        statistics.put("totalUsers", users.size());
        statistics.put("activeUsers", activeUsers);
        statistics.put("usersByRole", usersByRole);
        statistics.put("eventCreators", eventCreators);
        statistics.put("ticketSellers", ticketSellers);
        statistics.put("ticketBuyers", ticketBuyers);
        statistics.put("bidders", bidders);
        statistics.put("registrationsTimeline", registrationsTimeline);

        return statistics;
    }

    @Override
    public Map<String, Object> getFraudStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        List<FraudDetectionEntity> fraudDetections = fraudDetectionRepository.findAll();

        Map<String, Long> fraudByStatus = fraudDetections.stream()
                .collect(Collectors.groupingBy(
                        FraudDetectionEntity::getStatus,
                        Collectors.counting()
                ));

        Map<String, Long> fraudByReason = fraudDetections.stream()
                .collect(Collectors.groupingBy(
                        detection -> detection.getReason() != null ? detection.getReason() : "Unknown",
                        Collectors.counting()
                ));

        Map<LocalDate, Long> fraudByDay = fraudDetections.stream()
                .collect(Collectors.groupingBy(
                        detection -> detection.getDetectedAt().toLocalDate(),
                        Collectors.counting()
                ));

        List<Map<String, Object>> fraudTimeline = fraudByDay.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    Map<String, Object> dataPoint = new HashMap<>();
                    dataPoint.put("date", entry.getKey().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    dataPoint.put("count", entry.getValue());
                    return dataPoint;
                })
                .toList();

        Map<Integer, Long> fraudByUser = fraudDetections.stream()
                .filter(detection -> detection.getUser() != null)
                .collect(Collectors.groupingBy(
                        detection -> detection.getUser().getUserId(),
                        Collectors.counting()
                ));

        List<Map.Entry<Integer, Long>> topFraudUsers = fraudByUser.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(10)
                .toList();

        List<Map<String, Object>> topFraudUsersData = topFraudUsers.stream()
                .map(entry -> {
                    UserEntity user = userRepository.findById(entry.getKey()).orElse(null);
                    if (user == null) return null;

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("userId", user.getUserId());
                    userData.put("name", user.getName());
                    userData.put("email", user.getEmail());
                    userData.put("fraudCount", entry.getValue());

                    return userData;
                })
                .filter(Objects::nonNull)
                .toList();

        statistics.put("totalFraudDetections", fraudDetections.size());
        statistics.put("fraudByStatus", fraudByStatus);
        statistics.put("fraudByReason", fraudByReason);
        statistics.put("fraudTimeline", fraudTimeline);
        statistics.put("topFraudUsers", topFraudUsersData);

        return statistics;
    }

    @Override
    public Map<String, Object> getUserSpecificStatistics(Integer userId) {
        Map<String, Object> statistics = new HashMap<>();

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        List<EventEntity> userEvents = eventRepository.findByCreatedByUserId(userId);
        List<TicketEntity> sellingTickets = ticketRepository.findBySellerUserId(userId);
        List<PaymentEntity> userPayments = paymentRepository.findByBuyerUserId(userId);
        List<BidEntity> userBids = bidRepository.findByUserUserId(userId);
        List<FraudDetectionEntity> userFraudDetections = fraudDetectionRepository.findByUserUserId(userId);

        BigDecimal sellingRevenue = BigDecimal.ZERO;
        for (TicketEntity ticket : sellingTickets) {
            if ("SOLD".equals(ticket.getStatus())) {
                Collection<PaymentEntity> ticketPayments = paymentRepository.findByTicketTicketId(ticket.getTicketId());
                for (PaymentEntity payment : ticketPayments) {
                    sellingRevenue = sellingRevenue.add(payment.getTotalAmount());
                }
            }
        }

        BigDecimal purchaseSpending = userPayments.stream()
                .map(PaymentEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long successfulBids = userBids.stream()
                .filter(bid -> "ACCEPTED".equals(bid.getStatus()))
                .count();

        OptionalDouble avgBidAmount = userBids.stream()
                .map(BidEntity::getAmount)
                .mapToDouble(BigDecimal::doubleValue)
                .average();

        List<Map<String, Object>> activityTimeline = new ArrayList<>();

        userEvents.forEach(event -> {
            Map<String, Object> activity = new HashMap<>();
            activity.put("date", event.getCreatedAt());
            activity.put("type", "EVENT_CREATED");
            activity.put("detail", "Created event: " + event.getName());
            activityTimeline.add(activity);
        });

        sellingTickets.forEach(ticket -> {
            Map<String, Object> activity = new HashMap<>();
            activity.put("date", ticket.getCreatedAt());
            activity.put("type", "TICKET_LISTED");
            activity.put("details ", "Listed ticket for event: " + ticket.getEvent().getName());
            activityTimeline.add(activity);
        });

        userPayments.forEach(payment -> {
            Map<String, Object> activity = new HashMap<>();
            activity.put("date", payment.getPaidAt());
            activity.put("type", "TICKET_PURCHASED");
            activity.put("details", "Purchased ticket for event: " + payment.getTicket().getEvent().getName());
            activityTimeline.add(activity);
        });

        userBids.forEach(bid -> {
            Map<String, Object> activity = new HashMap<>();
            activity.put("date", bid.getPlacedAt());
            activity.put("type", "BID_PLACED");
            activity.put("details", "Placed bid on ticket for event: " + bid.getTicket().getEvent().getName());
            activityTimeline.add(activity);
        });

        activityTimeline.sort((a, b) -> {
            LocalDateTime dateA = (LocalDateTime) a.get("date");
            LocalDateTime dateB = (LocalDateTime) b.get("date");
            return dateB.compareTo(dateA);
        });

        statistics.put("userId", user.getUserId());
        statistics.put("name", user.getName());
        statistics.put("email", user.getEmail());
        statistics.put("role", user.getRole());
        statistics.put("registeredAt", user.getRegisteredAt());
        statistics.put("lastLoginAt", user.getLastLoginAt());
        statistics.put("eventsCreated", userEvents.size());
        statistics.put("ticketsListed", sellingTickets.size());
        statistics.put("ticketsPurchased", userPayments.stream().mapToLong(PaymentEntity::getQuantity).sum());
        statistics.put("bidsPlaced", userBids.size());
        statistics.put("successfulBids", successfulBids);
        statistics.put("fraudDetections", userFraudDetections.size());
        statistics.put("sellingRevenue", sellingRevenue);
        statistics.put("purchaseSpending", purchaseSpending);
        statistics.put("averageBidAmount", avgBidAmount.orElse(0));
        statistics.put("activityTimeline", activityTimeline);

        return statistics;
    }

    private Map<String, Object> getDailySalesData(List<PaymentEntity> payments) {
        Map<String, Object> salesData = new HashMap<>();

        Map<LocalDate, List<PaymentEntity>> paymentsByDay = payments.stream()
                .collect(Collectors.groupingBy(payment -> payment.getPaidAt().toLocalDate()));

        List<Map<String, Object>> timeline = paymentsByDay.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    Map<String, Object> dataPoint = new HashMap<>();
                    dataPoint.put("date", entry.getKey().format(DateTimeFormatter.ISO_LOCAL_DATE));

                    BigDecimal revenue = entry.getValue().stream()
                            .map(PaymentEntity::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    long ticketsSold = entry.getValue().stream()
                            .mapToLong(PaymentEntity::getQuantity)
                            .sum();

                    dataPoint.put("revenue ", revenue);
                    dataPoint.put("tickets Sold Out", ticketsSold);

                    return dataPoint;
                })
                .toList();

        salesData.put("time  line", timeline);

        return salesData;
    }

    private Map<String, Object> getWeeklySalesData(List<PaymentEntity> payments) {
        Map<String, Object> salesData = new HashMap<>();

        Map<String, List<PaymentEntity>> paymentsByWeek = new HashMap<>();

        for (PaymentEntity payment : payments) {
            LocalDate date = payment.getPaidAt().toLocalDate();
            int year = date.getYear();
            int week = date.get(java.time.temporal.WeekFields.of(Locale.getDefault()).weekOfYear());

            String weekKey = year + "-W" + week;

            if (!paymentsByWeek.containsKey(weekKey)) {
                paymentsByWeek.put(weekKey, new ArrayList<>());
            }

            paymentsByWeek.get(weekKey).add(payment);
        }

        List<Map<String, Object>> timeline = paymentsByWeek.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    Map<String, Object> dataPoint = new HashMap<>();
                    dataPoint.put("week", entry.getKey());

                    BigDecimal revenue = entry.getValue().stream()
                            .map(PaymentEntity::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    long ticketsSold = entry.getValue().stream()
                            .mapToLong(PaymentEntity::getQuantity)
                            .sum();

                    dataPoint.put(" revenue", revenue);
                    dataPoint.put("ticket Sold", ticketsSold);

                    return dataPoint;
                })
                .toList();

        salesData.put("time line", timeline);

        return salesData;
    }

    private Map<String, Object> getMonthlySalesData(List<PaymentEntity> payments) {
        Map<String, Object> salesData = new HashMap<>();

        Map<String, List<PaymentEntity>> paymentsByMonth = new HashMap<>();

        for (PaymentEntity payment : payments) {
            LocalDate date = payment.getPaidAt().toLocalDate();
            int year = date.getYear();
            int month = date.getMonthValue();

            String monthKey = year + "-" + (month < 10 ? "0" + month : month);

            if (!paymentsByMonth.containsKey(monthKey)) {
                paymentsByMonth.put(monthKey, new ArrayList<>());
            }

            paymentsByMonth.get(monthKey).add(payment);
        }

        List<Map<String, Object>> timeline = paymentsByMonth.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    Map<String, Object> dataPoint = new HashMap<>();
                    dataPoint.put("month", entry.getKey());

                    BigDecimal revenue = entry.getValue().stream()
                            .map(PaymentEntity::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    long ticketsSold = entry.getValue().stream()
                            .mapToLong(PaymentEntity::getQuantity)
                            .sum();

                    dataPoint.put("revenue", revenue);
                    dataPoint.put("ticketsSold", ticketsSold);

                    return dataPoint;
                })
                .toList();

        salesData.put("timeline", timeline);

        return salesData;
    }

    private Map<String, Object> getYearlySalesData(List<PaymentEntity> payments) {
        Map<String, Object> salesData = new HashMap<>();

        Map<Integer, List<PaymentEntity>> paymentsByYear = payments.stream()
                .collect(Collectors.groupingBy(payment -> payment.getPaidAt().getYear()));

        List<Map<String, Object>> timeline = paymentsByYear.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    Map<String, Object> dataPoint = new HashMap<>();
                    dataPoint.put("year", entry.getKey());

                    BigDecimal revenue = entry.getValue().stream()
                            .map(PaymentEntity::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    long ticketsSold = entry.getValue().stream()
                            .mapToLong(PaymentEntity::getQuantity)
                            .sum();

                    dataPoint.put("revenue", revenue);
                    dataPoint.put("ticketsSold", ticketsSold);

                    return dataPoint;
                })
                .toList();

        salesData.put("timeline", timeline);

        return salesData;
    }

    private Map<String, Object> convertEventToMap(EventEntity event) {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("eventId", event.getEventId());
        eventMap.put("name", event.getName());
        eventMap.put("description", event.getDescription());
        eventMap.put("eventDate", event.getEventDate());
        eventMap.put("venueName", event.getVenueName());
        eventMap.put("venueLocation", event.getVenueLocation());
        eventMap.put("category", event.getCategory());
        eventMap.put("totalTickets", event.getTotalTickets());

        if (event.getCreatedBy() != null) {
            eventMap.put("createdBy", event.getCreatedBy().getName());
        }

        return eventMap;
    }

    private Map<String, Object> convertPaymentToMap(PaymentEntity payment) {
        Map<String, Object> paymentMap = new HashMap<>();
        paymentMap.put("paymentId", payment.getPaymentId());
        paymentMap.put("paidAt", payment.getPaidAt());
        paymentMap.put("totalAmount", payment.getTotalAmount());
        paymentMap.put("quantity", payment.getQuantity());
        paymentMap.put("method", payment.getMethod());

        if (payment.getBuyer() != null) {
            paymentMap.put("buyerId", payment.getBuyer().getUserId());
            paymentMap.put("buyerName", payment.getBuyer().getName());
        }

        if (payment.getTicket() != null) {
            paymentMap.put("ticketId", payment.getTicket().getTicketId());

            if (payment.getTicket().getEvent() != null) {
                paymentMap.put("eventId", payment.getTicket().getEvent().getEventId());
                paymentMap.put("eventName", payment.getTicket().getEvent().getName());
            }
        }

        return paymentMap;
    }
}