package model

import java.security.SecureRandom
import java.util.*
import kotlin.collections.ArrayList

class Board(private var width: Int, private var height: Int, private var pieces: ArrayList<AbstractPiece>) : Observable() {

    init {
        newGame(width, height, pieces)
    }

    companion object {
        private const val DEFAULT_WIDTH = 10
        private const val DEFAULT_HEIGHT = 20
        private const val WALL = "|"
        private const val CORNER = "+"
        private const val FLOOR = "-"
        private const val EMPTY = " "
        private const val FROZEN = "X"
        private const val CURRENT_PIECE = "*"
        private val RANDOM = SecureRandom()
        private const val MIN_SIZE = 5
        private const val ROTATIONS = 4
        private const val EXTRA_ROWS = 4
    }

    var frozenBlocks: MutableList<Array<Block>> = mutableListOf()
    lateinit var currentPiece: AbstractPiece
    lateinit var nextPiece: AbstractPiece
    var gameOver: Boolean = false
    var clearedLines: Int = 0

    constructor() : this(DEFAULT_WIDTH, DEFAULT_HEIGHT, ArrayList())

    fun newGame(theWidth: Int, theHeight: Int,
                        thePieces: ArrayList<AbstractPiece>?) {
        if (theWidth < MIN_SIZE || theHeight < MIN_SIZE) {
            throw IllegalArgumentException()
        }
        width = theWidth
        height = theHeight
        frozenBlocks = mutableListOf()
        if (thePieces == null) {
            pieces.clear()
        } else {
            pieces = thePieces
        }
        gameOver = false
        setNextPiece()
        assignCurrentPiece()
        clearedLines = 0
        setChanged()
        notifyObservers()
    }

    fun moveLeft(): Boolean {
        val blocks = currentPiece.getBoardCoordinates()
        val canPass = blocks.indices.none { blocks[it][0] == 0
                ||blockAt(blocks[it][0] - 1, blocks[it][1]) !== Block.EMPTY
        }

        if (canPass) {
            currentPiece.moveLeft()
            setChanged()
            notifyObservers()
        }
        return canPass
    }

    fun moveRight(): Boolean {
        val blocks = currentPiece.getBoardCoordinates()
        val canPass = blocks.indices.none {
            blocks[it][0] == width - 1 ||
                    blockAt(blocks[it][0] + 1, blocks[it][1]) !== Block.EMPTY
        }

        if (canPass) {
            currentPiece.moveRight()
            setChanged()
            notifyObservers()
        }
        return canPass
    }

    fun moveDown(): Boolean {
        val blocks = currentPiece.getBoardCoordinates()
        val canPass = blocks.indices.none {
            blocks[it][1] == 0 ||
                    blockAt(blocks[it][0], blocks[it][1] - 1) !== Block.EMPTY
        }


        if (canPass) {
            currentPiece.moveDown()
            setChanged()
            notifyObservers()
        } else {
            freeze()
        }
        return canPass
    }

    fun hardDrop() {
        var canPass = true
        while (canPass) {
            canPass = moveDown()
        }
    }

    fun rotate(): Boolean {
        currentPiece.rotate()
        val blocks = currentPiece.getBoardCoordinates()
        var canPass = true

        for (dimension in blocks) {
            if (dimension[0] >= width || blockAt(dimension[0], dimension[1]) !== Block.EMPTY) {

                for (i in 1 until ROTATIONS) {
                    currentPiece.rotate()
                }
                canPass = false
                break
            }
        }
        if (canPass) {
            setChanged()
            notifyObservers()
        }
        return canPass
    }

    fun step() {
        moveDown()
    }

    private fun assignCurrentPiece() {
        currentPiece = nextPiece
        setNextPiece()
    }

    private fun setNextPiece() {
        nextPiece = if (pieces.isEmpty()) {
            randomPiece(width / 2 - 2, height)
        } else {
            pieces.removeAt(0)
        }
    }

    private fun randomPiece(theX: Int, theY: Int): AbstractPiece {
        val blocks = Block.values()
        val result: AbstractPiece

        result = when (blocks[RANDOM.nextInt(blocks.size)]) {
            Block.I -> IPiece(theX, theY)
            Block.J -> JPiece(theX, theY)
            Block.L -> LPiece(theX, theY)
            Block.O -> OPiece(theX, theY)
            Block.S -> SPiece(theX, theY)
            Block.T -> TPiece(theX, theY)
            Block.Z -> ZPiece(theX, theY)
            else -> randomPiece(theX, theY)
        }
        return result
    }

    private fun blockAt(x: Int, y: Int): Block? {
        var result: Block? = null
        if (x in 0..(width - 1) && y >= 0) {
            result = Block.EMPTY
            if (x < frozenBlocks.size) {
                result = frozenBlocks[x][y]
            }
        }
        return result
    }

    private fun freeze() {
        val coordinates = currentPiece.getBoardCoordinates()

        for (block in coordinates.indices) {
            val x = coordinates[block][0]
            val y = coordinates[block][1]

            // Add rows until this block can fit in one.
            while (y >= frozenBlocks.size) {
                val newRow = Array(width) { Block.EMPTY }
                for (i in 0 until width) {
                    newRow[i] = Block.EMPTY
                }
                frozenBlocks.add(newRow)
            }
            val row = frozenBlocks[y]
            row[x] = currentPiece.block
        }
        clearLines()
        if (frozenBlocks.size > height) {
            gameOver = true
        } else {
            assignCurrentPiece()
        }
        setChanged()
        notifyObservers()
    }

    private fun clearLines() {

        for (i in frozenBlocks.size - 1 downTo 0) {
            val blocks = frozenBlocks[i]

            val clear = blocks.none { it === Block.EMPTY }
            if (clear) {
                frozenBlocks.removeAt(i)
                clearedLines++
            }
        }
        setChanged()
        notifyObservers(clearedLines)
        clearedLines = 0
    }

    private fun currentPieceAt(theX: Int, theY: Int): Boolean {
        val blocks = currentPiece.getBoardCoordinates()
        return blocks.indices.any { blocks[it][1] == theY && blocks[it][0] == theX }
    }

    private fun getRowString(theRow: Int): String {
        val sb = StringBuilder()

        if (frozenBlocks.size - 1 < theRow) {
            for (column in 0 until width) {
                if (currentPieceAt(column, theRow)) {
                    sb.append(CURRENT_PIECE)
                } else {
                    sb.append(EMPTY)
                }
            }
        } else {
            val rowBlocks = frozenBlocks[theRow]

            for (column in 0 until width) {
                when {
                    currentPieceAt(column, theRow) -> sb.append(CURRENT_PIECE)
                    rowBlocks[column] === Block.EMPTY -> sb.append(EMPTY)
                    else -> sb.append(FROZEN)
                }
            }
        }
        return sb.toString()
    }

    override fun toString(): String {
        val sb = StringBuilder()

        for (i in height + EXTRA_ROWS - 1 downTo height - 1 + 1) {
            sb.append(EMPTY)
            sb.append(getRowString(i))
            sb.append('\n')
        }

        for (i in height - 1 downTo 0) {
            sb.append(WALL)
            sb.append(getRowString(i))
            sb.append(WALL)
            sb.append('\n')
        }
        sb.append(CORNER)
        for (i in 0 until width) {
            sb.append(FLOOR)
        }
        sb.append(CORNER)
        return sb.toString()
    }

}