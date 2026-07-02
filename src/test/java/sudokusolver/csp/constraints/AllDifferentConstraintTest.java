package sudokusolver.csp.constraints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import sudokusolver.csp.PropagationResult;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class AllDifferentConstraintTest {
    private AllDifferentConstraint constraint;

    @BeforeEach
    public void setUp() {
        constraint = new AllDifferentConstraint(List.of("A", "B", "C", "D"));
    }

    @Test
    @DisplayName("Test propagation with one assigned value")
    public void testPropagationOneAssignedValues() {
        Set<Integer> domainA = new HashSet<>(Set.of(1));
        Set<Integer> domainB = new HashSet<>(Set.of(1, 2, 3));
        Set<Integer> domainC = new HashSet<>(Set.of(1, 3, 4));
        Set<Integer> domainD = new HashSet<>(Set.of(1, 2, 3, 4));
        PropagationResult result = constraint.propagate(List.of(domainA, domainB, domainC, domainD));
        assertThat(result).isEqualTo(PropagationResult.Reduced);
        assertThat(domainA).containsExactlyInAnyOrder(1);
        assertThat(domainB).containsExactlyInAnyOrder(2, 3);
        assertThat(domainC).containsExactlyInAnyOrder(3, 4);
        assertThat(domainD).containsExactlyInAnyOrder(2, 3, 4);
    }

    @Test
    @DisplayName("Test propagation with less available values than variables")
    public void testPropagationLessAvailableValues() {
        Set<Integer> domainA = new HashSet<>(Set.of(1, 2));
        Set<Integer> domainB = new HashSet<>(Set.of(1, 2, 3));
        Set<Integer> domainC = new HashSet<>(Set.of(1, 3));
        Set<Integer> domainD = new HashSet<>(Set.of(2, 3));
        PropagationResult result = constraint.propagate(List.of(domainA, domainB, domainC, domainD));
        assertThat(result).isEqualTo(PropagationResult.Contradiction);
    }

    @Test
    @DisplayName("Test propagation with exactly as many available values as variables")
    public void testPropagationExactlyAsManyAvailableValues() {
        Set<Integer> domainA = new HashSet<>(Set.of(1, 2, 3));
        Set<Integer> domainB = new HashSet<>(Set.of(1, 2, 3));
        Set<Integer> domainC = new HashSet<>(Set.of(1, 2, 3));
        Set<Integer> domainD = new HashSet<>(Set.of(1, 2, 3, 4));
        PropagationResult result = constraint.propagate(List.of(domainA, domainB, domainC, domainD));
        assertThat(result).isEqualTo(PropagationResult.Reduced);
        assertThat(domainA).containsExactlyInAnyOrder(1, 2, 3);
        assertThat(domainB).containsExactlyInAnyOrder(1, 2, 3);
        assertThat(domainC).containsExactlyInAnyOrder(1, 2, 3);
        assertThat(domainD).containsExactlyInAnyOrder(4);
    }
}
