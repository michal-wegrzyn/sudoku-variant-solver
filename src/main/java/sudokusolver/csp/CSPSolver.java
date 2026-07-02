package sudokusolver.csp;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

/**
 * A static utility class that implements a solving algorithm for Constraint
 * Satisfaction Problems (CSPs).
 */
public class CSPSolver {
    private CSPSolver() {
        // Private constructor to prevent instantiation
        throw new UnsupportedOperationException("CSPSolver is a static class and cannot be instantiated");
    }

    /**
     * Solves the given CSP problem and returns a list of solutions, where each
     * solution is a mapping of variable names to their assigned values.
     *
     * @param problem      the CSP problem to solve
     * @param maxSolutions the maximum number of solutions to find
     * @return a list of solutions, each represented as a map from variable names to
     *         their assigned integer values
     * @throws IllegalArgumentException if the provided CSP problem is not valid
     */
    public static List<Map<String, Integer>> solve(CSPProblem problem, int maxSolutions) {
        if (!problem.isValid())
            throw new IllegalArgumentException(
                    "CSPProblem is not valid: constraints should refer only to variables in domain store");
        List<Map<String, Integer>> solutions = new ArrayList<>();
        if (maxSolutions <= 0)
            return solutions;

        DomainStore domainStore = problem.domainStore.deepCopy();
        DomainStore originalDomains = new DomainStore();
        LinkedHashSet<Constraint> constraintsToPropagate = new LinkedHashSet<>(problem.constraints);

        Map<String, Set<Constraint>> constraintsByVariable = problem.getVariables().stream()
                .collect(Collectors.toMap(var -> var, var -> new HashSet<>()));
        for (Constraint constraint : problem.constraints)
            for (String variable : constraint.getVariables())
                constraintsByVariable.get(variable).add(constraint);

        // Initial propagation before starting the search
        LinkedHashSet<Constraint> satisfiedConstraints = new LinkedHashSet<>();
        boolean result = propagate(domainStore, originalDomains, satisfiedConstraints, constraintsToPropagate,
                constraintsByVariable);
        if (!result) {
            return solutions;
        }
        // Remove the satisfied constraints from the initial propagation step to avoid
        // redundant checks during the search.
        for (String variable : problem.getVariables()) {
            constraintsByVariable.get(variable).removeIf(satisfiedConstraints::contains);
        }
        satisfiedConstraints = new LinkedHashSet<>();
        search(domainStore, maxSolutions, constraintsByVariable, satisfiedConstraints, solutions);
        return solutions;
    }

    /**
     * Performs a backtracking search to find solutions to the CSP problem, while
     * applying constraint propagation at each step to prune the search space.
     * 
     * @param domainStore
     * @param maxSolutions
     * @param constraintsByVariable
     * @param satisfied
     * @param solutions
     */
    private static void search(DomainStore domainStore, int maxSolutions,
            Map<String, Set<Constraint>> constraintsByVariable, LinkedHashSet<Constraint> satisfied,
            List<Map<String, Integer>> solutions) {
        if (solutions.size() >= maxSolutions)
            return;
        String variable = chooseVariableToAssign(domainStore);
        if (variable == null) {
            solutions.add(domainStore.getAssignment());
            return;
        }
        Set<Integer> domain = domainStore.getDomain(variable);
        for (int value : domain) {
            DomainStore originalDomains = new DomainStore();
            originalDomains.addVariable(variable, domain);
            domainStore.assignValue(variable, value);
            LinkedHashSet<Constraint> constraintsToPropagate = new LinkedHashSet<>(constraintsByVariable.get(variable));
            int satisfiedBefore = satisfied.size();
            if (propagate(domainStore, originalDomains, satisfied, constraintsToPropagate, constraintsByVariable)) {
                search(domainStore, maxSolutions, constraintsByVariable, satisfied, solutions);
            }
            domainStore.addVariables(originalDomains);
            while (satisfied.size() > satisfiedBefore) {
                satisfied.removeLast();
            }
            if (solutions.size() >= maxSolutions)
                return;
        }
    }

    /**
     * Performs constraint propagation to reduce the domains.
     * 
     * @param domainStore            current state of variable domains
     * @param originalDomains        if this method is going to modify a domain, it
     *                               will first store the original set here (unless
     *                               already stored) and then put a copy to the
     *                               domainStore
     * @param satisfied              the set of constraints that have been already
     *                               satisfied (they can be skipped), this method
     *                               will add to this set when it finds a new
     *                               satisfied constraint
     * @param constraintsToPropagate the queue of constraints that need to be
     *                               propagated the algorithm will start with, is
     *                               going to become empty at the end
     * @param constraintsByVariable  a mapping from variable names to the set of
     *                               constraints that involve them
     * @return {@code true} if propagation completed without contradictions,
     *         {@code false} if a contradiction was found
     */
    private static boolean propagate(DomainStore domainStore,
            DomainStore originalDomains, Set<Constraint> satisfied,
            LinkedHashSet<Constraint> constraintsToPropagate, Map<String, Set<Constraint>> constraintsByVariable) {
        while (!constraintsToPropagate.isEmpty()) {
            Constraint constraint = constraintsToPropagate.removeFirst();
            if (satisfied.contains(constraint))
                continue;
            List<Set<Integer>> domains = new ArrayList<>();
            List<Integer> sizesBefore = new ArrayList<>();
            for (String variable : constraint.getVariables()) {
                if (!originalDomains.containsVariable(variable)) {
                    originalDomains.addVariable(variable, domainStore.getDomain(variable));
                    domainStore.addVariable(variable, new HashSet<>(originalDomains.getDomain(variable)));
                }
                Set<Integer> domain = domainStore.getDomain(variable);
                domains.add(domain);
                sizesBefore.add(domain.size());
            }
            PropagationResult result = constraint.propagate(domains);
            switch (result) {
                case PropagationResult.Contradiction:
                    return false;
                case PropagationResult.Satisfied:
                    satisfied.add(constraint);
                case PropagationResult.Reduced:
                    for (int i = 0; i < constraint.getVariables().size(); i++) {
                        int new_size = domains.get(i).size();
                        if (new_size == 0)
                            return false;
                        if (new_size != sizesBefore.get(i)) {
                            String variable = constraint.getVariables().get(i);
                            for (Constraint c : constraintsByVariable.get(variable)) {
                                constraintsToPropagate.add(c);
                            }
                        }
                    }
                    break;
            }
        }
        return true;
    }

    /**
     * Selects the next variable to assign a value to, using the Minimum Remaining
     * Values (MRV) heuristic. It returns the variable with the smallest domain size
     * other than 1. If all variables are assigned (domains of size 1), it returns
     * {@code null}.
     * <p>
     * Note: If there is a variable with an empty domain, it will be returned.
     * </p>
     * 
     * @param domainStore
     * @return
     */
    private static String chooseVariableToAssign(DomainStore domainStore) {
        String variable = null;
        int minDomainSize = Integer.MAX_VALUE;
        for (String var : domainStore.getVariables()) {
            int size = domainStore.getDomain(var).size();
            if (size == 0)
                return var;
            if (1 < size && size < minDomainSize) {
                minDomainSize = size;
                variable = var;
            }
        }
        return variable;
    }
}
