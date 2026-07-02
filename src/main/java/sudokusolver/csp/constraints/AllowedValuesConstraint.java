package sudokusolver.csp.constraints;

import java.util.List;
import java.util.Set;
import sudokusolver.csp.Constraint;
import sudokusolver.csp.PropagationResult;

/**
 * Ensures that each variable in the constraint's scope holds a value from a
 * specified set of allowed values.
 */
public class AllowedValuesConstraint extends Constraint {
    private Set<Integer> allowedValues;

    public AllowedValuesConstraint(List<String> variables, Set<Integer> allowedValues) {
        super(variables);
        this.allowedValues = Set.copyOf(allowedValues);
    }

    public Set<Integer> getAllowedValues() {
        return allowedValues;
    }

    public PropagationResult propagate(List<Set<Integer>> domains) {
        for (Set<Integer> domain : domains) {
            domain.retainAll(allowedValues);
            if (domain.isEmpty()) {
                return PropagationResult.Contradiction;
            }
        }
        return PropagationResult.Satisfied;
    }
}
