package view

import model.Board
import util.CountdownTimer
import java.awt.Color
import javax.swing.JPanel
import java.awt.Dimension
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JLabel
import com.oracle.util.Checksums.update




class InformationPanel(board: Board, countdownTimer: CountdownTimer) : JPanel() {


    companion object {
        private const val INFO_WIDTH = 250
        private const val INFO_HEIGHT = 500
    }

    private val nextPiece: NextPiecePreview = NextPiecePreview(board)
    val scorePanel: ScorePanel =  ScorePanel(board)
    private val timerPanel: TimerPanel = TimerPanel(countdownTimer)

    init {
        background = Color.MAGENTA
        preferredSize = Dimension(INFO_WIDTH, INFO_HEIGHT)
        add(nextPiece)
        add(scorePanel)
        setUpControlInfo()
        add(timerPanel)
    }

    fun updateTimerPanel() {
        timerPanel.update()
    }

    fun newGame() {
        scorePanel.resetLines()
    }


    fun showTimerPanel() {
        timerPanel.showPanel()
    }

    fun hideTimerPanel() {
        timerPanel.hidePanel()
    }
    private fun setUpControlInfo() {
        val infoPanel = JPanel()
        val title = JLabel("Controls")
        val moveRight = JLabel("Right Arrow - Moves the Piece Right")
        val moveLeft = JLabel("Left Arrow - Moves the Piece Left")
        val moveDown = JLabel("Down Arrow - Moves the Piece Down")
        val hardDrop = JLabel("Space - Drops the Piece to the Bottom")
        val rotate = JLabel("R - Rotates the current Piece clockwise")
        val pause = JLabel("P - Pauses/Unpauses the game")
        val box = Box(BoxLayout.PAGE_AXIS)

        box.add(title)
        box.add(moveRight)
        box.add(moveLeft)
        box.add(moveDown)
        box.add(hardDrop)
        box.add(rotate)
        box.add(pause)

        infoPanel.add(box)
        add(infoPanel)
    }
}