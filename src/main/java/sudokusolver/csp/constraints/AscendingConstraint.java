package sudokusolver.csp.constraints;

import java.util.List;
import java.util.Set;
import sudokusolver.csp.Constraint;
import sudokusolver.csp.PropagationResult;

/**
 * Ensures that the values of the variables in the constraint's scope are in
 * strictly ascending order.
 */
public class AscendingConstraint extends Constraint {
    public AscendingConstraint(List<String> variables) {
        super(variables);
    }

    public AscendingConstraint(String... variables) {
        super(variables);
    }

    public PropagationResult propagate(List<Set<Integer>> domains) {
        for (int i = 0; i < domains.size() - 1; i++) {
            if (domains.get(i).isEmpty()) {
                return PropagationResult.Contradiction;
            }
            int currentMin = domains.get(i).stream().min(Integer::compareTo).orElseThrow();
            domains.get(i + 1).removeIf(value -> value <= currentMin);

            if (domains.get(i + 1).isEmpty()) {
                return PropagationResult.Contradiction;
            }

        }
        for (int i = domains.size() - 1; i > 0; i--) {
            int currentMax = domains.get(i).stream().max(Integer::compareTo).orElseThrow();
            domains.get(i - 1).removeIf(value -> value >= currentMax);

            if (domains.get(i - 1).isEmpty()) {
                return PropagationResult.Contradiction;
            }
        }

        for (int i = 0; i < domains.size() - 1; i++) {
            int currMax = domains.get(i).stream().max(Integer::compareTo).orElseThrow();
            int nextMin = domains.get(i + 1).stream().min(Integer::compareTo).orElseThrow();
            if (currMax >= nextMin) {
                return PropagationResult.Reduced;
            }
        }

        return PropagationResult.Satisfied;
    }
}
