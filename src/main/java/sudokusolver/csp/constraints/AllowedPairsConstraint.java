package sudokusolver.csp.constraints;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import sudokusolver.csp.Constraint;
import sudokusolver.csp.PropagationResult;
import sudokusolver.csp.ValuePair;

/**
 * Enforces that the values of two variables belong to a specified set of
 * allowed pairs.
 */
public class AllowedPairsConstraint extends Constraint {
    private Set<ValuePair> allowedPairs;

    public AllowedPairsConstraint(List<String> variables, Set<ValuePair> allowedPairs) {
        if (variables.size() != 2) {
            throw new IllegalArgumentException("AllowedPairsConstraint requires exactly 2 variables");
        }
        super(variables);
        this.allowedPairs = Set.copyOf(allowedPairs);
    }

    public AllowedPairsConstraint(String variable1, String variable2, Set<ValuePair> allowedPairs) {
        super(List.of(variable1, variable2));
        this.allowedPairs = Set.copyOf(allowedPairs);
    }

    public Set<ValuePair> getAllowedPairs() {
        return allowedPairs;
    }

    public PropagationResult propagate(List<Set<Integer>> domains) {
        Set<Integer> domain1 = domains.get(0);
        Set<Integer> domain2 = domains.get(1);
        Set<ValuePair> validPairs = allowedPairs.stream()
                .filter(pair -> domain1.contains(pair.first()) && domain2.contains(pair.second()))
                .collect(Collectors.toSet());
        if (validPairs.isEmpty()) {
            return PropagationResult.Contradiction;
        }

        Set<Integer> newDomain1 = validPairs.stream().map(ValuePair::first)
                .collect(Collectors.toSet());
        Set<Integer> newDomain2 = validPairs.stream().map(ValuePair::second)
                .collect(Collectors.toSet());

        if (newDomain1.isEmpty() || newDomain2.isEmpty()) {
            return PropagationResult.Contradiction;
        }

        domain1.retainAll(newDomain1);
        domain2.retainAll(newDomain2);

        if (validPairs.size() == domain1.size() * domain2.size()) {
            return PropagationResult.Satisfied;
        }
        return PropagationResult.Reduced;
    }
}
