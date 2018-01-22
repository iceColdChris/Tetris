package model

abstract class AbstractPiece(private var rotations: Array<Array<IntArray>>, private var x: Int,
                             private var y: Int, val block: Block) : Piece, Cloneable {
    companion object {
        const val BLOCKS = 4
    }

    var currentRotation: Int = 0

    override fun moveLeft() {
        x--
    }

    override fun moveRight() {
        x++
    }

    override fun moveDown() {
        y--
    }

    override fun rotate() {
        if (rotations.size > 1)
            currentRotation = (currentRotation + 1) % rotations.size
    }

    fun getRotation(): Array<IntArray> {
        return rotations[currentRotation].clone()
    }

    override fun getX(): Int {
        return x
    }

    override fun getY(): Int {
        return y
    }

    fun getBoardCoordinates(): Array<IntArray> {
        val result = Array(BLOCKS) { IntArray(2) }

        for (i in 0 until BLOCKS) {
            result[i][0] = rotations[currentRotation][i][0] + x
            result[i][1] = rotations[currentRotation][i][1] + y
        }
        return result
    }

    @Throws(CloneNotSupportedException::class)
    public override fun clone(): Piece {

        // clone this Piece
        val result = super.clone() as Piece

        // now separately clone the my_rotations 3D array since arrays are mutable
        val newArray3D = Array(rotations.size) { arrayOf(intArrayOf(0)) }
        for (array_2d in 0 until rotations.size) {
            for (array_1d in 0 until rotations[0].size) {
                newArray3D[array_2d][array_1d] = rotations[array_2d][array_1d].clone()
            }
        }
        (result as AbstractPiece).rotations = newArray3D

        return result
    }

    override fun toString(): String {

        val width = width()
        val height = height()
        val sb = StringBuilder()

        for (col in height downTo 0) {
            for (row in 0 until width) {
                var found = false
                for (b in 0 until BLOCKS) {
                    if (rotations[currentRotation][b][1] == col
                            && rotations[currentRotation][b][0] == row) {
                        sb.append("[]")
                        found = true
                        break
                    }
                }
                if (!found) {
                    sb.append("  ")
                }
            }
            sb.append("\n")
        }
        return sb.toString()
    }

    private fun height(): Int {
        var result = 0
        (0 until BLOCKS)
                .asSequence()
                .filter { rotations[currentRotation][it][1] > result }
                .forEach { result = rotations[currentRotation][it][1] }
        return result + 1
    }

    private fun width(): Int {
        var result = 0
        (0 until BLOCKS)
                .asSequence()
                .filter { rotations[currentRotation][it][0] > result }
                .forEach { result = rotations[currentRotation][it][0] }
        return result + 1
    }
}