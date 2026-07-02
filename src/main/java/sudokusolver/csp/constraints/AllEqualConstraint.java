package sudokusolver.csp.constraints;

import java.util.List;
import java.util.Set;
import sudokusolver.csp.Constraint;
import sudokusolver.csp.PropagationResult;

/**
 * Ensures that all variables in the constraint's scope hold the same value.
 */
public class AllEqualConstraint extends Constraint {
    public AllEqualConstraint(List<String> variables) {
        super(variables);
    }

    public AllEqualConstraint(String... variables) {
        super(variables);
    }

    public PropagationResult propagate(List<Set<Integer>> domains) {
        Set<Integer> intersection = domains.get(0);
        for (Set<Integer> domain : domains) {
            intersection.retainAll(domain);
            if (intersection.isEmpty()) {
                return PropagationResult.Contradiction;
            }
        }
        for (Set<Integer> domain : domains) {
            domain.retainAll(intersection);
        }
        if (intersection.size() == 1) {
            return PropagationResult.Satisfied;
        }
        return PropagationResult.Reduced;
    }
}
