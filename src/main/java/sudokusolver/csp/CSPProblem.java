package sudokusolver.csp;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a constraint satisfaction problem, consisting of a domain store
 * and a list of constraints.
 */
public class CSPProblem {
    /** The store containing variables and their respective domains. */
    public DomainStore domainStore;
    /** The list of constraints applied to this problem. */
    public List<Constraint> constraints;

    /**
     * Constructs a new CSPProblem with a given domain store and constraints.
     * 
     * @param domainStore the initial domain store to use
     * @param constraints the initial list of constraints to apply
     */
    public CSPProblem(DomainStore domainStore, List<Constraint> constraints) {
        this.domainStore = domainStore;
        this.constraints = constraints;
    }

    /**
     * Checks if constraints only refer to variables defined in the domain store.
     * 
     * @return true if all constraints reference valid variables, false otherwise
     */
    public boolean isValid() {
        for (Constraint constraint : constraints)
            for (String variable : constraint.getVariables())
                if (!domainStore.getVariables().contains(variable))
                    return false;
        return true;
    }

    /**
     * Retrieves all variables present in the domain store.
     * 
     * @return a set of all variable names
     */
    public Set<String> getVariables() {
        return domainStore.getVariables();
    }

    /**
     * Adds a variable to the domain store. If the variable already exists, it will
     * be overwritten.
     * 
     * @param variable the name of the variable to add
     * @param domain   the set of allowed values for the variable
     */
    public void addVariable(String variable, Set<Integer> domain) {
        domainStore.addVariable(variable, domain);
    }

    /**
     * Deletes a variable from the domain store and removes all constraints that
     * reference it.
     * 
     * @param variable the name of the variable to remove
     */
    public void deleteVariable(String variable) {
        domainStore.deleteVariable(variable);
        constraints.removeIf(constraint -> constraint.getVariables().contains(variable));
    }

    /**
     * Finds all constraints that apply to a specific variable.
     * 
     * @param variable the name of the variable to filter constraints by
     * @return a list of all constraints that reference the given variable
     */
    public Set<Constraint> getConstraintsForVariable(String variable) {
        return constraints.stream()
                .filter(constraint -> constraint.getVariables().contains(variable))
                .collect(Collectors.toSet());
    }

    /**
     * Adds a constraint to the problem.
     * 
     * @param constraint the constraint to add to the problem
     * @throws IllegalArgumentException if the constraint references a variable
     *                                  not in the domain store
     */
    public void addConstraint(Constraint constraint) {
        for (String variable : constraint.getVariables())
            if (!domainStore.getVariables().contains(variable))
                throw new IllegalArgumentException("Constraint contains variable not in domain store: " + variable);
        constraints.add(constraint);
    }
}
