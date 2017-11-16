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

package genetik.crossover

import genetik.Chromosome
import genetik.Gene
import java.util.*

class HalfUniformCrossover(compare: (Gene, Gene) -> Boolean): Crossover {
    val cmp: (Gene, Gene) -> Boolean = compare

    override fun crossover(mother: Chromosome, father: Chromosome): Array<Chromosome> {
        if (mother.genes.size != father.genes.size)
            throw IllegalArgumentException()

        val indeces = mutableListOf<Int>()
        for (i: Int in mother.genes.indices) {
            if (cmp(mother.genes[i], father.genes[i])) indeces.add(i)
        }

        Collections.shuffle(indeces)
        val mid = indeces.size / 2
        repeat(mid) { indeces.removeAt(0) }

        val childrenGenes = Pair(mutableListOf<Gene>(), mutableListOf<Gene>())

        for (i: Int in mother.genes.indices) {
            if (indeces.contains(i)) {
                childrenGenes.second.add(mother.genes[i].clone())
                childrenGenes.first.add(father.genes[i].clone())
            } else {
                childrenGenes.first.add(mother.genes[i].clone())
                childrenGenes.second.add(father.genes[i].clone())
            }
        }

        val geneLength = mother.geneLength

        return arrayOf(
                Chromosome(childrenGenes.first.toTypedArray(), geneLength),
                Chromosome(childrenGenes.second.toTypedArray(), geneLength)
        )
    }
}