enum class CellState(val symbol: String) {
    MINE("X"),
    UNEXPLORED_CELL("."),
    EXPLORED_FREE_CELL("/"),
    EXPLORED_FREE_CELL_WITH_MINES_AROUND("0"),
    UNEXPLORED_MARKED_CELL("*")
}

