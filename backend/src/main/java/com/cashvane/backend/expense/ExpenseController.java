package com.cashvane.backend.expense;

import com.cashvane.backend.organization.OrganizationRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;
    private final OrganizationRepository organizationRepository;

    public ExpenseController(ExpenseRepository expenseRepository, OrganizationRepository organizationRepository) {
        this.expenseRepository = expenseRepository;
        this.organizationRepository = organizationRepository;
    }

    private UUID currentOrgId(HttpServletRequest request) {
        return UUID.fromString((String) request.getAttribute("organizationId"));
    }

    @PostMapping
    public Expense createExpense(@RequestBody Expense expense, HttpServletRequest request) {
        UUID orgId = currentOrgId(request);
        expense.setOrganization(organizationRepository.findById(orgId).orElseThrow());
        return expenseRepository.save(expense);
    }

    @GetMapping
    public List<Expense> getMyExpenses(HttpServletRequest request) {
        UUID orgId = currentOrgId(request);
        return expenseRepository.findByOrganizationId(orgId);
    }
}