package model

import javafx.scene.paint.Color

enum class Block(val letter : String, val color : Color) {
    EMPTY(" ", Color.BLACK),
    I("I", Color.CYAN),
    J("J", Color.BLUE),
    L("L", Color.ORANGE),
    O("O", Color.YELLOW),
    S("S", Color.GREEN),
    T("T", Color.MAGENTA),
    Z("Z", Color.RED);

}