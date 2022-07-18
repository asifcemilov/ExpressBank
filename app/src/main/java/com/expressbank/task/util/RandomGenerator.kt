package com.expressbank.task.util


class RandomGenerator {

    companion object {

        fun randomBudgets(): IntArray {
            val limit = 100000
            val randoms = IntArray(3)
            randoms[0] = (limit / 2 + limit / 3..limit).random()
            randoms[1] = (limit / 2..limit / 2 + limit / 4).random()
            randoms[2] = (limit / 6..limit / 4).random()
            return randoms
        }

        fun randomDay(): List<Int> {
            val randoms = ArrayList<Int>()

            while (randoms.size < 8) {
                var random = (10..30).random()
                for (i in randoms) {
                    if (random == i) {
                        random = -1
                        break
                    }
                }
                if (random > 0)
                    randoms.add(random)
            }
            return randoms.sorted().reversed()
        }

        fun nRandomsThatSumToLimit(n: Int, limit: Int, zeroValues: Int): List<Int> {
            val randoms = mutableListOf<Int>()
            for (i in 0 until n - zeroValues - 1) {
                randoms.add((1..limit).random())
            }
            randoms.sort()
            var p = 0
            for (i in randoms.indices) {
                val d = randoms[i] - p
                p = randoms[i]
                randoms[i] = d
            }
            randoms.add(limit - p)

            for (i in 0 until zeroValues)
                randoms.add(0)

            randoms.shuffle()

            return randoms.shuffled()
        }

    }
}