package com.cashvane.backend.dashboard;

import java.math.BigDecimal;

public class DashboardResponse {
    public BigDecimal totalReceivables;
    public BigDecimal totalExpenses;
    public BigDecimal netPosition;

    public DashboardResponse(BigDecimal totalReceivables, BigDecimal totalExpenses, BigDecimal netPosition) {
        this.totalReceivables = totalReceivables;
        this.totalExpenses = totalExpenses;
        this.netPosition = netPosition;
    }
}