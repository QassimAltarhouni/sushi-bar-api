package org.example.sushibar.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.sushibar.models.MethodStat;
import org.example.sushibar.repositories.MethodStatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Aspect
@Component
public class StatisticAspect {

    @Autowired
    private MethodStatRepository repository;

    @Around("@annotation(org.example.sushibar.aop.annotations.LogMethod)")
    public Object collectStats(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;

        String methodKey = joinPoint.getSignature().toShortString();

        MethodStat stat = repository.findById(methodKey).orElse(new MethodStat(methodKey));
        stat.incrementCount();
        stat.setLastAccessed(LocalDateTime.now());
        stat.updateTiming(duration);

        repository.save(stat);

        return result;
    }
}
