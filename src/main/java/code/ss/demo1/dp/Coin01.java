package code.ss.demo1.dp;

/**
 * 先看下题目：给你 k 种面值的硬币，面值分别为 c1, c2 ... ck，每种硬币的数量无限，再给一个总金额 amount，问你最少需要几枚硬币凑出这个金额，如果不可能凑出，算法返回 -1 。算法的函数签名如下：
 */
public class Coin01 {

    // coins 中是可选硬币面值，amount 是目标金额
    int coinChange(int[] coins, int amount) {
        return dp(coins, amount);
    }

    int dp(int[] coins, int amount) {
        if (amount == 0) {
            return 0;
        }
        if (amount < 0) {
            return -1;
        }
        int res = Integer.MAX_VALUE;

        for (int i = 0; i < coins.length; i++) {
            int coin = coins[i];
            int subr = dp(coins, amount - coin);
            if (subr == -1) {
                continue;
            }
            res = Math.min(res, subr + 1);
        }
        return res;
    }


}
