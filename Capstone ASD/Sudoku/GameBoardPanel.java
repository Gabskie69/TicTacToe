package Sudoku;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class GameBoardPanel extends JPanel{
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // Define named constants for UI sizes
    public static final int CELL_SIZE = 60;   // Sudoku.Cell width/height in pixels
    public static final int BOARD_WIDTH  = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;
    // Board width/height in pixels

    private long startTime;
    private long endTime;
    // Define properties
    /** The game board composes of 9x9 Cells (customized JTextFields) */
    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    /** It also contains a Sudoku.Puzzle with array numbers and isGiven */
    public Puzzle puzzle = new Puzzle();

    /** Constructor */
    public GameBoardPanel() {
        super.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));  // JPanel

        // Allocate the 2D array of Sudoku.Cell, and added into JPanel.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);
                super.add(cells[row][col]);   // JPanel
            }
        }

        // [TODO 3] Allocate a common listener as the ActionEvent listener for all the
        //  Cells (JTextFields)
        CellInputListener listener = new CellInputListener();

        // [TODO 4] Adds this common listener to all editable cells
        for (int row = 0; row < SudokuConstants.GRID_SIZE; row ++) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; col ++) {
                if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener);   // For all editable rows and cols
                }
            }
        }

        super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }

    /**
     * Generate a new puzzle; and reset the game board of cells based on the puzzle.
     * You can call this method to start a new game.
     */
    public void newGame() {
        // Misalnya, kita ingin 30 sel kosong
        int cellsToGuess = 13;
        puzzle.newPuzzle(cellsToGuess);
        startTime = System.currentTimeMillis(); // Catat waktu mulai
        repaint();

        // Reset semua 9x9 sel berdasarkan puzzle baru
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
            }
        }
    }

    /**
     * Return true if the puzzle is solved
     * i.e., none of the cell have status of TO_GUESS or WRONG_GUESS
     */
    public boolean isSolved() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
//                    File file = new File("open-new-level-143027.wav");
//                    AudioInputStream audioStream = null;
//                    try {
//                        audioStream = AudioSystem.getAudioInputStream(file);
//                    } catch (UnsupportedAudioFileException e) {
//                        throw new RuntimeException(e);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    Clip clip = null;
//                    try {
//                        clip = AudioSystem.getClip();
//                    } catch (LineUnavailableException e) {
//                        throw new RuntimeException(e);
//                    }
//                    clip.open(audioStream);
//                    clip.start();
                    return false;
                }
            }
        }
        return true;
    }

    // [TODO 2] Define a Listener Inner Class for all the editable Cells
    private class CellInputListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Get a reference of the JTextField that triggers this action event
            Cell sourceCell = (Cell)e.getSource();

            // Retrieve the int entered
            int numberIn = Integer.parseInt(sourceCell.getText());
            // For debugging
            System.out.println("You entered " + numberIn);

            /*
             * [TODO 5] (later - after TODO 3 and 4)
             * Check the numberIn against sourceCell.number.
             * Update the cell status sourceCell.status,
             * and re-paint the cell via sourceCell.paint().
             */
            if (numberIn == sourceCell.number) {
               sourceCell.status = CellStatus.CORRECT_GUESS;
            } else {
               sourceCell.paint(Color.RED);
            }// re-paint this cell based on its status

            /*
             * [TODO 6] (later)
             * Check if the player has solved the puzzle after this move,
             *   by calling isSolved(). Put up a congratulation JOptionPane, if so.
             */

            if (isSolved()) {
                endTime = System.currentTimeMillis(); // Catat waktu selesai
                long elapsedTime = endTime - startTime; // Hitung waktu yang berlalu (dalam ms)

                // Konversi ke format menit:detik
                long seconds = (elapsedTime / 1000) % 60;
                long minutes = (elapsedTime / 1000) / 60;

                ImageIcon iconWin = new ImageIcon("C:\\Users\\Aryabima\\CapstoneASD-C-Kelompok11-\\Capstone ASD\\Sudoku\\iconwin.png");
                int option = JOptionPane.showConfirmDialog(
                        GameBoardPanel.this,
                        String.format(
                                "Congratulations! You've solved the puzzle in %d minutes and %d seconds!\nWould you like to start a new game?",
                                minutes, seconds
                        ),
                        "Sudoku Puzzle Solved",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        iconWin
                );

                // Jika pemain memilih "Yes", mulai game baru
                if (option == JOptionPane.YES_OPTION) {
                    newGame(); // Restart the game
                }
            }
        }
    }
}