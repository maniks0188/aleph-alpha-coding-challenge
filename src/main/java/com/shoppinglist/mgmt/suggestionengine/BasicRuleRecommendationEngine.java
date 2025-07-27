package com.shoppinglist.mgmt.suggestionengine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;


/**
 * This class acts as the component for the recommendation engine.
 * This class contains the method to provide suggestions based upon the input items.
 * 
 * This class can be extended further to add support for different suggestion logic.
 *
 */
@Component
public class BasicRuleRecommendationEngine implements RecommendationStrategy {

	private final Map<Set<String>, List<String>> additionalItemsMap = Map.of(
	        Set.of("item124546", "item124547"), List.of("item1245461", "item1245462"),
	        Set.of("item124545","item1245460"), List.of("item1245463"),
	        Set.of("item124548"), List.of("item1245464","item1245465"),
	        Set.of("item1245460","item124549","item124546"), List.of("item1245466","item1245467")
	    );
	
	@Override
	public RecommendationStrategyType getType() {
		return RecommendationStrategyType.BASIC;
	}
	
	@Override
	public List<String> suggest(List<String> currentItems) {
        Set<String> currentSet = currentItems.stream()
            .collect(Collectors.toSet());

        Set<String> suggestions = new HashSet<>();

        for (Map.Entry<Set<String>, List<String>> entry : additionalItemsMap.entrySet()) {
            if (currentSet.containsAll(entry.getKey())) {
                for (String suggestion : entry.getValue()) {
                    if (!currentSet.contains(suggestion.toLowerCase())) {
                        suggestions.add(suggestion);
                    }
                }
                break;
            }
        }

        return new ArrayList<>(suggestions);
    }

	
}
