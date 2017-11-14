/*
 *  MIT License
 *
 *  Copyright (c) 2017 Marco Pacini
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package genetik

import java.util.Random

/**
 * This class represents gene concept. It contains the information encoded line an array of float number.
 */
class Gene: Cloneable {
    var dna: Array<Float>

    constructor(length: Int) {
        dna = Array(length, { _ -> random.nextFloat() })
    }

    private constructor(gene: Gene) {
        dna = Array(gene.dna.size, { i -> gene.dna[i] })
    }

    public override fun clone(): Gene {
        return Gene(this)
    }

    fun mutate(probability: Float, magnitude: Float = 1f): Boolean {
        var mutated = false

        for (i: Int in dna.indices) {
            if (random.nextFloat() < probability) {
                if (magnitude == 1f) {
                    dna[i] = random.nextFloat()
                } else {
                    dna[i] = bound(dna[i] + (2 * random.nextFloat() - 1) * magnitude)
                }

                mutated = true
            }
        }

        return mutated
    }

    companion object {
        val random = Random()

        fun bound(value: Float, interval: Pair<Float, Float> = Pair(0f, 1f)): Float {
            return if (value > interval.first) if (value < interval.second) value else 1f else 0f
        }
    }
}
