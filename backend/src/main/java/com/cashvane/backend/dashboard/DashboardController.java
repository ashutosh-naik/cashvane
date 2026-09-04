package com.cashvane.backend.dashboard;

import com.cashvane.backend.expense.Expense;
import com.cashvane.backend.expense.ExpenseRepository;
import com.cashvane.backend.invoice.Invoice;
import com.cashvane.backend.invoice.InvoiceRepository;
import com.cashvane.backend.invoice.InvoiceStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final InvoiceRepository invoiceRepository;
    private final ExpenseRepository expenseRepository;

    public DashboardController(InvoiceRepository invoiceRepository, ExpenseRepository expenseRepository) {
        this.invoiceRepository = invoiceRepository;
        this.expenseRepository = expenseRepository;
    }

    private UUID currentOrgId(HttpServletRequest request) {
        return UUID.fromString((String) request.getAttribute("organizationId"));
    }

    @GetMapping
    public DashboardResponse getDashboard(HttpServletRequest request) {
        UUID orgId = currentOrgId(request);

        List<Invoice> invoices = invoiceRepository.findByOrganizationId(orgId);
        List<Expense> expenses = expenseRepository.findByOrganizationId(orgId);

        BigDecimal totalReceivables = invoices.stream()
                .filter(invoice -> invoice.getStatus() != InvoiceStatus.PAID)
                .map(Invoice::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netPosition = totalReceivables.subtract(totalExpenses);

        return new DashboardResponse(totalReceivables, totalExpenses, netPosition);
    }
}