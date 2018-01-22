package view

import model.Block
import model.Board
import util.CountdownTimer
import java.awt.*
import java.awt.event.KeyAdapter

import javax.swing.JLabel
import javax.swing.JPanel
import java.awt.event.KeyEvent
import javax.swing.Timer
import java.util.*


class GameBoard(private val board: Board, val timer: Timer, val countdownTimer: CountdownTimer) : JPanel(), Observer {

    companion object {
        private const val GAME_WIDTH = 250
        private const val GAME_HEIGHT = 500
        private const val BLOCK_WIDTH = 25
        private const val SCALE = 25
    }


    private var gamePaused: Boolean = false
    private var colorOff: Boolean = false
    private lateinit var pause: JLabel

    init {
        background = Color.WHITE
        preferredSize = Dimension(GAME_WIDTH, GAME_HEIGHT)
        board.addObserver(this)
        addKeyListener(GameAction())
        createPauseFont()
    }

    private fun createPauseFont() {
        pause = JLabel("Game is paused!")
        add(pause)
        pause.font = Font("default", Font.BOLD, SCALE)
        pause.isVisible = false
    }

    fun setColorMode(colorModeEnabled: Boolean) {
        colorOff = colorModeEnabled
    }

    public override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2d : Graphics2D = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON)

        val piece = board.currentPiece
        val deadPiece = board.frozenBlocks
        val coordinates = piece.getBoardCoordinates()

        for (i in 0 until coordinates.size) {
            g2d.paint = piece.block.color
            g2d.fillRect(coordinates[i][0] * SCALE, (-1 * coordinates[i][1]
                    * SCALE) + GAME_HEIGHT - BLOCK_WIDTH, BLOCK_WIDTH, BLOCK_WIDTH)
            g2d.paint = Color.BLACK
            g2d.drawRect(coordinates[i][0] * SCALE, (-1 * coordinates[i][1]
                    * SCALE) + GAME_HEIGHT - BLOCK_WIDTH, BLOCK_WIDTH, BLOCK_WIDTH)
        }

        for (y in 0 until deadPiece.size) {
            val array = deadPiece[y]
            for (x in array.indices) {
                if (array[x] !== Block.EMPTY) {
                    if (colorOff) {
                        g2d.paint = Color.GRAY
                    } else {
                        g2d.paint = array[x].color
                    }
                    g2d.fillRect(x * SCALE, (-1 * y
                            * SCALE) + GAME_HEIGHT - BLOCK_WIDTH, BLOCK_WIDTH, BLOCK_WIDTH)
                    g2d.paint = Color.BLACK
                    g2d.drawRect(x * SCALE, (-1 * y
                            * SCALE) + GAME_HEIGHT - BLOCK_WIDTH, BLOCK_WIDTH, BLOCK_WIDTH)
                }
            }
        }
    }

    override fun update(o: Observable?, arg: Any?) {
        repaint()
    }

    inner class GameAction : KeyAdapter() {

        override fun keyPressed(event: KeyEvent) {

            when (event.keyCode) {
                KeyEvent.VK_LEFT -> if (!gamePaused) board.moveLeft()
                KeyEvent.VK_RIGHT -> if (!gamePaused) board.moveRight()
                KeyEvent.VK_SPACE -> if (!gamePaused) board.hardDrop()
                KeyEvent.VK_R -> if (!gamePaused) board.rotate()
                KeyEvent.VK_DOWN -> if (!gamePaused) board.moveDown()
                KeyEvent.VK_P -> when (gamePaused) {
                    true -> {
                        timer.start()
                        countdownTimer.start()
                        pause.isVisible = false
                        gamePaused = false
                    }
                    false -> {
                        gamePaused = true
                        countdownTimer.stop()
                        timer.stop()
                        pause.isVisible = true
                    }
                }

            }

        }
    }
}