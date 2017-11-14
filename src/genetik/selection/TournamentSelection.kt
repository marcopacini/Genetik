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

package genetik.selection

import genetik.Phenotype
import java.util.Collections

class TournamentSelection(tournamentSize: Int): Selection {
    val size: Int = tournamentSize
    var selected: MutableList<Phenotype> = mutableListOf<Phenotype>()

    override fun run(partecipants: Array<Phenotype>) {
        val selection = mutableListOf<Phenotype>()

        // Build groups randomly
        val indeces = MutableList(partecipants.size, { i -> i })
        Collections.shuffle(indeces)

        var i = 0
        while (i < indeces.size) {
            val tournament = mutableListOf<Phenotype>()

            for (j: Int in 0 until size) {
                if (i >= indeces.size) break
                tournament.add(partecipants[indeces[i++]])
            }

            val phenotype = tournament.maxBy { it.fitness }
            if (phenotype != null) selection.add(phenotype)
        }

        selected = selection
    }

    override fun pull(n: Int): Array<Phenotype> {
        if (n > selected.size) {
            throw IllegalArgumentException(
                    String.format("Cannot pull more element than selected: %d > %d", n, selected.size)
            )
        }

        Collections.shuffle(selected)
        return Array(n, { i -> selected[i] })
    }
}