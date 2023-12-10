package brickGame;

import java.io.Serializable;

/**
 * The {@code BlockSerializable} class represents a serializable version of a block in a brick-breaking game.
 * It is used for serializing block information, including row, column (j), and type.
 *
 * @author Chai Ze Xuan
 */
public class BlockSerializable implements Serializable {

    /**
     * The row index of the block in the game grid.
     */
    public final int row;

    /**
     * The column index (j) of the block in the game grid.
     */
    public final int j;

    /**
     * The type of the block, determining its appearance and behavior.
     */
    public final int type;

    /**
     * Constructs a new BlockSerializable object with the specified row, column (j), and type.
     *
     * @param row The row index of the block in the game grid.
     * @param j The column index (j) of the block in the game grid.
     * @param type The type of the block, determining its appearance and behavior.
     */
    public BlockSerializable(int row , int j , int type) {
        this.row = row;
        this.j = j;
        this.type = type;
    }
}
