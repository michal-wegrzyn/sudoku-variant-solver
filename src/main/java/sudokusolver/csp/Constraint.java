package sudokusolver.csp;

import java.util.List;
import java.util.Set;

/**
 * Represents a generic constraint in the Constraint Satisfaction Problem (CSP).
 * Every specific constraint must define the variables it bounds and implement
 * the propagation logic to reduce their domains.
 */
public abstract class Constraint {
    /**
     * The list of variable identifiers involved in this constraint.
     */
    private List<String> variables;

    /**
     * Constructs a constraint with a list of participating variables.
     * The provided list is safely copied to ensure immutability.
     *
     * @param variables the list of variable names scoped by this constraint
     */
    public Constraint(List<String> variables) {
        this.variables = List.copyOf(variables);
    }

    /**
     * Constructs a constraint with a varargs array of participating variables.
     *
     * @param variables the variable names scoped by this constraint
     */
    public Constraint(String... variables) {
        this.variables = List.of(variables);
    }

    /**
     * Returns the unmodifiable list of variables constrained by this instance.
     *
     * @return the list of variable identifiers
     */
    public List<String> getVariables() {
        return variables;
    }

    /**
     * Executes the constraint propagation logic to prune the domains of the scoped
     * variables.
     * <p>
     * <b>Monotonicity Contract:</b> Implementations of this method are only allowed
     * to remove values from the provided domains, they must NEVER inject new
     * values.
     * </p>
     *
     * @param domains a mutable list of domains (sets of available values)
     *                corresponding one-to-one with the indices of the
     *                {@code variables} list
     * @return the {@link PropagationResult} indicating the outcome of this
     *         propagation step.
     */
    public abstract PropagationResult propagate(List<Set<Integer>> domains);
}
