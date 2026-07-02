package sudokusolver.csp.constraints;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import sudokusolver.csp.Constraint;
import sudokusolver.csp.PropagationResult;

/**
 * Ensures that no two variables in the constraint's scope hold the same value.
 */
public class AllDifferentConstraint extends Constraint {
    public AllDifferentConstraint(List<String> variables) {
        super(variables);
    }

    public AllDifferentConstraint(String... variables) {
        super(variables);
    }

    public PropagationResult propagate(List<Set<Integer>> domains) {
        Set<Integer> availableValues = new HashSet<>();
        for (Set<Integer> domain : domains) {
            availableValues.addAll(domain);
        }
        if (availableValues.size() < domains.size()) {
            return PropagationResult.Contradiction;
        }
        if (availableValues.size() == domains.size()) {
            Map<Integer, Integer> valueToIndex = new HashMap<>();
            for (int i = 0; i < domains.size(); i++) {
                Set<Integer> domain = domains.get(i);
                for (int value : domain) {
                    if (valueToIndex.containsKey(value)) {
                        valueToIndex.put(value, -1); // Mark as duplicate
                    } else {
                        valueToIndex.put(value, i);
                    }
                }
            }
            for (Map.Entry<Integer, Integer> entry : valueToIndex.entrySet()) {
                int value = entry.getKey();
                int index = entry.getValue();
                if (index != -1) {
                    domains.get(index).clear();
                    domains.get(index).add(value);
                }
            }
        }

        Set<Integer> assignedValues = new HashSet<>();
        for (int i = 0; i < domains.size(); i++) {
            Set<Integer> domain = domains.get(i);
            if (domain.size() == 1) {
                int value = domain.iterator().next();
                if (assignedValues.contains(value)) {
                    return PropagationResult.Contradiction;
                }
                assignedValues.add(value);
            }
        }

        for (int i = 0; i < domains.size(); i++) {
            Set<Integer> domain = domains.get(i);
            if (domain.size() > 1) {
                for (int value : assignedValues) {
                    domain.remove(value);
                }
                if (domain.isEmpty()) {
                    return PropagationResult.Contradiction;
                }
            }
        }

        if (assignedValues.size() == domains.size()) {
            return PropagationResult.Satisfied;
        }

        return PropagationResult.Reduced;
    }
}
