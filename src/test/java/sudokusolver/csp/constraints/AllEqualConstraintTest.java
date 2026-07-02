package sudokusolver.csp.constraints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import sudokusolver.csp.PropagationResult;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class AllEqualConstraintTest {
    private AllEqualConstraint constraint;

    @BeforeEach
    public void setUp() {
        constraint = new AllEqualConstraint(List.of("A", "B", "C", "D"));
    }

    @Test
    @DisplayName("Test propagation with one assigned value")
    public void testPropagationOneAssignedValues() {
        Set<Integer> domainA = new HashSet<>(Set.of(1));
        Set<Integer> domainB = new HashSet<>(Set.of(1, 2, 3));
        Set<Integer> domainC = new HashSet<>(Set.of(1, 3, 4));
        Set<Integer> domainD = new HashSet<>(Set.of(1, 2, 3, 4));
        PropagationResult result = constraint.propagate(List.of(domainA, domainB, domainC, domainD));
        assertThat(result).isEqualTo(PropagationResult.Satisfied);
        assertThat(domainA).containsExactlyInAnyOrder(1);
        assertThat(domainB).containsExactlyInAnyOrder(1);
        assertThat(domainC).containsExactlyInAnyOrder(1);
        assertThat(domainD).containsExactlyInAnyOrder(1);
    }

    @Test
    @DisplayName("Test propagation with no common values")
    public void testPropagationNoCommonValues() {
        Set<Integer> domainA = new HashSet<>(Set.of(1, 7));
        Set<Integer> domainB = new HashSet<>(Set.of(3, 4, 5));
        Set<Integer> domainC = new HashSet<>(Set.of(6, 8));
        Set<Integer> domainD = new HashSet<>(Set.of(2, 9));
        PropagationResult result = constraint.propagate(List.of(domainA, domainB, domainC, domainD));
        assertThat(result).isEqualTo(PropagationResult.Contradiction);
    }

    @Test
    @DisplayName("Test propagation with a few common values")
    public void testPropagationFewCommonValues() {
        Set<Integer> domainA = new HashSet<>(Set.of(1, 2, 3, 5, 6));
        Set<Integer> domainB = new HashSet<>(Set.of(1, 2, 4, 5, 8));
        Set<Integer> domainC = new HashSet<>(Set.of(1, 2, 5, 6, 8, 9));
        Set<Integer> domainD = new HashSet<>(Set.of(1, 2, 5, 7));
        PropagationResult result = constraint.propagate(List.of(domainA, domainB, domainC, domainD));
        assertThat(result).isEqualTo(PropagationResult.Reduced);
        assertThat(domainA).containsExactlyInAnyOrder(1, 2, 5);
        assertThat(domainB).containsExactlyInAnyOrder(1, 2, 5);
        assertThat(domainC).containsExactlyInAnyOrder(1, 2, 5);
        assertThat(domainD).containsExactlyInAnyOrder(1, 2, 5);
    }
}
