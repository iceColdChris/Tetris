package view

import model.Board
import java.awt.*
import java.util.*
import javax.swing.JPanel


class NextPiecePreview(private val board: Board) : JPanel(), Observer{

    init {
        board.addObserver(this)
        preferredSize = Dimension(PREVIEW_SIZE, PREVIEW_SIZE)
    }

    companion object {
        private const val SCALE = 15
        private const val VERTICAL_SCALE = 125
        private const val HORIZONTAL_SCALE = 30
        private const val BOARD_HEIGHT = 500
        private const val PREVIEW_SIZE = 80
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        val g2d = g as Graphics2D

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON)


        val piece = board.nextPiece


        val coordinates = piece.getBoardCoordinates()


        for (i in coordinates.indices) {
            g2d.paint = piece.block.color
            g2d.fillRect(coordinates[i][0] * SCALE - HORIZONTAL_SCALE, BOARD_HEIGHT - coordinates[i][1] * SCALE - VERTICAL_SCALE, SCALE, SCALE)
            g2d.paint = Color.BLACK
            g2d.drawRect(coordinates[i][0] * SCALE - HORIZONTAL_SCALE, BOARD_HEIGHT - coordinates[i][1] * SCALE - VERTICAL_SCALE, SCALE, SCALE)

        }
    }

    override fun update(o: Observable?, arg: Any?) {
        repaint()
    }
}