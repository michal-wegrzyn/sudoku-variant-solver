package sudokusolver.csp.constraints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import sudokusolver.csp.PropagationResult;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class AscendingConstraintTest {
    private AscendingConstraint constraint;

    @BeforeEach
    public void setUp() {
        constraint = new AscendingConstraint(List.of("A", "B", "C", "D"));
    }

    @Test
    @DisplayName("Test propagation with one solution")
    public void testPropagationOneAssignedValues() {
        Set<Integer> domainA = new HashSet<>(Set.of(1, 2, 4));
        Set<Integer> domainB = new HashSet<>(Set.of(1, 2, 3));
        Set<Integer> domainC = new HashSet<>(Set.of(3, 4));
        Set<Integer> domainD = new HashSet<>(Set.of(1, 3, 4));
        PropagationResult result = constraint.propagate(List.of(domainA, domainB, domainC, domainD));
        assertThat(result).isEqualTo(PropagationResult.Satisfied);
        assertThat(domainA).containsExactlyInAnyOrder(1);
        assertThat(domainB).containsExactlyInAnyOrder(2);
        assertThat(domainC).containsExactlyInAnyOrder(3);
        assertThat(domainD).containsExactlyInAnyOrder(4);
    }

    @Test
    @DisplayName("Test propagation with contradiction")
    public void testPropagationWithContradiction() {
        Set<Integer> domainA = new HashSet<>(Set.of(2, 4));
        Set<Integer> domainB = new HashSet<>(Set.of(1, 3));
        Set<Integer> domainC = new HashSet<>(Set.of(2, 4));
        Set<Integer> domainD = new HashSet<>(Set.of(1, 2, 3));
        PropagationResult result = constraint.propagate(List.of(domainA, domainB, domainC, domainD));
        assertThat(result).isEqualTo(PropagationResult.Contradiction);
    }
}
