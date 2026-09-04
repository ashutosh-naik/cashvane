package com.cashvane.backend.organization;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationRepository organizationRepository;

    public OrganizationController(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @GetMapping
    public List<Organization> getMyOrganization(HttpServletRequest request) {
        String organizationId = (String) request.getAttribute("organizationId");
        UUID orgId = UUID.fromString(organizationId);

        return organizationRepository.findById(orgId)
                .map(List::of)
                .orElse(List.of());
    }
}