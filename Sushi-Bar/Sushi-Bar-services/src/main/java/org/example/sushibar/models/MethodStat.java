package org.example.sushibar.models;

import jakarta.persistence.*;

/**
 * Entity representing performance statistics for a tracked method.
 */
@Entity
@Table(name = "method_stats")
public class MethodStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "method_name", nullable = false, unique = true)
    private String methodName;

    @Column(name = "call_count", nullable = false)
    private long callCount;

    @Column(name = "total_execution_time", nullable = false)
    private long totalExecutionTime;

    @Column(name = "min_execution_time", nullable = false)
    private long minExecutionTime;

    @Column(name = "max_execution_time", nullable = false)
    private long maxExecutionTime;

    // Enable this field to track max returned price of responses
    @Column(name = "max_returned_price")
    private double maxReturnedPrice;

    /**
     * Default constructor for JPA.
     */
    public MethodStat() {
        this.callCount = 0L;
        this.totalExecutionTime = 0L;
        this.minExecutionTime = Long.MAX_VALUE;
        this.maxExecutionTime = 0L;
        this.maxReturnedPrice = 0.0;
    }

    /**
     * Constructor with method name.
     *
     * @param methodName the name of the tracked method
     */
    public MethodStat(String methodName) {
        this();
        this.methodName = methodName;
    }

    // === Getters and Setters ===

    public Long getId() {
        return id;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public long getCallCount() {
        return callCount;
    }

    public void setCallCount(long callCount) {
        this.callCount = callCount;
    }

    public long getTotalExecutionTime() {
        return totalExecutionTime;
    }

    public void setTotalExecutionTime(long totalExecutionTime) {
        this.totalExecutionTime = totalExecutionTime;
    }

    public long getMinExecutionTime() {
        return minExecutionTime;
    }

    public void setMinExecutionTime(long minExecutionTime) {
        this.minExecutionTime = minExecutionTime;
    }

    public long getMaxExecutionTime() {
        return maxExecutionTime;
    }

    public void setMaxExecutionTime(long maxExecutionTime) {
        this.maxExecutionTime = maxExecutionTime;
    }

    public double getMaxReturnedPrice() {
        return maxReturnedPrice;
    }

    public void setMaxReturnedPrice(double maxReturnedPrice) {
        this.maxReturnedPrice = maxReturnedPrice;
    }

    public long getAverageExecutionTime() {
        return callCount == 0 ? 0 : totalExecutionTime / callCount;
    }

    // Called by the aspect to increment usage
    public void incrementCount() {
        this.callCount++;
    }

    @Override
    public String toString() {
        return "MethodStat{" +
                "id=" + id +
                ", methodName='" + methodName + '\'' +
                ", callCount=" + callCount +
                ", totalExecutionTime=" + totalExecutionTime +
                ", minExecutionTime=" + minExecutionTime +
                ", maxExecutionTime=" + maxExecutionTime +
                '}';
    }
}
