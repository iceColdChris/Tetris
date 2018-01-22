package view

import util.CountdownTimer
import java.awt.Font
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.BoxLayout
import java.awt.Font.BOLD
import javax.swing.Box
import javax.xml.datatype.DatatypeConstants.SECONDS




class TimerPanel(val coundownTimer: CountdownTimer) : JPanel() {

    init {
        isVisible = false
        createJLabel()
    }

    companion object {
        private const val TIME_LEFT = "Time Remaining: "
        private const val COLON = ":"
        private const val SECONDS = 1000
        private const val MINUTE = 60
        private const val FONTSIZE = 20
    }
    private var minutes: Int = 0
    private var seconds: Int = 0
    private lateinit var label: JLabel

    private fun getTimeLeft() {
        minutes = (coundownTimer.gameTime / (SECONDS * MINUTE) % MINUTE)
        seconds = (coundownTimer.gameTime / SECONDS) % MINUTE
    }

    fun update() {
        if (!coundownTimer.gameOver) {
            getTimeLeft()
            label.text = TIME_LEFT + minutes + COLON + seconds
        }
    }

    fun showPanel() {
        isVisible = true
    }

    fun hidePanel() {
        isVisible = false
    }

    private fun createJLabel() {
        label = JLabel(TIME_LEFT + minutes + COLON + seconds)
        label.font = Font("default", BOLD, FONTSIZE)
        val box = Box(BoxLayout.PAGE_AXIS)
        box.add(label)
        add(box)
    }
}