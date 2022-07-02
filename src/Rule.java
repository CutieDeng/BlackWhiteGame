import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface Rule {
    // Value 1 means black, but -1 means white, 0 default.
    int getState(int row, int col);

    // Use this method to set the size, and then initialize it.
    // It's invalid to set it after init.
    void setSize(int size);

    // Initialize the game board.
    void init();

    void drop();

    // Attempt to set the position your flag.
    // Returns false when failure.
    boolean setPoi(int row, int col, boolean isBlack);

    boolean attemptPoi(int row, int col, boolean isBlack);

    void forcePoi(int row, int col, boolean isBlack);

    void register(Function<Boolean, BiConsumer<Integer, Integer>> f);
}
