package com.cashvane.backend.client;

import com.cashvane.backend.organization.OrganizationRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientRepository clientRepository;
    private final OrganizationRepository organizationRepository;

    public ClientController(ClientRepository clientRepository, OrganizationRepository organizationRepository) {
        this.clientRepository = clientRepository;
        this.organizationRepository = organizationRepository;
    }

    private UUID currentOrgId(HttpServletRequest request) {
        return UUID.fromString((String) request.getAttribute("organizationId"));
    }

    @PostMapping
    public Client createClient(@RequestBody Client client, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (role.equals("VIEWER")) {
            throw new com.cashvane.backend.security.AccessDeniedException("Viewers cannot create clients");
        }

        UUID orgId = currentOrgId(request);
        client.setOrganization(organizationRepository.findById(orgId).orElseThrow());
        return clientRepository.save(client);
    }

    @GetMapping
    public List<Client> getMyClients(HttpServletRequest request) {
        UUID orgId = currentOrgId(request);
        return clientRepository.findByOrganizationId(orgId);
    }
}