package org.example.sushibar.models.dto;

import org.example.sushibar.models.MethodStat;

/**
 * DTO for exposing method stats cleanly.
 */
public class MethodStatDto {

    private String methodName;
    private long callCount;
    private long totalExecutionTime;
    private long minExecutionTime;
    private long maxExecutionTime;
    private long averageExecutionTime;
    private double usagePercentage;

    // New field for max returned price
    private double maxReturnedPrice;

    public MethodStatDto(MethodStat stat) {
        this.methodName = stat.getMethodName();
        this.callCount = stat.getCallCount();
        this.totalExecutionTime = stat.getTotalExecutionTime();
        this.minExecutionTime = stat.getMinExecutionTime();
        this.maxExecutionTime = stat.getMaxExecutionTime();
        this.averageExecutionTime = stat.getAverageExecutionTime();
        this.maxReturnedPrice = stat.getMaxReturnedPrice();
    }

    // Getters and Setters

    public String getMethodName() {
        return methodName;
    }

    public long getCallCount() {
        return callCount;
    }

    public long getTotalExecutionTime() {
        return totalExecutionTime;
    }

    public long getMinExecutionTime() {
        return minExecutionTime;
    }

    public long getMaxExecutionTime() {
        return maxExecutionTime;
    }

    public long getAverageExecutionTime() {
        return averageExecutionTime;
    }

    public double getUsagePercentage() {
        return usagePercentage;
    }

    public void setUsagePercentage(double usagePercentage) {
        this.usagePercentage = usagePercentage;
    }

    public double getMaxReturnedPrice() {
        return maxReturnedPrice;
    }

    public void setMaxReturnedPrice(double maxReturnedPrice) {
        this.maxReturnedPrice = maxReturnedPrice;
    }
}
