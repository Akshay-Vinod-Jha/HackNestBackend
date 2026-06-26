package com.hacknest.backend.services.hackathon;

import com.hacknest.backend.dto.hackathon.CreateHackathonRequest;
import com.hacknest.backend.dto.hackathon.HackathonResponse;
import com.hacknest.backend.enums.HackathonStatus;
import com.hacknest.backend.models.hackathon.Hackathon;
import com.hacknest.backend.repositories.HackathonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HackathonServiceImpl implements HackathonService {

    private final HackathonRepository hackathonRepository;

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

        return buildHackathonResponse(hackathon);
    }

    @Override
    public HackathonResponse getHackathonById(String hackathonId) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));
        return buildHackathonResponse(hackathon);
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
