package org.example.sushibar.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.sushibar.entities.MenuItemEntity;
import org.example.sushibar.entities.OrderEntity;
import org.example.sushibar.models.MethodStat;
import org.example.sushibar.repositories.MethodStatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Aspect
@Component
public class StatisticAspect {

    private static final Logger logger = LoggerFactory.getLogger(StatisticAspect.class);

    private final MethodStatRepository repository;

    public StatisticAspect(MethodStatRepository repository) {
        this.repository = repository;
    }

    @Around("@annotation(org.example.sushibar.aop.annotations.LogMethod)")
    public Object recordStats(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();

        long start = System.nanoTime() / 1_000_000_000;
        Object result = joinPoint.proceed();
        long duration = (System.nanoTime() - start) / 1_000_000_000;

        MethodStat stat = repository.findByMethodName(methodName)
                .orElseGet(() -> new MethodStat(methodName));

        stat.incrementCount();
        stat.setTotalExecutionTime(stat.getTotalExecutionTime() + duration);
        stat.setMinExecutionTime(Math.min(duration, stat.getMinExecutionTime()));
        stat.setMaxExecutionTime(Math.max(duration, stat.getMaxExecutionTime()));

        if (result instanceof ResponseEntity<?> responseEntity) {
            Object body = responseEntity.getBody();

            // Adapt this if you have a DTO for menu response
            if (body instanceof com.example.models.GetAllMenuItems200Response dto) {
                extractFromList(dto.getContent()).ifPresent(price -> {
                    stat.setMaxReturnedPrice(price);
                    logger.info("Max menu item price: {}", price);
                });
            } else if (body instanceof Map<?, ?> map) {
                Object content = map.get("content");
                if (content instanceof List<?> list) {
                    extractFromList(list).ifPresent(price -> {
                        stat.setMaxReturnedPrice(price);
                        logger.info("Max price from content list: {}", price);
                    });
                }
            } else if (body instanceof List<?> list) {
                extractFromList(list).ifPresent(price -> {
                    stat.setMaxReturnedPrice(price);
                    logger.info("Max price from returned list: {}", price);
                });
            } else if (body instanceof OrderEntity order) {
                extractMaxPriceFromOrder(order).ifPresent(price -> {
                    stat.setMaxReturnedPrice(price);
                    logger.info("Max order item price in order {}: {}", order.getId(), price);
                });
            } else {
            }
        }

        repository.save(stat);
        return result;
    }

    private Optional<Double> extractFromList(List<?> items) {
        return items.stream()
                .map(this::extractPrice)
                .filter(price -> price > 0)
                .max(Double::compare);
    }

    private double extractPrice(Object item) {
        if (item == null) return 0.0;

        if (item instanceof MenuItemEntity menuItem) {
            BigDecimal price = BigDecimal.valueOf(menuItem.getPrice());
            return price.doubleValue();
        }

        if (item instanceof Map<?, ?> map && map.containsKey("price")) {
            Object priceObj = map.get("price");
            if (priceObj instanceof Number) {
                return ((Number) priceObj).doubleValue();
            } else if (priceObj instanceof String str) {
                try {
                    return Double.parseDouble(str);
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            }
        }
        return 0.0;
    }

    private Optional<Double> extractMaxPriceFromOrder(OrderEntity order) {
        if (order == null || order.getItems() == null || order.getItems().isEmpty()) {
            return Optional.empty();
        }

        return order.getItems().stream()
                .map(menuItem -> {
                    if (menuItem == null) return 0.0;
                    BigDecimal price = BigDecimal.valueOf(menuItem.getPrice());
                    return price.doubleValue();
                })
                .filter(price -> price > 0)
                .max(Double::compare);
    }
}
