package org.example.sushibar.models;

import org.example.sushibar.models.dto.MethodStatDto;

import java.util.List;

/**
 * Aggregated statistics summary.
 */
public class StatsSummary {

    private long totalCalls;
    private List<MethodStatDto> methodStats;

    public StatsSummary() {
    }

    public StatsSummary(long totalCalls, List<MethodStatDto> methodStats) {
        this.totalCalls = totalCalls;
        this.methodStats = methodStats;
    }

    public long getTotalCalls() {
        return totalCalls;
    }

    public void setTotalCalls(long totalCalls) {
        this.totalCalls = totalCalls;
    }

    public List<MethodStatDto> getMethodStats() {
        return methodStats;
    }

    public void setMethodStats(List<MethodStatDto> methodStats) {
        this.methodStats = methodStats;
    }
}
