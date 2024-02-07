import kotlin.random.Random

class Game {

    private val mine = CellState.MINE.symbol
    private val unexploredCell = CellState.UNEXPLORED_CELL.symbol
    private val freeCell = CellState.EXPLORED_FREE_CELL.symbol
    private var minesAround = CellState.EXPLORED_FREE_CELL_WITH_MINES_AROUND.symbol.toInt()
    private val markedCell = CellState.UNEXPLORED_MARKED_CELL.symbol
    private var stateOfCellBeforeMark = ""

    private val rows = 9
    private val columns = 9

    val gameField = MutableList(rows) { MutableList(columns) { unexploredCell } }
    var amountOfMines = 0

    val listOfMines = mutableListOf<Pair<Int, Int>>()
    val mapOfNumbersOfMines = mutableMapOf<Pair<Int, Int>, Int>()
    val listOfFreeCells = mutableSetOf<Pair<Int, Int>>()
    val listOfMarkedCells = mutableListOf<Pair<Int, Int>>()
    fun initGameField() {
        setMinesOnGameField(amountOfMines)
        setCellsNearMines(listOfMines)
        setFreeCells()
    }

    fun printGameField(gameField: MutableList<MutableList<String>>) {
        val columnRange = (1..columns).map { it }
        val separator = (1..columns).map { "-" }
        println("\n |${columnRange.joinToString("")}| ")
        println("-|${separator.joinToString("")}|")

        for ((index, row) in gameField.withIndex()) {
            print("${index + 1}|")
            for (cell in row) {
                print(cell)
            }
            println("|")
        }
        println("-|${separator.joinToString("")}|")
    }

    fun setMinesOnGameField(mines: Int) {
        listOfMines.clear()
        var count = 0
        while (count < mines) {
            val row = Random.nextInt(0, rows)
            val cell = Random.nextInt(0, columns)
            if (listOfMines.size == 0) {
                listOfMines.add(Pair(row, cell))
                count++
            } else if (Pair(row, cell) !in listOfMines) {
                listOfMines.add(Pair(row, cell))
                count++
            }
        }
    }

    fun checkFirstInput(coords: Pair<Int, Int>) {
        while (coords in listOfMines) {
            listOfMines.remove(coords)
            listOfMines.add(Pair(Random.nextInt(0, rows), Random.nextInt(0, columns)))
            setCellsNearMines(listOfMines)
            setFreeCells()
        }
    }


    fun setCellsNearMines(listOfMines: MutableList<Pair<Int, Int>>) {
        mapOfNumbersOfMines.clear()
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                val currentCoords = Pair(i, j)
                if (currentCoords !in listOfMines) {
                    val surrounds = checkSurrounds(currentCoords)
                    for (pair in surrounds) {
                        if ((pair.first >= 0 && pair.second >= 0) && (pair.first < rows && pair.second < columns)) {
                            if (Pair(pair.first, pair.second) in listOfMines) {
                                minesAround++
                                mapOfNumbersOfMines[currentCoords] = minesAround
                            }
                        }
                    }
                }
                minesAround = 0
            }
        }
    }

    fun setFreeCells() {
        listOfFreeCells.clear()
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                val currentCoords = Pair(i, j)
                if (currentCoords !in listOfMines && currentCoords !in mapOfNumbersOfMines.keys) {
                    listOfFreeCells.add(currentCoords)
                }
            }
        }
    }

    private fun checkSurrounds(pair: Pair<Int, Int>): List<Pair<Int, Int>> {
        return listOf(
            Pair(pair.first - 1, pair.second - 1),
            Pair(pair.first - 1, pair.second),
            Pair(pair.first - 1, pair.second + 1),
            Pair(pair.first, pair.second - 1),
            Pair(pair.first, pair.second + 1),
            Pair(pair.first + 1, pair.second - 1),
            Pair(pair.first + 1, pair.second),
            Pair(pair.first + 1, pair.second + 1)
        )
    }

    fun setCell(word: String, coords: Pair<Int, Int>): MutableList<MutableList<String>> {
        when (word) {
            "free" -> {
                when (coords) {
                    in listOfFreeCells -> setFreeCellAndNumbers(coords)
                    in mapOfNumbersOfMines -> setNumber(coords)
                }
            }
        }
        return gameField
    }

    private fun setNumber(coords: Pair<Int, Int>) {
        gameField[coords.first][coords.second] = mapOfNumbersOfMines[coords].toString()
    }

    private fun setFreeCellAndNumbers(pair: Pair<Int, Int>) {
        gameField[pair.first][pair.second] = freeCell
        val listOfSurrounds = mutableListOf<Pair<Int, Int>>()
        val listOfUsedSurrounds = mutableListOf<Pair<Int, Int>>()
        listOfSurrounds.add(pair)
        while (listOfSurrounds.size > 0) {
            val surrounds = checkSurrounds(listOfSurrounds.first())
            for (surroundCell in surrounds) {
                if ((surroundCell.first >= 0 && surroundCell.second >= 0) && (surroundCell.first < rows && surroundCell.second < columns)) {
                    if (surroundCell in listOfFreeCells) {
                        gameField[surroundCell.first][surroundCell.second] = freeCell
                        if (surroundCell !in listOfUsedSurrounds) {
                            listOfSurrounds.add(surroundCell)
                        }
                    } else if (surroundCell in mapOfNumbersOfMines) {
                        gameField[surroundCell.first][surroundCell.second] =
                            mapOfNumbersOfMines[surroundCell].toString()
                    }
                }
                listOfUsedSurrounds.add(surroundCell)
            }
            listOfSurrounds.removeAt(0)
        }
    }

    fun setMine() {
        for (thisMine in listOfMines) {
            gameField[thisMine.first][thisMine.second] = mine
        }
    }

    fun setMark(coords: Pair<Int, Int>) {
        if (gameField[coords.first][coords.second] != markedCell) {
            stateOfCellBeforeMark = gameField[coords.first][coords.second]
            gameField[coords.first][coords.second] = markedCell
            listOfMarkedCells.add(coords)
        } else {
            if (stateOfCellBeforeMark == unexploredCell) {
                gameField[coords.first][coords.second] = unexploredCell
            } else {
                when (coords) {
                    in listOfFreeCells -> gameField[coords.first][coords.second] = freeCell
                    in mapOfNumbersOfMines -> gameField[coords.first][coords.second] =
                        mapOfNumbersOfMines[coords].toString()
                }
            }
            listOfMarkedCells.remove(coords)
        }
    }

    fun checkIfAllCellsExplored(): Boolean {
        val count = gameField.flatMap { list -> list.filter { it == unexploredCell } }
        return count.size == listOfMines.size
    }
}