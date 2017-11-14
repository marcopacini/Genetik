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
 *  This class represent a chromosome that encode a solution as a set of gene.
 */
open class Chromosome: Cloneable {
    val genes: Array<Gene>
    val geneLength: Int

    constructor(length: Int, gl: Int) {
        genes = Array(length, { _ -> Gene(gl) })
        geneLength = gl
    }

    constructor(g: Array<Gene>, gl: Int) {
        genes = g
        geneLength = gl
    }

    public override fun clone(): Chromosome {
        val g = mutableListOf<Gene>()
        for (i: Int in genes.indices) g.add(genes[i].clone())

        return Chromosome(g.toTypedArray(), geneLength)
    }

    open fun multiPointCrossover(chromosome: Chromosome, n: Int): Array<Chromosome> {
        // Argument validation
        if (genes.size != chromosome.genes.size)
            throw IllegalArgumentException()

        val sources = arrayListOf<Chromosome>(this, chromosome)
        val pivots = Array(n, { _ -> random.nextInt(genes.size) }).sortedArray()

        val childrenGenes = Pair(mutableListOf<Gene>(), mutableListOf<Gene>())

        var p = 0
        for (i: Int in genes.indices) {
            if (p < pivots.size && i >= pivots[p]) p++

            childrenGenes.first.add(sources[p % sources.size].genes[i].clone())
            childrenGenes.second.add(sources[(p + 1) % sources.size].genes[i].clone())
        }

        return arrayOf(
                Chromosome(childrenGenes.first.toTypedArray(), geneLength),
                Chromosome(childrenGenes.second.toTypedArray(), geneLength)
        )
    }

    fun mutate(probability: Float, magnitude: Float = 1f): Boolean {
        var mutated = false

        for (i: Int in genes.indices) {
            if (genes[i].mutate(probability, magnitude)) mutated = true
        }

        return mutated
    }

    companion object {
        val random = Random()
    }
}