package sudokusolver.csp;

/**
 * An immutable holder for a pair of primitive integer values.
 *
 * @param first  the first integer value of the pair
 * @param second the second integer value of the pair
 */
public record ValuePair(int first, int second) {
    /**
     * Returns a string representation of this pair in the format
     * {@code Pair[first, second]}.
     *
     * @return a formatted string representing the pair
     */
    @Override
    public String toString() {
        return "Pair[" + first + ", " + second + "]";
    }
}
