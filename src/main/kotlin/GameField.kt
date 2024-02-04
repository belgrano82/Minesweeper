import kotlin.random.Random

class GameField {

    private val mine = Cells.MINE.symbol
    private val safecell = Cells.SAFECELL.symbol

    private val rows = 9
    private val columns = 9

    private val gameField = MutableList(rows) { MutableList(columns) { safecell } }

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
                if (gameField[i][j] == safecell) {
                    val surrounds = checkSurrounds(i, j)
                    for (pair in surrounds) {
                        if ((pair.first >= 0 && pair.second >= 0) && (pair.first < rows && pair.second < columns)) {
                            if (gameField[pair.first][pair.second] == mine) {
                                countMines++
                                gameField[i][j] = countMines.toString()
                            }
                        }
                    }
                }
            }
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
        for (row in gameField) {
            for (cell in row) {
                print(cell)
            }
            println()
        }
    }
}