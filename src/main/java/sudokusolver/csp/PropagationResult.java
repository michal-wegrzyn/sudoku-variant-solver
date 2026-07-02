package sudokusolver.csp;

/**
 * Represents the outcome of a constraint propagation step.
 */
public enum PropagationResult {
    /**
     * A contradiction was found during propagation, meaning
     * the current partial assignment cannot lead to a valid solution.
     */
    Contradiction,

    /**
     * No contradiction was detected, and the constraint cannot yet be definitively
     * marked as permanently satisfied.
     * <p>
     * <b>Strict Semantics:</b> This is a state of incomplete knowledge. It does NOT
     * prove the absence of future contradictions, nor does it prove that the
     * constraint will fail.
     * </p>
     * <p>
     * <i>Edge Case Exception:</i> If, after propagation, all domains associated
     * with this constraint are reduced to exactly 1 element, {@code Reduced}
     * becomes logically equivalent to {@link #Satisfied} (guaranteeing the absence
     * of contradictions).
     * </p>
     */
    Reduced,

    /**
     * The constraint is guaranteed to be satisfied under the current domain states.
     */
    Satisfied
}