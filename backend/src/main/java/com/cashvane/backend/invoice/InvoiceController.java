package com.cashvane.backend.invoice;

import com.cashvane.backend.client.Client;
import com.cashvane.backend.client.ClientRepository;
import com.cashvane.backend.organization.OrganizationRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;
    private final ClientRepository clientRepository;
    private final OrganizationRepository organizationRepository;

    public InvoiceController(InvoiceRepository invoiceRepository, ClientRepository clientRepository, OrganizationRepository organizationRepository) {
        this.invoiceRepository = invoiceRepository;
        this.clientRepository = clientRepository;
        this.organizationRepository = organizationRepository;
    }

    private UUID currentOrgId(HttpServletRequest request) {
        return UUID.fromString((String) request.getAttribute("organizationId"));
    }

    public static class CreateInvoiceRequest {
        public UUID clientId;
        public java.time.LocalDate dueDate;
        public java.math.BigDecimal taxPercent;
        public List<InvoiceItem> items;
    }

    @PostMapping
    public Invoice createInvoice(@RequestBody CreateInvoiceRequest request, HttpServletRequest httpRequest) {
        UUID orgId = currentOrgId(httpRequest);

        Client client = clientRepository.findById(request.clientId).orElseThrow();

        Invoice invoice = new Invoice();
        invoice.setOrganization(organizationRepository.findById(orgId).orElseThrow());
        invoice.setClient(client);
        invoice.setDueDate(request.dueDate);
        if (request.taxPercent != null) {
            invoice.setTaxPercent(request.taxPercent);
        }

        for (InvoiceItem item : request.items) {
            item.setInvoice(invoice);
        }
        invoice.setItems(request.items);

        return invoiceRepository.save(invoice);
    }

    @GetMapping
    public List<Invoice> getMyInvoices(HttpServletRequest request) {
        UUID orgId = currentOrgId(request);
        return invoiceRepository.findByOrganizationId(orgId);
    }
}