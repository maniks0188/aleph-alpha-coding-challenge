package com.shoppinglist.mgmt.suggestionengine;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;


@Component
public class RecommendationEngineContext {

	private final Map<RecommendationStrategyType, RecommendationStrategy> recommendationEngines;

	public RecommendationEngineContext(List<RecommendationStrategy> strategies) {
		this.recommendationEngines = strategies.stream()
                .collect(Collectors.toMap(RecommendationStrategy::getType, s -> s));
    }

    public List<String> recommend(List<String> currentItems, RecommendationStrategyType strategyKey) {
        RecommendationStrategy strategy = recommendationEngines.get(strategyKey);
        return strategy.suggest(currentItems);
    }
	
	
}
