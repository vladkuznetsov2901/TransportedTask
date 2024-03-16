fun main() {
    // Исходные данные
    val supplies = arrayOf(200, 350, 150) // Поставки
    val demands = arrayOf(100, 100, 80, 90, 170) // Требования
    val costs = arrayOf(
        intArrayOf(1, 4, 5, 3, 1),
        intArrayOf(2, 3, 1, 4, 2),
        intArrayOf(2, 1, 3, 1, 1)
    ) // Матрица стоимостей

    val numCols = costs[0].size + 1

    val costs2 = Array(costs.size) { IntArray(numCols) }

    for (i in costs.indices) {
        for (j in 0 until costs[0].size) {
            costs2[i][j] = costs[i][j]
            costs2[i][costs2[0].size - 1] = 0
        }
    }

    println("--------------------Costs-----------------------")

    for (i in costs.indices) {
        for (j in 0 until costs2[0].size) {
            print("${costs2[i][j]}\t")
        }
        println()
    }
    println("-------------------------------------------------------------")
    println()

    println("--------------------Initial data-----------------------")


    val initialMatrix = Array(supplies.size + 1) { IntArray(demands.size + 1) }

    for ((k, i) in (1 until initialMatrix.size).withIndex()) {

        initialMatrix[i][0] = supplies[k]
    }

    for ((k, i) in (1 until initialMatrix[0].size).withIndex()) {

        initialMatrix[0][i] = demands[k]
    }

    for (i in costs.indices) {
        for (j in costs[i].indices) {
            initialMatrix[i + 1][j + 1] = costs[i][j]
        }
    }


    for (element in initialMatrix) {
        for (j in initialMatrix[0].indices) {
            print("${element[j]}\t")
        }
        println()
    }
    println("-------------------------------------------------------------")

    println("\n")

    println("--------------------NorthWest Algorithm-----------------------")

    val totalCost = northWestAlgorithm(supplies, demands, costs)

    println("-------------------------------------------------------------")

    println("\n")

    println("--------------------Optimize Algorithm-----------------------")


    val matrixOptimize = optimizeAlgorithm(supplies, demands, costs, totalCost)

    println("-------------------------------------------------------------")

    println("\n")

    println("--------------------Dancing Algorithm-----------------------")


    theDancingMethod(costs2, matrixOptimize)

    println("-------------------------------------------------------------")


}

fun northWestAlgorithm(supplies: Array<Int>, demands: Array<Int>, costs: Array<IntArray>): Int {
    val numRows = supplies.size
    val numCols = demands.size

    val suppliesNorthWest = IntArray(numRows)// Поставки
    val demandsNorthWest = IntArray(numCols)// Требования

    for (i in supplies.indices) {
        suppliesNorthWest[i] = supplies[i]
    }
    for (i in demands.indices) {
        demandsNorthWest[i] = demands[i]
    }

    val matrixNorthwest = Array(3) { IntArray(1) }

    // Создаем и заполняем таблицу решения
    val solution = Array(numRows) { IntArray(numCols) }
    for (i in 0 until numRows) {
        for (j in 0 until numCols) {
            val quantity = minOf(suppliesNorthWest[i], demandsNorthWest[j])
            solution[i][j] = quantity
            suppliesNorthWest[i] -= quantity
            demandsNorthWest[j] -= quantity
        }
    }
    var k = 0
    for (el in suppliesNorthWest) {
        if (el > 0) {
            matrixNorthwest[k][0] = el
            k += 1

        } else {
            matrixNorthwest[k][0] = 0
            k += 1
        }
    }


    val numRows2 = solution.size
    val numCols2 = solution[0].size + 1

    val result = Array(numRows2) { IntArray(numCols2) }

    // Объединяем две матрицы в одну
    for (i in 0 until numRows2) {
        for (j in 0 until numCols2 - 1) {
            result[i][j] = solution[i][j]

        }
    }

    for (i in 0 until numRows2) {
        result[i][result[0].size - 1] = matrixNorthwest[i][0]
    }


    // Выводим решение
    println("Решение транспортной задачи:")
    for (i in 0 until numRows2) {
        for (j in 0 until numCols2) {
            print("${result[i][j]}\t")
        }
        println()
    }


    // Выводим общую стоимость решения
    var totalCost = 0
    for (i in 0 until numRows) {
        for (j in 0 until numCols) {
            totalCost += solution[i][j] * costs[i][j]
        }
    }
    println("Общая стоимость решения: $totalCost")
    return totalCost
}

