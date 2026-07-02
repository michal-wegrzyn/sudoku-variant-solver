package sudokusolver.csp.constraints;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import sudokusolver.csp.Constraint;
import sudokusolver.csp.PropagationResult;

/**
 * Enforces that the sum of the values assigned to the variables in its scope
 * equals a specified target sum.
 */
public class SumConstraint extends Constraint {
    /** The target sum for the constraint. */
    private int sum;

    public SumConstraint(List<String> variables, int sum) {
        super(variables);
        this.sum = sum;
    }

    public int getSum() {
        return sum;
    }

    public PropagationResult propagate(List<Set<Integer>> domains) {
        int assignedSum = 0;
        int unassignedCount = 0;
        for (Set<Integer> domain : domains) {
            if (domain.size() == 1) {
                assignedSum += domain.iterator().next();
            } else {
                unassignedCount++;
            }
        }
        if (unassignedCount == 0) {
            return assignedSum == sum ? PropagationResult.Satisfied : PropagationResult.Contradiction;
        }
        if (unassignedCount == 1) {
            int requiredValue = sum - assignedSum;
            for (Set<Integer> domain : domains) {
                if (domain.size() != 1) {
                    if (!domain.contains(requiredValue)) {
                        return PropagationResult.Contradiction;
                    }
                    domain.clear();
                    domain.add(requiredValue);
                    return PropagationResult.Satisfied;
                }
            }
        }
        if (unassignedCount == 2) {
            int requiredValue = sum - assignedSum;
            Set<Integer> firstDomain = null;
            Set<Integer> secondDomain = null;
            for (Set<Integer> domain : domains) {
                if (domain.size() != 1) {
                    if (firstDomain == null) {
                        firstDomain = domain;
                    } else {
                        secondDomain = domain;
                        break;
                    }
                }
            }
            Set<Integer> toRemove = new HashSet<>();
            for (int value : firstDomain) {
                int complement = requiredValue - value;
                if (!secondDomain.contains(complement)) {
                    toRemove.add(value);
                }
            }
            firstDomain.removeAll(toRemove);
            if (firstDomain.isEmpty()) {
                return PropagationResult.Contradiction;
            }
            toRemove.clear();
            for (int value : secondDomain) {
                int complement = requiredValue - value;
                if (!firstDomain.contains(complement)) {
                    toRemove.add(value);
                }
            }
            secondDomain.removeAll(toRemove);
            if (secondDomain.isEmpty()) {
                return PropagationResult.Contradiction;
            }

            return firstDomain.size() == 1 ? PropagationResult.Satisfied : PropagationResult.Reduced;
        }
        int minSum = 0;
        int maxSum = 0;
        for (Set<Integer> domain : domains) {
            if (domain.isEmpty()) {
                return PropagationResult.Contradiction;
            }
            minSum += domain.stream().min(Integer::compareTo).orElse(0);
            maxSum += domain.stream().max(Integer::compareTo).orElse(0);
        }
        boolean canSatisfy = false;
        if (sum >= minSum && sum <= maxSum) {
            canSatisfy = true;
        }
        return canSatisfy ? PropagationResult.Reduced : PropagationResult.Contradiction;
    }
}
