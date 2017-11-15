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

import genetik.crossover.Crossover
import genetik.selection.Selection
import java.util.Random

class Evolution(size: Int, chromosomeLength: Int, geneLength: Int, eval: (Chromosome) -> Double) {
    val factory: () -> Chromosome = { Chromosome(chromosomeLength, geneLength) }
    val fitness: (Chromosome) -> Double = eval
    var population: MutableList<Phenotype> = MutableList(size, { _ -> Phenotype(factory(), fitness)})

    fun run(
            iteration: Int,
            elitismRatio: Float = .5f,
            selector: Selection,
            crossover: Crossover,
            mutationProbability: Float = .01f
    ): List<Phenotype> {
        repeat(iteration) {
            // Selection
            selector.run(population.toTypedArray())

            // Elitism
            val size = population.size
            val heirs = mutableListOf<Phenotype>()
            val elitism = (size * elitismRatio).toInt()

            repeat(elitism) {
                val phenotype = population.maxBy { it.fitness } as Phenotype
                population.remove(phenotype)
                heirs.add(phenotype)
            }

            // Crossover and mutation
            while (heirs.size < size) {
                val selection = selector.pull()
                val children = crossover.crossover(selection[0].chromosome, selection[1].chromosome)

                children.forEach {
                    h ->
                    if (children.size < size) {
                        h.mutate(mutationProbability)
                        heirs.add(Phenotype(h, fitness))
                    }
                }
            }

            population = heirs
        }

        return population
    }

    companion object {
        val random = Random()

        fun of(size: Int, chromosomeLength: Int, geneLength: Int, fitness: (Chromosome) -> Double): Evolution {
            return Evolution(size, chromosomeLength, geneLength, fitness)
        }
    }
}