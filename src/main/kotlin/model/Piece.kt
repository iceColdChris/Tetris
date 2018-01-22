package model


interface Piece {

    fun moveLeft()
    fun moveRight()
    fun moveDown()
    fun rotate()
    fun getX() : Int
    fun getY() : Int
}