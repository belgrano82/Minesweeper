fun main() {

    val gameField = GameField()

    print ("How many mines do you want on the field? ")
    val amountOfMines = readln().toInt()
    gameField.initGameField(amountOfMines)
    gameField.printGameFieldWithMines()
}