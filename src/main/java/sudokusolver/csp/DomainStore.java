package sudokusolver.csp;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * A class representing the store for variable domains in a Constraint
 * Satisfaction Problem.
 */
public class DomainStore {
    /** The map storing the domains for each variable. */
    public Map<String, Set<Integer>> domains;

    /** Initializes an empty domain store. */
    public DomainStore() {
        this.domains = new HashMap<>();
    }

    /**
     * Initializes the domain store with a given map of variable domains.
     * <p>
     * <b>Note on Mutability:</b> The provided map is directly assigned
     * to the internal state of this DomainStore without copying.
     * </p>
     * 
     * @param domains
     */
    public DomainStore(Map<String, Set<Integer>> domains) {
        this.domains = domains;
    }

    /**
     * Returns the domain for the specified variable.
     *
     * @param variable the variable for which to retrieve the domain
     * @return the set of possible values for the variable
     */
    public Set<Integer> getDomain(String variable) {
        return domains.get(variable);
    }

    /**
     * Removes a specific value from the domain of the given variable.
     * 
     * @param variable the variable whose domain is to be modified
     * @param value    the value to be removed from the variable's domain
     * @return {@code true} if the value was successfully removed, {@code false} if
     *         the value was not present
     */
    public boolean removeValue(String variable, int value) {
        return domains.get(variable).remove(value);
    }

    /**
     * Adds a specific value to the domain of the given variable.
     * 
     * @param variable the variable whose domain is to be modified
     * @param value    the value to be added to the variable's domain
     * @return {@code true} if the value was added, {@code false} if the value was
     *         already present
     */
    public boolean addValue(String variable, int value) {
        return domains.get(variable).add(value);
    }

    /**
     * Assigns a specific value to the variable by reducing its domain to a
     * singleton set containing only that value.
     *
     * @param variable the variable to be assigned
     * @param value    the value to assign to the variable
     */
    public void assignValue(String variable, int value) {
        Set<Integer> domain = new HashSet<>();
        domain.add(value);
        domains.put(variable, domain);
    }

    /**
     * Returns the set of variables stored in this DomainStore.
     * 
     * @return the set of variable names for which domains are stored
     */
    public Set<String> getVariables() {
        return domains.keySet();
    }

    /**
     * Adds a new variable with its corresponding domain to the store. If the
     * variable already exists, its domain will be overwritten.
     * 
     * @param variable the name of the variable to add
     * @param domain   the set of possible values for the variable
     * 
     *                 <p>
     *                 <b>Note on Mutability:</b> The provided domain set is
     *                 directly assigned to the internal state of this DomainStore
     *                 without copying.
     *                 </p>
     */
    public void addVariable(String variable, Set<Integer> domain) {
        domains.put(variable, domain);
    }

    /**
     * Adds all variables and their corresponding domains from another DomainStore
     * to
     * this store. If a variable already exists, its domain will be overwritten.
     * 
     * @param domainStore the DomainStore from which to copy variables and domains
     */
    public void addVariables(DomainStore domainStore) {
        addVariables(domainStore.domains);
    }

    /**
     * Adds all variables and their corresponding domains from a given map to this
     * store. If a variable already exists, its domain will be overwritten.
     * 
     * @param domains the map of variable names to their corresponding domains to be
     *                added
     */
    public void addVariables(Map<String, Set<Integer>> domains) {
        for (Map.Entry<String, Set<Integer>> entry : domains.entrySet()) {
            this.domains.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Deletes a variable and its corresponding domain from the store.
     *
     * @param variable the name of the variable to delete
     */
    public void deleteVariable(String variable) {
        domains.remove(variable);
    }

    /**
     * Checks if the store contains a domain for the specified variable.
     * 
     * @param variable the name of the variable to check for existence in the store
     * @return {@code true} if the store contains a domain for the variable,
     *         {@code false} otherwise
     */
    public boolean containsVariable(String variable) {
        return domains.containsKey(variable);
    }

    /**
     * Checks if all variables in the store have been assigned a single value (i.e.,
     * their domains are singletons).
     * 
     * @return {@code true} if all variables have singleton domains, {@code false}
     *         otherwise
     */
    public boolean allAssigned() {
        for (Set<Integer> domain : domains.values()) {
            if (domain.size() != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a map representing the current assignment of values to variables.
     * 
     * @return a map from variable names to their assigned values, or {@code null}
     *         if any variable has multiple possible values
     */
    public Map<String, Integer> getAssignment() {
        Map<String, Integer> assignment = new HashMap<>();
        for (Map.Entry<String, Set<Integer>> entry : domains.entrySet()) {
            if (entry.getValue().size() == 1) {
                assignment.put(entry.getKey(), entry.getValue().iterator().next());
            } else {
                return null;
            }
        }
        return assignment;
    }

    /**
     * Copies the domains of all variables in the store.
     * <p>
     * <b>Note on Mutability:</b> This method creates a new map and new sets for
     * each variable's domain, ensuring that the copied domains are independent of
     * the original ones and each other.
     * </p>
     */
    public void copyDomains() {
        Map<String, Set<Integer>> newDomains = new HashMap<>();
        for (Map.Entry<String, Set<Integer>> entry : domains.entrySet()) {
            newDomains.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        this.domains = newDomains;
    }

    /**
     * Creates a deep copy of this DomainStore, including independent copies of all
     * variable domains.
     *
     * @return a new DomainStore instance with copied domains
     */
    public DomainStore deepCopy() {
        DomainStore copy = new DomainStore(domains);
        copy.copyDomains();
        return copy;
    }
}