fun optimizeAlgorithm(
    supplies: Array<Int>,
    demands: Array<Int>,
    costs: Array<IntArray>,
    totalCost: Int
): Array<IntArray> {


    val numRows = supplies.size
    val numCols = demands.size

    val suppliesOptimize = IntArray(numRows)// Поставки
    val demandsOptimize = IntArray(numCols)// Требования

    for (i in supplies.indices) {
        suppliesOptimize[i] = supplies[i]
    }
    for (i in demands.indices) {
        demandsOptimize[i] = demands[i]
    }

    val matrixNorthwest = Array(3) { IntArray(1) }

    var min = costs[0][0]
    var max = costs[0][0]

    for (i in costs.indices) {
        for (j in costs.indices) {
            if (costs[i][j] < min) {
                min = costs[i][j]
            }
            if (costs[i][j] > max) {
                max = costs[i][j]
            }
        }

    }

    // Создаем и заполняем таблицу решения
    val solutionOptimize = Array(numRows) { IntArray(numCols) }
    for (k in min..max) {
        for (i in 0 until numRows) {
            for (j in 0 until numCols) {
                if (costs[i][j] == k) {
                    val quantity = minOf(suppliesOptimize[i], demandsOptimize[j])
                    solutionOptimize[i][j] = quantity
                    suppliesOptimize[i] -= quantity
                    demandsOptimize[j] -= quantity
                }

            }
        }
    }
    var k = 0
    for (el in suppliesOptimize) {
        if (el > 0) {
            matrixNorthwest[k][0] = el
            k += 1

        } else {
            matrixNorthwest[k][0] = 0
            k += 1
        }
    }


    val numRows2 = solutionOptimize.size
    val numCols2 = solutionOptimize[0].size + 1

    val result = Array(numRows2) { IntArray(numCols2) }

    // Объединяем две матрицы в одну
    for (i in 0 until numRows2) {
        for (j in 0 until numCols2 - 1) {
            result[i][j] = solutionOptimize[i][j]

        }
    }

    for (i in 0 until numRows2) {
        result[i][result[0].size - 1] = matrixNorthwest[i][0]
    }


    // Выводим решение
    println("Решение транспортной задачи:")
    for (i in 0 until numRows2) {
        for (j in 0 until numCols2) {
            print("${result[i][j]}\t")
        }
        println()
    }


    // Выводим общую стоимость решения
    var totalCostOptimize = 0
    for (i in 0 until numRows) {
        for (j in 0 until numCols) {
            totalCostOptimize += solutionOptimize[i][j] * costs[i][j]
        }
    }
    println("Общая стоимость решения: $totalCostOptimize")

    println("$totalCostOptimize < $totalCost")

    return result

}

fun theDancingMethod(
    costs2: Array<IntArray>,
    matrixOptimize: Array<IntArray>
) {
    val numRows = matrixOptimize.size
    val numCols = matrixOptimize[0].size

    var matrixOptimize2 = matrixOptimize

    val matrixU = IntArray(numRows)// Поставки
    val matrixV = IntArray(numCols)// Требования

    for (i in 0 until numRows) {
        matrixU[i] = -999
    }
    matrixU[0] = 0
    for (i in 0 until numCols) {
        matrixV[i] = -999
    }


    // Создаем и заполняем таблицу решения
    val solution = Array(numRows) { IntArray(numCols) }
    var totalCost = 0

    while (true){
        for (i in 0 until numRows) {
            for (j in 0 until numCols) {
                if (matrixOptimize[i][j] != 0) {
                    if (matrixV[j] != -999) {
                        matrixU[i] = costs2[i][j] - matrixV[j]
                    }
                    if (matrixU[i] == -999 && matrixV[i] != -999) {
                        matrixU[i] = costs2[i][j] - matrixV[j]
                    }
                    if (matrixV[j] == -999 && matrixU[i] != -999) {
                        matrixV[j] = costs2[i][j] - matrixU[i]
                    }
                }

            }
        }

        if (isUVGood(matrixU, matrixV, numRows, numCols)) break

    }


    var indexI = 0
    var indexJ = 0

    for (i in 0 until numRows) {
        for (j in 0 until numCols) {
            if (matrixOptimize[i][j] == 0) {
                val delta = (matrixV[j] + matrixU[i]) - costs2[i][j]
                if (delta > 0) {

                    indexI = i
                    indexJ = j
                }
                solution[i][j] = delta
            }
        }
    }



    println("--------------------------Matrix------------------------------")
    for (i in 0 until numRows) {
        for (j in 0 until numCols) {
            print("${matrixOptimize[i][j]}\t")
        }
        println()
    }
    println("---------------------------------------------------------------")
    println()

    for (i in matrixV.indices) {
        print("        V${i + 1} = ${matrixV[i]}")

    }
    println()
    for (i in matrixU.indices) {
        println("U${i + 1} = ${matrixU[i]}      ")
    }
    println()
    println("----------------------Matrix not optimized--------------------")
    println("------------------------Delta Matrix--------------------------")
    for (i in 0 until numRows) {
        for (j in 0 until numCols) {
            print("${solution[i][j]}\t")
        }
        println()
    }
    println("---------------------------------------------------------------")
    println("Delta = [$indexI;$indexJ]")


    val indexMap = searchCycle(matrixOptimize, indexI, indexJ)

    matrixOptimize2 = optimizeMatrix(indexMap, matrixOptimize, indexI, indexJ)


    println()



    for (i in 0 until numRows) {
        matrixU[i] = -999
    }
    matrixU[0] = 0
    for (i in 0 until numCols) {
        matrixV[i] = -999
    }

    while (true){
        for (i in 0 until numRows) {
            for (j in 0 until numCols) {
                if (matrixOptimize2[i][j] != 0) {
                    if (matrixV[j] != -999) {
                        matrixU[i] = 0
                        matrixU[i] = costs2[i][j] - matrixV[j]
                    }
                    if (matrixU[i] == -999 && matrixV[i] != -999) {
                        matrixU[i] = 0
                        matrixU[i] = costs2[i][j] - matrixV[j]
                    }
                    if (matrixV[j] == -999 && matrixU[i] != -999) {
                        matrixV[j] = 0
                        matrixV[j] = costs2[i][j] - matrixU[i]
                    }
                }

            }
        }

        if (isUVGood(matrixU, matrixV, numRows, numCols)) break

    }

    for (i in 0 until numRows) {
        for (j in 0 until numCols) {
            if (matrixOptimize2[i][j] == 0) {
                val delta = (matrixV[j] + matrixU[i]) - costs2[i][j]
                if (delta > 0) {

                    indexI = i
                    indexJ = j
                }
                solution[i][j] = delta
            }
        }
    }

    println()

    for (i in matrixV.indices) {
        print("        V${i + 1} = ${matrixV[i]}")
    }
    println()
    for (i in matrixU.indices) {
        println("U${i + 1} = ${matrixU[i]}      ")
    }
    println()

    println("----------------------Matrix is optimized--------------------")

    println("---------------------Calculation-----------------------------")
    for (i in 0 until numRows) {
        for (j in 0 until numCols) {
            print("(${matrixOptimize2[i][j]} * ${costs2[i][j]}) + ")
            totalCost += matrixOptimize2[i][j] * costs2[i][j]

        }
        println()
    }
    println(" = $totalCost")
    println("Общая стоимость решения: $totalCost")


}

