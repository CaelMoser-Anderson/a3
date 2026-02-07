package cs2110;

import java.util.Random;
import static cs2110.LoopInvariants.*;

/**
 * Contains methods for computing the optimal achievable profit of a stock transaction based on its
 * price history in a given time window.
 */
public class Trading {

    /**
     * Returns an *index* of the maximum value in `prices(i..]`. Requires that `0 <= i <
     * prices.length-1`.
     */
    static int argmaxTail(int[] prices, int i) {
        // TODO 2: Implement this method according to its specifications. Use a `while` loop
        //  documented with the invariant you visualized in part 1.
        int max = prices[i + 1];
        int pricesLength = prices.length;
        int j = i+2;
        while (j < prices.length){
            if (max < prices[j]){
                max = prices[j];
            }
            j++;
        }
        return max;
    }

    /**
     * Returns the maximum profit that can be achieved through a transaction (a purchase followed by
     * a sale in a later time period) for the given `prices` window, or returns 0 if no profitable
     * transaction can be made. Requires `prices.length > 1`, and each entry of `prices` is >= 0.
     */
    static int optimalProfit1(int[] prices) {
        // TODO 3: Implement this method according to its specifications. Uncomment and fill in the
        //  definition of this `while` loop so that it has the given loop invariant. The body of
        //  this loop should call `argmaxTail()` in each iteration.

        /*
         * Loop invariant: `optProfit` is the maximum profit that can be achieved when the share is
         * purchased at a time in `[..i)`.
         */
        int i = 0;
        int maxProfits = 0;
        while (i < prices.length) {
            assert optimalProfit1Invariant(prices, optProfit, i);
            if (maxProfits < (argmaxTail(prices, i) - prices[i])){
                maxProfits = argmaxTail(prices, i) - prices[i];
            }
        }
        return maxProfits;
    }

    /**
     * Returns the maximum profit that can be achieved through a transaction (a purchase followed by
     * a sale in a later time period) for the given `prices` window, or returns 0 of no profitable
     * transaction can be made. Requires `prices.length > 1`, and each entry of `prices` is >= 0.
     */
    static int optimalProfit2(int[] prices) {
        // TODO 5: Implement this method according to its specifications. Uncomment and fill in the
        //  definition of this `while` loop so that it has the given loop invariant. Augment this
        //  invariant to account for any additional local variables you modify within the body of
        //  the loop. Your implementation should have worst-case runtime complexity O(N), where
        //  N=prices.length, and worst-case space complexity O(1).

        /*
         * Loop invariant: `optProfit` is the maximum profit that can be achieved when the share
         * is purchased at a time in `(j..]`.
         */
        int j = prices.length - 1;
        int buyPoint = j;
        int sellPoint = j;
        int maxProfit = 0;
        while (j >= 0) {
            assert optimalProfit2Invariant(prices, optProfit, j);
            if (prices[j] <= prices[buyPoint]){
                buyPoint = prices[j];
                maxProfit = prices[buyPoint] - prices[sellPoint];
            }
            if (prices[j]> prices[sellPoint]){
                maxProfit = prices[buyPoint] - prices[sellPoint];
            }
            j--;


        }
        throw new UnsupportedOperationException();
    }

}
