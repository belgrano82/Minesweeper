fun main() {

    val game = Game()

    print("How many mines do you want on the field? ")
    game.amountOfMines = readln().toInt()
    game.initGameField()
    game.printGameField(game.gameField)

    var coords: Pair<Int, Int>

    var resultOfGame = false
    var stepOfGame = 0

    while (!resultOfGame) {

        print("Set/unset mines marks or claim a cell as free: ")
        val (coord1, coord2, mark) = readln().split(" ").map { it }
        coords = Pair(coord2.toInt() - 1, coord1.toInt() - 1)
        if (stepOfGame == 0) {
            game.checkFirstInput(coords)
            stepOfGame++
        }


            when (mark) {
                "free" -> when (coords) {
                    in game.listOfFreeCells -> game.setCell(mark, coords)
                    in game.mapOfNumbersOfMines -> game.setCell(mark, coords)
                    in game.listOfMines -> {
                        resultOfGame = true
                    }
                }

                "mine" -> game.setMark(coords)
            }

        if (game.listOfMarkedCells.containsAll(game.listOfMines)
            && game.listOfMarkedCells.size == game.listOfMines.size
        ) {
            game.setMine()
            game.printGameField(game.gameField)
            println("Congratulations! You found all the mines!")
            resultOfGame = true
        } else if (game.checkIfAllCellsExplored()) {
            game.printGameField(game.gameField)
            println("Congratulations! You found all the mines!")
            resultOfGame = true
        } else if (resultOfGame) {
            game.setMine()
            game.printGameField(game.gameField)
            println("You stepped on a mine and failed!")
        } else game.printGameField(game.gameField)
    }
}