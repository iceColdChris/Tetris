package model

class SPiece(x: Int, y: Int) : AbstractPiece(MY_ROTATIONS, x, y, Block.S) {
    companion object {
        private val MY_ROTATIONS = arrayOf(arrayOf(intArrayOf(1, 2), intArrayOf(2, 2), intArrayOf(0, 1), intArrayOf(1, 1)),
                arrayOf(intArrayOf(1, 2), intArrayOf(1, 1), intArrayOf(2, 1), intArrayOf(2, 0)),
                arrayOf(intArrayOf(1, 1), intArrayOf(2, 1), intArrayOf(0, 0), intArrayOf(1, 0)),
                arrayOf(intArrayOf(0, 2), intArrayOf(0, 1), intArrayOf(1, 1), intArrayOf(1, 0)))
    }
}