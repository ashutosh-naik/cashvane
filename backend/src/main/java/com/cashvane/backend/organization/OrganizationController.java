package com.cashvane.backend.organization;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationRepository organizationRepository;

    public OrganizationController(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @PostMapping
    public Organization createOrganization(@RequestBody Organization organization) {
        return organizationRepository.save(organization);
    }

    @GetMapping
    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }
}