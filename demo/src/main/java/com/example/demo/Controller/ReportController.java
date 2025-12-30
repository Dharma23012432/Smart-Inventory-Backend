package com.example.demo.Controller;

import com.example.demo.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private SaleRepository saleRepository;

    @GetMapping("/revenue")
    public Map<String, Double> revenueSummary() {
        Map<String, Double> result = new HashMap<>();

        LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay();
        LocalDateTime startOfTomorrow = today.plusDays(1).atStartOfDay();

        LocalDateTime startOfYesterday = today.minusDays(1).atStartOfDay();
        LocalDateTime endOfYesterday = startOfToday;

        LocalDateTime startOf7Days = today.minusDays(6).atStartOfDay(); // last 7 days including today
        LocalDateTime startOf30Days = today.minusDays(29).atStartOfDay(); // last 30 days including today

        Double todayTotal = saleRepository.sumTotalAmountBetween(startOfToday, startOfTomorrow);
        Double yesterdayTotal = saleRepository.sumTotalAmountBetween(startOfYesterday, endOfYesterday);
        Double last7DaysTotal = saleRepository.sumTotalAmountBetween(startOf7Days, startOfTomorrow);
        Double last30DaysTotal = saleRepository.sumTotalAmountBetween(startOf30Days, startOfTomorrow);

        result.put("today", todayTotal != null ? todayTotal : 0.0);
        result.put("yesterday", yesterdayTotal != null ? yesterdayTotal : 0.0);
        result.put("last7Days", last7DaysTotal != null ? last7DaysTotal : 0.0);
        result.put("last30Days", last30DaysTotal != null ? last30DaysTotal : 0.0);

        return result;
    }
}
