package view

import model.Board
import util.CountdownTimer
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Toolkit
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import javax.swing.*


class TetrisGUI : PropertyChangeListener {

    companion object {
        private val KIT = Toolkit.getDefaultToolkit()
        private val SCREEN_SIZE = KIT.screenSize
        private val SCREEN_WIDTH = SCREEN_SIZE.width
        private val SCREEN_HEIGHT = SCREEN_SIZE.height
        private const val SECOND = 1000
        private const val DIFFICULTY_INCREASE = 100
        private const val GAME_WIDTH = 10
        private const val GAME_HEIGHT = 20
    }

    private var frame: JFrame = JFrame("Kotlin Tetris")
    private var newGame: JMenuItem = JMenuItem("New Game")
    private var timerStep = SECOND
    private val timer = Timer(timerStep, null)
    private val countdownTimer = CountdownTimer(timerStep, null)
    private val board = Board()
    private val gameBoard = GameBoard(board, timer, countdownTimer)
    private val info = InformationPanel(board, countdownTimer)
    private var panelView: JPanel? = null
    private var timeAttack: JCheckBoxMenuItem? = null
    private var frozenColor: JCheckBoxMenuItem? = null

    fun start() {
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.jMenuBar = createMenu()
        frame.add(createPanel())
        frame.add(info, BorderLayout.EAST)
        frame.isVisible = true
        frame.isResizable = false
        info.scorePanel.addPropertyChangeListener(this)
        frame.pack()
        frame.setLocation(SCREEN_WIDTH / 2 - frame.width / 2,
                SCREEN_HEIGHT / 2 - frame.height / 2)


        timerSetUp()

    }

    private fun timerSetUp() {
        timer.addActionListener({
            gameOver()
            board.step()
        })

        countdownTimer.addActionListener({
            timeAttackGameOver()
            info.updateTimerPanel()
            countdownTimer.countdown()
        })

        timer.start()
    }

    private fun timeAttackGameOver() {
        if (countdownTimer.gameOver) {
            timer.stop()
            countdownTimer.stop()
            newGame.isEnabled = true
            countdownTimer.displayTimeOut()
            countdownTimer.reset()
        }
    }

    private fun gameOver() {
        if (board.gameOver) {
            newGame.isEnabled = true
            timer.stop()
            countdownTimer.stop()
            countdownTimer.reset()
            info.hideTimerPanel()
            JOptionPane.showMessageDialog(frame,
                    "Game Over!")

        }
    }

    private fun createMenu(): JMenuBar? {
        val menu = JMenuBar()
        val file = JMenu("File")
        val mode = JMenu("Mode")
        val about = JMenu("About")

        val endGame = JMenuItem("End Game")
        val aboutLevels = JMenuItem("Levels and Score")
        timeAttack = JCheckBoxMenuItem("Time Attack!")
        frozenColor = JCheckBoxMenuItem("Disable Frozen Colors")
        timeAttackMode()
        newGame.isEnabled = false

        aboutLevels.addActionListener {
            JOptionPane.showMessageDialog(null,
                    buildInstructions())
        }

        frozenColor!!.addActionListener({ gameBoard.setColorMode(frozenColor!!.isSelected) })

        timeAttack!!.addActionListener({
            if (timeAttack!!.isSelected) {
                timer.stop()
                newGame.isEnabled = true
                endGame.isEnabled = false
                JOptionPane.showMessageDialog(null,
                        "Enable - Please restart your game!")
            } else {

                newGame.isEnabled = true
                endGame.isEnabled = false
                JOptionPane.showMessageDialog(null, "Disable - Please restart your game!")
            }
            timeAttackMode()
        })

        newGame.addActionListener({

            board.newGame(GAME_WIDTH, GAME_HEIGHT, null)
            newGame.isEnabled = false
            endGame.isEnabled = true
            info.newGame()
            if (timeAttack!!.isSelected) {
                countdownTimer.start()
                countdownTimer.restart()
            }
            countdownTimer.reset()
            timer.restart()

        })

        endGame.addActionListener({
            timer.stop()
            countdownTimer.stop()
            newGame.isEnabled = true
            JOptionPane.showMessageDialog(null,
                    "You ended the game!")
        })

        about.add(aboutLevels)
        mode.add(timeAttack)
        mode.add(frozenColor)
        file.add(newGame)
        file.add(endGame)
        menu.add(file)
        menu.add(mode)
        menu.add(about)
        return menu
    }

    private fun timeAttackMode() {
        if (timeAttack!!.isSelected) {
            countdownTimer.setGameover(false)
            info.showTimerPanel()

        } else {
            countdownTimer.setGameover(true)
            info.hideTimerPanel()
            countdownTimer.reset()

        }
    }

    private fun buildInstructions(): String {
        val sb = StringBuilder()
        sb.append("Score: 10 points per line cleared ")
        sb.append("additional points can be made for ")
        sb.append("clearing multiple lines: \n")
        sb.append("  -2 lines is an aditional 20 points \n")
        sb.append("  -3 lines is an aditional 30 points \n")
        sb.append("  -4 lines is an aditional 40 points \n")
        sb.append("Level: The level increases every time you clear 5 lines.\n" + "The timer is also reduced by 100 milliseconds for every level cleared.")
        return sb.toString()
    }


    private fun createPanel(): JPanel {
        panelView = JPanel()
        panelView!!.background = Color.GREEN
        panelView!!.add(gameBoard)
        gameBoard.isFocusable = true
        gameBoard.requestFocusInWindow()

        return panelView as JPanel
    }

    override fun propertyChange(the_evt: PropertyChangeEvent) {
        if (timerStep > 0 && the_evt.newValue != the_evt.oldValue) {
            timerStep -= DIFFICULTY_INCREASE
            timer.delay = timerStep
        }
    }

}





