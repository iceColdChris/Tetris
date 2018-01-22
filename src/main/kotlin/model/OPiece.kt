package model

class OPiece(x: Int, y: Int) : AbstractPiece(MY_ROTATIONS, x, y, Block.O) {
    companion object {
        private val MY_ROTATIONS = arrayOf(arrayOf(intArrayOf(1, 2), intArrayOf(2, 2), intArrayOf(1, 1), intArrayOf(2, 1)))
    }
}