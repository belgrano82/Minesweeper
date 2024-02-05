fun main() {

    val gameField = GameField()

    print("How many mines do you want on the field? ")
    val amountOfMines = readln().toInt()
    gameField.initGameField(amountOfMines)
    gameField.printGameFieldWithMines()

    while (gameField.foundMines != gameField.listOfMines.size || gameField.checkedSafecells != 0) {
        print("Set/delete mines marks (x and y coordinates): ")
        val (x, y) = readln().split(" ").map { it.toInt() }
        gameField.checkInput(y - 1, x - 1)
    }
    println("Congratulations! You found all the mines!")

}