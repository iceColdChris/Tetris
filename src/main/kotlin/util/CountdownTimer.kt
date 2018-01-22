package util

import java.awt.event.ActionListener
import javax.swing.Timer
import javax.swing.JOptionPane



class CountdownTimer(delay: Int, action: ActionListener?)  : Timer(delay, action) {

    companion object {
        private const val GAME_TIME = 300000
        private const val SECOND = 1000
    }

    var gameTime: Int = GAME_TIME
    var gameOver: Boolean = false

    fun countdown() {
        if (gameTime != 0 && !gameOver) {
            gameTime -= SECOND
        } else if (gameTime == 0) {
            gameOver = true

        }
    }

    fun setGameover(isGameOver: Boolean) {
        gameOver = isGameOver
        if (!isGameOver) {
            stop()
        }
    }

    fun reset() {
        gameTime = GAME_TIME
        gameOver = false
    }

    fun displayTimeOut() {
        if (gameTime == 0) {
            JOptionPane.showMessageDialog(null,
                    "Time is up! How did you do?")
        }
    }
}