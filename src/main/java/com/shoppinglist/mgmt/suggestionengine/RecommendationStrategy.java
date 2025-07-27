package com.shoppinglist.mgmt.suggestionengine;

import java.util.List;

public interface RecommendationStrategy {

	RecommendationStrategyType getType();
	public List<String> suggest(List<String> currentItems);
}