fun searchCycle(matrixOptimize: Array<IntArray>, indexI: Int, indexJ: Int): ArrayList<Int> {
    val indexMap = arrayListOf<Int>()
    val indexMatrix = Array(2) { IntArray(2) }
    for (i in indexI until matrixOptimize.size) {
        for (j in indexJ until matrixOptimize[0].size) {
            if (i != indexI && j != indexJ
            ) {
                if (matrixOptimize[i][j] != 0) {
                    if (matrixOptimize[indexI][j] != 0) {
                        if (matrixOptimize[i][indexJ] != 0) {
                            indexMap.add(matrixOptimize[i][j])
                            indexMap.add(matrixOptimize[i][indexJ])
                            indexMap.add(matrixOptimize[indexI][j])
                        }
                    }
                }


            }
        }
    }

    indexMap.add(matrixOptimize[indexI][indexJ])
    indexMap.reverse()

    var endCycle = 0
    var k = 0
    var g = 0

    while (endCycle < indexMap.size) {
        indexMatrix[k][g] = indexMap[endCycle]
        g++
        if (g == indexMatrix[0].size) {
            k++
            g = 0
        }

        endCycle++
    }

    println()
    println("------------------Elements of cycle----------------------------")
    println(indexMap)
    println()

    println("-------------------------Cycle Matrix---------------------------------")
    for (i in indexMatrix.indices) {
        for (j in 0 until indexMatrix[0].size) {
            if ((i == 0 && j == 0) || (i == indexI && j == indexJ)){
                print("${indexMatrix[i][j]}(+)\t")
            }
            else print("${indexMatrix[i][j]}(-)\t")

        }
        println()
    }
    println()

    indexMap.remove(0)
    return indexMap
}

fun optimizeMatrix(
    indexMap: ArrayList<Int>, matrixOptimize: Array<IntArray>,
    indexI: Int, indexJ: Int
): Array<IntArray> {

    val min = indexMap.min()

    for (i in indexI until matrixOptimize.size) {
        for (j in indexJ until matrixOptimize[0].size) {
            if (i == indexI && matrixOptimize[i][j] in indexMap &&
                matrixOptimize[i][j] != 0
            ) {
                matrixOptimize[i][j] -= min
            }
            if (j == indexJ && matrixOptimize[i][j] in indexMap) {
                matrixOptimize[i][j] -= min
            } else if (i != indexI && j != indexJ &&
                matrixOptimize[i][j] in indexMap
            ) {
                matrixOptimize[i][j] += min
            }
        }
    }
    matrixOptimize[indexI][indexJ] += min
    println("------------------Optimize matrix----------------------------")
    for (element in matrixOptimize) {
        for (j in 0 until matrixOptimize[0].size) {
            print("${element[j]}\t")
        }
        println()
    }
    return matrixOptimize
}

fun isUVGood(matrixU: IntArray, matrixV: IntArray, numRows: Int, numCols: Int): Boolean {
    var flag = true
    for (i in 0 until numRows) {
        for (j in 0 until numCols) {
            if (matrixU[i] == -999 || matrixV[j] == -999) {
                flag = false
            }
        }
    }
    return flag
}






