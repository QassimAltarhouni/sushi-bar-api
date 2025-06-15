package org.example.sushibar.controllers;

import org.example.sushibar.models.MethodStat;
import org.example.sushibar.models.StatsSummary;
import org.example.sushibar.models.dto.MethodStatDto;
import org.example.sushibar.entities.*;
import org.example.sushibar.repositories.MethodStatRepository;
import org.example.sushibar.repositories.MenuItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final MethodStatRepository methodStatRepository;
    private final MenuItemRepository menuItemRepository;

    public StatisticsController(MethodStatRepository methodStatRepository,
                                MenuItemRepository menuItemRepository) {
        this.methodStatRepository = methodStatRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @GetMapping("/methods")
    public List<MethodStatDto> getAllMethodStats() {
        return methodStatRepository.findAll().stream()
                .map(MethodStatDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/methods/top/{n}")
    public List<MethodStatDto> getTopMethods(@PathVariable int n) {
        return methodStatRepository.findAll().stream()
                .sorted(Comparator.comparingLong(MethodStat::getCallCount).reversed())
                .limit(n)
                .map(MethodStatDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/summary")
    public StatsSummary getStatisticsSummary() {
        List<MethodStat> stats = methodStatRepository.findAll();
        long totalCalls = stats.stream().mapToLong(MethodStat::getCallCount).sum();
        // ✅ Get max real price from the menu table
        double maxRealPrice = menuItemRepository.findAll().stream()
                .mapToDouble(MenuItemEntity::getPrice)
                .max()
                .orElse(0.0);

        List<MethodStatDto> withPercentage = stats.stream().map(stat -> {
            double percent = totalCalls > 0 ? (stat.getCallCount() * 100.0 / totalCalls) : 0.0;
            MethodStatDto dto = new MethodStatDto(stat);
            dto.setMaxReturnedPrice(maxRealPrice);
            dto.setUsagePercentage(percent);
            return dto;
        }).collect(Collectors.toList());

        return new StatsSummary(totalCalls, withPercentage);
    }

    @GetMapping("/dashboard")
    public Map<String, Object> getStatisticsDashboard() {
        List<MethodStat> stats = methodStatRepository.findAll();
        long totalCalls = stats.stream().mapToLong(MethodStat::getCallCount).sum();

        MethodStat mostCalled = stats.stream()
                .max(Comparator.comparingLong(MethodStat::getCallCount))
                .orElse(null);

        long highestMax = stats.stream()
                .mapToLong(MethodStat::getMaxExecutionTime)
                .max().orElse(0);

        long lowestMin = stats.stream()
                .mapToLong(MethodStat::getMinExecutionTime)
                .min().orElse(0);

        // ✅ Get max real price from the menu table
        double maxRealPrice = menuItemRepository.findAll().stream()
                .mapToDouble(MenuItemEntity::getPrice)
                .max()
                .orElse(0.0);

        Map<String, Object> dashboard = new LinkedHashMap<>();
        dashboard.put("totalCalls", totalCalls);
        dashboard.put("mostCalledMethod", mostCalled != null ? mostCalled.getMethodName() : null);
        dashboard.put("mostCalledCount", mostCalled != null ? mostCalled.getCallCount() : 0);
        dashboard.put("maxExecutionTime", highestMax);
        dashboard.put("minExecutionTime", lowestMin);
        dashboard.put("maxReturnedPrice", maxRealPrice); // ✅ Now accurate

        return dashboard;
    }

    @GetMapping("/methods/paginated")
    public Page<MethodStatDto> getPaginatedStats(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return methodStatRepository.findAll(pageable).map(MethodStatDto::new);
    }
}
