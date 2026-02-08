package cs2110;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

class TradingTest {

    // --- argmaxTail Tests ---

    @Test
    void testArgmaxTailBasic() {
        // Range to search: indices (i..] -> (0..5] -> indices 1 to 5
        // Array: [10, 5, 8, 20, 15, 3]
        // Max in range indices 1-5 is 20 (at index 3)
        int[] prices = {10, 5, 8, 20, 15, 3};
        int i = 0;
        int expectedIndex = 3;

        assertEquals(expectedIndex, Trading.argmaxTail(prices, i),
                "Should return index 3 (value 20) for i=0");
    }

    @Test
    void testArgmaxTailAtEnd() {
        // Range to search: indices (3..4] -> index 4 only
        int[] prices = {10, 20, 30, 40, 50};
        int i = 3;
        // The only valid index after 3 is 4.
        assertEquals(4, Trading.argmaxTail(prices, i));
    }

    @Test
    void testArgmaxTailDescending() {
        // Range (0..4]: 40, 30, 20, 10
        // The max value in the tail is 40 at index 1
        int[] prices = {50, 40, 30, 20, 10};
        assertEquals(1, Trading.argmaxTail(prices, 0));
    }

    @Test
    void testArgmaxTailNoSideEffects() {
        // This test checks if your code accidentally modifies the array
        int[] prices = {10, 30, 20};
        int[] copy = prices.clone();

        Trading.argmaxTail(prices, 0);

        assertArrayEquals(copy, prices,
                "argmaxTail should not modify the contents of the prices array");
    }

    // --- Optimal Profit Tests (Tests both implementation 1 and 2) ---

    /**
     * Helper method to test both optimalProfit implementations against the same data.
     */
    private void assertBestProfit(int expectedProfit, int[] prices) {
        // We use a clone for each call to ensure one method doesn't mess up data for the other

        // Test Method 1
        int[] p1 = prices.clone();
        // Set a timeout because the current implementation has an infinite loop
        assertTimeoutPreemptively(java.time.Duration.ofMillis(500), () -> {
            assertEquals(expectedProfit, Trading.optimalProfit1(p1),
                    "optimalProfit1 failed for: " + Arrays.toString(prices));
        }, "optimalProfit1 timed out (likely infinite loop)");

        // Test Method 2
        int[] p2 = prices.clone();
        try {
            assertEquals(expectedProfit, Trading.optimalProfit2(p2),
                    "optimalProfit2 failed for: " + Arrays.toString(prices));
        } catch (UnsupportedOperationException e) {
            fail("optimalProfit2 threw UnsupportedOperationException (Implementation incomplete)");
        }
    }

    @Test
    void testProfitSimpleIncrease() {
        // Buy at 10, Sell at 50 -> Profit 40
        int[] prices = {10, 20, 50, 30};
        assertBestProfit(40, prices);
    }

    @Test
    void testProfitSimpleDecrease() {
        // Price always goes down. Best move is not to transact (Profit 0).
        int[] prices = {50, 40, 30, 20, 10};
        assertBestProfit(0, prices);
    }

    @Test
    void testProfitValley() {
        // Price drops then recovers.
        // {10, 5, 20} -> Buy at 5, Sell at 20 -> Profit 15.
        // (Buying at 10 and selling at 20 is only profit 10).
        int[] prices = {10, 5, 20};
        assertBestProfit(15, prices);
    }

    @Test
    void testProfitTwoPeaks() {
        // Scenario: {5, 10, 4, 20}
        // Option 1: Buy 5, Sell 10 (Profit 5)
        // Option 2: Buy 5, Sell 20 (Profit 15)
        // Option 3: Buy 4, Sell 20 (Profit 16) -> Winner
        int[] prices = {5, 10, 4, 20};
        assertBestProfit(16, prices);
    }

    @Test
    void testProfitLateBloom() {
        // Long period of decline, then a massive spike at the end
        // Buy at 1 (index 4), sell at 100 (index 5) -> Profit 99
        int[] prices = {50, 40, 30, 20, 1, 100};
        assertBestProfit(99, prices);
    }

    @Test
    void testProfitFlatLine() {
        // No movement
        int[] prices = {10, 10, 10, 10};
        assertBestProfit(0, prices);
    }

    @Test
    void testProfitMinimumLength() {
        // Precondition says length > 1
        int[] prices = {10, 15};
        assertBestProfit(5, prices);
    }
}