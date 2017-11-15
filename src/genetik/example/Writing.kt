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

package genetik.example

import genetik.Chromosome
import genetik.Codec
import genetik.Evolution
import genetik.crossover.MultiPointCrossover
import genetik.selection.TournamentSelection

class Characters private constructor(string: String) {
    var characters: String = string
    val length get() = characters.length

    override fun toString(): String {
        return characters
    }

    companion object: Codec<Characters> {
        val lowercase = "abcdefghijklmnopqrstuvwxyz"
        val uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val symbols = " ,;.!?()<>_-"

        override fun encode(chromosome: Chromosome): Characters {
            if (chromosome.geneLength != 1)
                throw IllegalArgumentException(String.format("wrong gene length: %d != 1", chromosome.geneLength))

            var chars = ""
            val alphabet = lowercase + uppercase + symbols

            for (i: Int in chromosome.genes.indices) {
                val dna = chromosome.genes[i].dna[0]

                chars += alphabet[((alphabet.length - 1) * dna).toInt()]
            }

            return Characters(chars)
        }
    }
}

val target = "A man who dares to waste one hour of time has not discovered the value of life (cit. Charles Darwin)"

fun eval(chromosome: Chromosome): Double {
    val c = Characters.encode(chromosome)

    var score = .0
    for (i: Int in target.indices) {
        if (c.characters[i] == target[i]) score++
    }

    return score
}

fun main(args: Array<String>) {
    val evolution = Evolution.of(
            size = 500,
            chromosomeLength = target.length,
            geneLength = 1,
            fitness = ::eval
    )

    val population = evolution.run(
            iteration = 7500,
            elitismRatio = .15f,
            selector = TournamentSelection(10),
            crossover = MultiPointCrossover(2),
            mutationProbability = .05f
    )

    val solution = population.maxBy { it.fitness }
    println(Characters.encode(solution!!.chromosome))
}
