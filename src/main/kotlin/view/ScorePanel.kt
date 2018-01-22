package view

import model.Board
import java.util.*
import javax.swing.Box
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.BoxLayout



class ScorePanel(private val board: Board) : JPanel(), Observer{

    init {
        board.addObserver(this)
        createScorePanel()
        setLevels()
    }

    companion object {
        private const val LINES_CLEARED = "Lines Cleared: "
        private const val SCORE = "Score: "
        private const val LEVEL = "Level: "
        private const val SCORE_BOOST = 10
        private const val LEVEL_BOOST = 2
    }


    private lateinit var showCleared: JLabel
    private lateinit var showScore: JLabel
    private lateinit var showLevel: JLabel
    private var linesCleared: Int = 0
    private var score: Int = 0
    private var levels: Int = 0
    private var oldLevels: Int = 0

    fun resetLines() {
        linesCleared = 0
        score = 0
        levels = 0
        showCleared.text = LINES_CLEARED + linesCleared
        showScore.text = SCORE + score
        showLevel.text = LEVEL + levels

    }

    private fun createScorePanel() {
        showCleared = JLabel(LINES_CLEARED + linesCleared)
        showScore = JLabel(SCORE + score)
        showLevel = JLabel(LEVEL + levels)
        val box = Box(BoxLayout.PAGE_AXIS)
        box.add(showCleared)
        box.add(showScore)
        box.add(showLevel)
        add(box)
    }

    private fun setLevels() {
        oldLevels = levels
        levels = linesCleared / LEVEL_BOOST
        showLevel.text = "level: " + levels
        firePropertyChange("levels", oldLevels, levels)

    }

    override fun update(o: Observable?, arg: Any?) {
        if (arg is Int && arg > 0) {

            linesCleared += arg
            score += SCORE_BOOST

            if (arg > 1) {
                score += SCORE_BOOST * arg
            }

            showCleared.text = LINES_CLEARED + linesCleared
            showScore.text = SCORE + score
            setLevels()
        }

    }

}