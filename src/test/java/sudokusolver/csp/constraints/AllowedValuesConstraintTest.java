package sudokusolver.csp.constraints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import sudokusolver.csp.PropagationResult;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class AllowedValuesConstraintTest {
    private AllowedValuesConstraint constraint;

    @BeforeEach
    public void setUp() {
        constraint = new AllowedValuesConstraint(List.of("X"), Set.of(1, 2, 3));
    }

    @Test
    @DisplayName("Test propagation with valid domain")
    public void testPropagateValidDomain() {
        Set<Integer> domain1 = new HashSet<>(Set.of(1, 2, 3));
        Set<Integer> domain2 = new HashSet<>(Set.of(2, 3));
        List<Set<Integer>> domains = new ArrayList<>();
        domains.add(domain1);
        domains.add(domain2);
        PropagationResult result = constraint.propagate(domains);
        assertThat(result).isEqualTo(PropagationResult.Satisfied);
        assertThat(domain1).containsExactlyInAnyOrder(1, 2, 3);
        assertThat(domain2).containsExactlyInAnyOrder(2, 3);
    }

    @Test
    @DisplayName("Test propagation with invalid domain")
    public void testPropagateInvalidDomain() {
        Set<Integer> domain = new HashSet<>(Set.of(4, 5, 6));
        List<Set<Integer>> domains = new ArrayList<>();
        domains.add(domain);
        PropagationResult result = constraint.propagate(domains);
        assertThat(result).isEqualTo(PropagationResult.Contradiction);
    }

    @Test
    @DisplayName("Test propagation with partially valid domain")
    public void testPropagatePartiallyValidDomain() {
        Set<Integer> domain1 = new HashSet<>(Set.of(2, 4, 5));
        Set<Integer> domain2 = new HashSet<>(Set.of(1, 3, 6));
        List<Set<Integer>> domains = new ArrayList<>();
        domains.add(domain1);
        domains.add(domain2);
        PropagationResult result = constraint.propagate(domains);
        assertThat(result).isEqualTo(PropagationResult.Satisfied);
        assertThat(domain1).containsExactlyInAnyOrder(2);
        assertThat(domain2).containsExactlyInAnyOrder(1, 3);
    }
}
