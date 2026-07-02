package sudokusolver.csp.constraints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import sudokusolver.csp.PropagationResult;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class SumConstraintTest {
    private SumConstraint constraint;

    @BeforeEach
    public void setUp() {
        constraint = new SumConstraint(List.of("A", "B", "C", "D"), 12);
    }

    @Test
    @DisplayName("Test propagation with two variables to assign")
    public void testPropagationTwoVariablesToAssign() {
        Set<Integer> domainA = new HashSet<>(Set.of(1));
        Set<Integer> domainB = new HashSet<>(Set.of(3));
        Set<Integer> domainC = new HashSet<>(Set.of(2, 3, 4));
        Set<Integer> domainD = new HashSet<>(Set.of(1, 4, 6, 7, 9));
        PropagationResult result = constraint.propagate(List.of(domainA, domainB, domainC, domainD));
        assertThat(result).isEqualTo(PropagationResult.Reduced);
        assertThat(domainA).containsExactlyInAnyOrder(1);
        assertThat(domainB).containsExactlyInAnyOrder(3);
        assertThat(domainC).containsExactlyInAnyOrder(2, 4);
        assertThat(domainD).containsExactlyInAnyOrder(4, 6);
    }

    @Test
    @DisplayName("Test propagation with contradiction")
    public void testPropagationLessAvailableValues() {
        Set<Integer> domainA = new HashSet<>(Set.of(1, 2));
        Set<Integer> domainB = new HashSet<>(Set.of(1, 2, 3));
        Set<Integer> domainC = new HashSet<>(Set.of(1, 3));
        Set<Integer> domainD = new HashSet<>(Set.of(2, 3));
        PropagationResult result = constraint.propagate(List.of(domainA, domainB, domainC, domainD));
        assertThat(result).isEqualTo(PropagationResult.Contradiction);
    }

    @Test
    @DisplayName("Test propagation with exactly one solution")
    public void testPropagationWithExactlyOneSolution() {
        Set<Integer> domainA = new HashSet<>(Set.of(2));
        Set<Integer> domainB = new HashSet<>(Set.of(4));
        Set<Integer> domainC = new HashSet<>(Set.of(1, 2, 5, 6, 8));
        Set<Integer> domainD = new HashSet<>(Set.of(2, 3, 4, 6, 7, 9));
        PropagationResult result = constraint.propagate(List.of(domainA, domainB, domainC, domainD));
        assertThat(result).isEqualTo(PropagationResult.Satisfied);
        assertThat(domainA).containsExactlyInAnyOrder(2);
        assertThat(domainB).containsExactlyInAnyOrder(4);
        assertThat(domainC).containsExactlyInAnyOrder(2);
        assertThat(domainD).containsExactlyInAnyOrder(4);
    }
}
