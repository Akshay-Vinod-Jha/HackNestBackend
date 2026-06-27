package com.hacknest.backend.services.hackathon;

import com.hacknest.backend.dto.common.PagedResponse;
import com.hacknest.backend.dto.hackathon.CreateHackathonRequest;
import com.hacknest.backend.dto.hackathon.HackathonResponse;
import com.hacknest.backend.dto.hackathon.HackathonSummaryResponse;
import com.hacknest.backend.enums.HackathonStatus;
import com.hacknest.backend.models.hackathon.Hackathon;
import com.hacknest.backend.repositories.HackathonRepository;
import com.hacknest.backend.repositories.UserRepository;
import com.hacknest.backend.models.User;
import com.hacknest.backend.services.notification.NotificationService;
import com.hacknest.backend.dto.notification.CreateNotificationRequest;
import com.hacknest.backend.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HackathonServiceImpl implements HackathonService {

    private final HackathonRepository hackathonRepository;
    private final MongoTemplate mongoTemplate;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Override
    public HackathonResponse createHackathon(CreateHackathonRequest request, String userId) {
        
        if (request.getTeamSizeMin() > request.getTeamSizeMax()) {
            throw new IllegalArgumentException("Minimum team size cannot be greater than maximum team size");
        }
        if (request.getRegistrationDeadline().isAfter(request.getHackathonStartDate())) {
            throw new IllegalArgumentException("Registration deadline must be before hackathon start date");
        }
        if (request.getHackathonStartDate().isAfter(request.getHackathonEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        Hackathon hackathon = Hackathon.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .organizer(request.getOrganizer())
                .websiteUrl(request.getWebsiteUrl())
                .registrationUrl(request.getRegistrationUrl())
                .mode(request.getMode())
                .status(HackathonStatus.UPCOMING)
                .teamSizeMin(request.getTeamSizeMin())
                .teamSizeMax(request.getTeamSizeMax())
                .registrationDeadline(request.getRegistrationDeadline())
                .hackathonStartDate(request.getHackathonStartDate())
                .hackathonEndDate(request.getHackathonEndDate())
                .prizePool(request.getPrizePool())
                .country(request.getCountry())
                .city(request.getCity())
                .domains(request.getDomains())
                .techStacks(request.getTechStacks())
                .tags(request.getTags())
                .createdBy(userId)
                .build();

        hackathon = hackathonRepository.save(hackathon);
        
        // Notify all users about the new hackathon
        List<User> allUsers = userRepository.findAll();
        for (User u : allUsers) {
            notificationService.createNotification(CreateNotificationRequest.builder()
                .recipientId(u.getId())
                .type(NotificationType.HACKATHON_UPDATE)
                .title("New Hackathon: " + hackathon.getTitle())
                .message("A new hackathon '" + hackathon.getTitle() + "' has been published! Check it out.")
                .relatedEntityId(hackathon.getId())
                .build());
        }

        return buildHackathonResponse(hackathon);
    }

    @Override
    public HackathonResponse getHackathonById(String hackathonId) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));
        return buildHackathonResponse(hackathon);
    }

    @Override
    public PagedResponse<HackathonSummaryResponse> getAllHackathons(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Hackathon> hackathonPage = hackathonRepository.findAll(pageable);
        
        Page<HackathonSummaryResponse> summaryPage = hackathonPage.map(hackathon -> 
            HackathonSummaryResponse.builder()
                .id(hackathon.getId())
                .title(hackathon.getTitle())
                .organizer(hackathon.getOrganizer())
                .mode(hackathon.getMode())
                .status(hackathon.getStatus())
                .registrationDeadline(hackathon.getRegistrationDeadline())
                .hackathonStartDate(hackathon.getHackathonStartDate())
                .country(hackathon.getCountry())
                .city(hackathon.getCity())
                .tags(hackathon.getTags())
                .build()
        );
        
        return PagedResponse.of(summaryPage);
    }

    @Override
    public PagedResponse<HackathonSummaryResponse> searchHackathons(String country, String mode, String status, String domain, String techStack, String tag, LocalDateTime registrationDeadlineBefore, int page, int size, String sortBy, String sortDirection) {
        Query query = new Query();
        
        if (country != null && !country.isBlank()) {
            query.addCriteria(Criteria.where("country").is(country));
        }
        if (mode != null && !mode.isBlank()) {
            query.addCriteria(Criteria.where("mode").is(mode));
        }
        if (status != null && !status.isBlank()) {
            query.addCriteria(Criteria.where("status").is(status));
        }
        if (domain != null && !domain.isBlank()) {
            query.addCriteria(Criteria.where("domains").is(domain));
        }
        if (techStack != null && !techStack.isBlank()) {
            query.addCriteria(Criteria.where("techStacks").is(techStack));
        }
        if (tag != null && !tag.isBlank()) {
            query.addCriteria(Criteria.where("tags").is(tag));
        }
        if (registrationDeadlineBefore != null) {
            query.addCriteria(Criteria.where("registrationDeadline").lte(registrationDeadlineBefore));
        }

        long total = mongoTemplate.count(query, Hackathon.class);
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        query.with(pageable);
        
        List<Hackathon> hackathons = mongoTemplate.find(query, Hackathon.class);
        Page<Hackathon> hackathonPage = new PageImpl<>(hackathons, pageable, total);
        
        Page<HackathonSummaryResponse> summaryPage = hackathonPage.map(hackathon -> 
            HackathonSummaryResponse.builder()
                .id(hackathon.getId())
                .title(hackathon.getTitle())
                .organizer(hackathon.getOrganizer())
                .mode(hackathon.getMode())
                .status(hackathon.getStatus())
                .registrationDeadline(hackathon.getRegistrationDeadline())
                .hackathonStartDate(hackathon.getHackathonStartDate())
                .country(hackathon.getCountry())
                .city(hackathon.getCity())
                .tags(hackathon.getTags())
                .build()
        );
        
        return PagedResponse.of(summaryPage);
    }

    private HackathonResponse buildHackathonResponse(Hackathon hackathon) {
        return HackathonResponse.builder()
                .id(hackathon.getId())
                .title(hackathon.getTitle())
                .description(hackathon.getDescription())
                .organizer(hackathon.getOrganizer())
                .websiteUrl(hackathon.getWebsiteUrl())
                .registrationUrl(hackathon.getRegistrationUrl())
                .mode(hackathon.getMode())
                .status(hackathon.getStatus())
                .teamSizeMin(hackathon.getTeamSizeMin())
                .teamSizeMax(hackathon.getTeamSizeMax())
                .registrationDeadline(hackathon.getRegistrationDeadline())
                .hackathonStartDate(hackathon.getHackathonStartDate())
                .hackathonEndDate(hackathon.getHackathonEndDate())
                .prizePool(hackathon.getPrizePool())
                .country(hackathon.getCountry())
                .city(hackathon.getCity())
                .domains(hackathon.getDomains())
                .techStacks(hackathon.getTechStacks())
                .tags(hackathon.getTags())
                .createdBy(hackathon.getCreatedBy())
                .createdAt(hackathon.getCreatedAt())
                .updatedAt(hackathon.getUpdatedAt())
                .build();
    }
}
