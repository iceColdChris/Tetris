package model

class TPiece(x: Int, y: Int) : AbstractPiece(MY_ROTATIONS, x, y, Block.T) {
    companion object {
        private val MY_ROTATIONS = arrayOf(arrayOf(intArrayOf(1, 2), intArrayOf(0, 1), intArrayOf(1, 1), intArrayOf(2, 1)),
                arrayOf(intArrayOf(1, 2), intArrayOf(1, 1), intArrayOf(2, 1), intArrayOf(1, 0)),
                arrayOf(intArrayOf(0, 1), intArrayOf(1, 1), intArrayOf(2, 1), intArrayOf(1, 0)),
                arrayOf(intArrayOf(1, 2), intArrayOf(0, 1), intArrayOf(1, 1), intArrayOf(1, 0)))
    }
}