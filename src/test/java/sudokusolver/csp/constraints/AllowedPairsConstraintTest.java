package sudokusolver.csp.constraints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import sudokusolver.csp.PropagationResult;
import sudokusolver.csp.ValuePair;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class AllowedPairsConstraintTest {
    private AllowedPairsConstraint constraint;

    @BeforeEach
    public void setUp() {
        ValuePair pair1 = new ValuePair(1, 2);
        ValuePair pair2 = new ValuePair(2, 2);
        ValuePair pair3 = new ValuePair(2, 3);
        ValuePair pair4 = new ValuePair(3, 4);
        Set<ValuePair> allowedPairs = new HashSet<>(Set.of(pair1, pair2, pair3, pair4));
        constraint = new AllowedPairsConstraint(List.of("X", "Y"), allowedPairs);
    }

    @Test
    @DisplayName("Test propagation with one possible pair")
    public void testPropagateValidDomain() {
        Set<Integer> domain1 = new HashSet<>(Set.of(1, 3));
        Set<Integer> domain2 = new HashSet<>(Set.of(2, 3, 5));
        List<Set<Integer>> domains = new ArrayList<>();
        domains.add(domain1);
        domains.add(domain2);
        PropagationResult result = constraint.propagate(domains);
        assertThat(result).isEqualTo(PropagationResult.Satisfied);
        assertThat(domain1).containsExactlyInAnyOrder(1);
        assertThat(domain2).containsExactlyInAnyOrder(2);
    }

    @Test
    @DisplayName("Test propagation with invalid domain")
    public void testPropagateInvalidDomain() {
        Set<Integer> domain1 = new HashSet<>(Set.of(1, 3, 4));
        Set<Integer> domain2 = new HashSet<>(Set.of(3, 5));
        List<Set<Integer>> domains = new ArrayList<>();
        domains.add(domain1);
        domains.add(domain2);
        PropagationResult result = constraint.propagate(domains);
        assertThat(result).isEqualTo(PropagationResult.Contradiction);
    }

    @Test
    @DisplayName("Test partial domain reduction")
    public void testPropagatePartiallyValidDomain() {
        Set<Integer> domain1 = new HashSet<>(Set.of(1, 2, 3, 4, 5));
        Set<Integer> domain2 = new HashSet<>(Set.of(1, 2, 3, 5, 7));
        List<Set<Integer>> domains = new ArrayList<>();
        domains.add(domain1);
        domains.add(domain2);
        PropagationResult result = constraint.propagate(domains);
        assertThat(result).isEqualTo(PropagationResult.Reduced);
        assertThat(domain1).containsExactlyInAnyOrder(1, 2);
        assertThat(domain2).containsExactlyInAnyOrder(2, 3);
    }
}
