# Sliding-Block-Puzzles
Sliding Block Puzzles is a puzzle game similar to Rush Hour implemented with the Java Swing Library. The game consists of a number of pieces that fit into a confined area. The goal is to move one of the pieces, "the goal piece" labeled "Z", to a specific position. Usually, the goal can only be achieved by moving all of the other pieces in a certain specified order of moves. Each piece may be restricted in the direction that it can move (vertically or horizontally).

To play the game just import the project into Eclipse. Make sure the puzzle files are in the working directory.

Use this format to create your own puzzles:
*First line contains two integers - size of the grid
*Second line has the starting position of the goal piece
*Remaining lines contain other pieces

Each piece's starting position is given by 4 integer values and one
character value.
*The first integer will be the starting row position.
*The second integer will be the starting column position.
*The third integer will be the width in columns.
*The fourth integer will be the height in rows.
*The character value will specify the direction of movement the piece can have. This character can be either an "h" for horizontal movement (left or right), a "v" for vertical movement (up or down), a "b" for both horizontal and vertical movement, or a "n" for no movement.
