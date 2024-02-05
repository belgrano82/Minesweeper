import kotlin.random.Random

class GameField {

    private val mine = Cells.MINE.symbol
    private val safeCell = Cells.SAFECELL.symbol
    private val checkedSell = Cells.CHECKEDSELL.symbol

    private val rows = 9
    private val columns = 9

    private val gameField = MutableList(rows) { MutableList(columns) { safeCell } }

    val listOfMines = mutableListOf<Pair<Int, Int>>()

    var foundMines = 0
    var checkedSafecells = 0

    fun initGameField(mines: Int) {
        var count = 0
        while (count < mines) {
            val row = Random.nextInt(0, rows)
            val cell = Random.nextInt(0, columns)
            if (gameField[row][cell] != mine) {
                gameField[row][cell] = mine
                count++
            }
        }
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                var countMines = 0
                if (gameField[i][j] == safeCell) {
                    val surrounds = checkSurrounds(i, j)
                    for (pair in surrounds) {
                        if ((pair.first >= 0 && pair.second >= 0) && (pair.first < rows && pair.second < columns)) {
                            if (gameField[pair.first][pair.second] == mine) {
                                countMines++
                                gameField[i][j] = countMines.toString()
                            }
                        }
                    }
                } else {
                    listOfMines.add(Pair(i, j))
                }
            }
        }
        for (item in listOfMines) {
            gameField[item.first][item.second] = safeCell
        }
    }


    fun checkSurrounds(x: Int, y: Int): List<Pair<Int, Int>> {
        return listOf(
            Pair(x - 1, y - 1),
            Pair(x - 1, y),
            Pair(x - 1, y + 1),
            Pair(x, y - 1),
            Pair(x, y + 1),
            Pair(x + 1, y - 1),
            Pair(x + 1, y),
            Pair(x + 1, y + 1)
        )
    }

    fun printGameFieldWithMines() {
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

    fun checkInput(x: Int, y: Int) {
        val thisCell = gameField[x][y]
        if (thisCell != safeCell && thisCell != checkedSell) {
            println("There is a number here!")
        } else if (thisCell == checkedSell) {
            gameField[x][y] = safeCell
            if (Pair(x, y) in listOfMines) {
                foundMines--
            } else {
                checkedSafecells--
            }
            printGameFieldWithMines()
        } else {
            gameField[x][y] = checkedSell
            if (Pair(x, y) in listOfMines) {
                foundMines++
            } else {
                checkedSafecells++
            }
            printGameFieldWithMines()
        }
    }
}