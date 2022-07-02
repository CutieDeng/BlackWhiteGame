import kotlin.ranges.IntRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SimpleRule implements Rule{

    private List<Integer> indexs;
    private List<Function<Boolean, BiConsumer<Integer, Integer>>> registers = new ArrayList<>();

    private int size;

    @Override
    public int getState(int row, int col) {
        return indexs.get(row * size + col);
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void init() {
        assert size > 0;
//        indexs = new ArrayList<>(size * size);
        indexs = IntStream.range(0, size * size).map(i -> 0).boxed().collect(Collectors.toList());
    }

    @Override
    public void drop() {
        indexs = null;
        registers.clear();
    }

    private final static int[][] MOVE = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    @Override
    public boolean setPoi(int row, int col, boolean isBlack) {
        assert row >= 0 && row < size;
        assert col >= 0 && col < size;

        if (indexs.get(row * size + col) != 0)
            return false;

        List<int[]> reduce = Arrays.stream(MOVE).map(v -> {
            assert v.length == 2;
            int r = row, c = col;
            List<int[]> ans = new ArrayList<>();
            while (true) {
                r += v[0];
                c += v[1];
                if (r < 0 || r >= size)
                    break;
                if (c < 0 || c >= size)
                    break;
                int nowValue = indexs.get(r * size + c);
                if (nowValue == 0)
                    break;
                if (nowValue == 1 ^ isBlack) {
                    ans.add(new int[]{r, c});
                } else {
                    return ans;
                }
            }
            return new ArrayList<int[]>();
        }).reduce(new ArrayList<>(), (ints, ints2) -> {
            ints.addAll(ints2);
            return ints;
        });
        if (reduce.size() == 0) {
            return false;
        }
        reduce.add(new int[] {row, col});
        for (int[] p: reduce) {
            indexs.set(p[0] * size + p[1], isBlack ? 1 : -1);
            for (Function<Boolean, BiConsumer<Integer, Integer>> f: registers) {
                f.apply(isBlack).accept(p[0], p[1]);
            }
        }
//        indexs.set(row * size + col, isBlack ? 1 : -1);
        return true;
    }

    @Override
    public boolean attemptPoi(int row, int col, boolean isBlack) {
        assert row >= 0 && row < size;
        assert col >= 0 && col < size;

        if (indexs.get(row * size + col) != 0)
            return false;

        List<int[]> reduce = Arrays.stream(MOVE).map(v -> {
            assert v.length == 2;
            int r = row, c = col;
            List<int[]> ans = new ArrayList<>();
            while (true) {
                r += v[0];
                c += v[1];
                if (r < 0 || r >= size)
                    break;
                if (c < 0 || c >= size)
                    break;
                int nowValue = indexs.get(r * size + c);
                if (nowValue == 0)
                    break;
                if (nowValue == 1 ^ isBlack) {
                    ans.add(new int[]{r, c});
                } else {
                    return ans;
                }
            }
            return new ArrayList<int[]>();
        }).reduce(new ArrayList<>(), (ints, ints2) -> {
            ints.addAll(ints2);
            return ints;
        });
        if (reduce.size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public void forcePoi(int row, int col, boolean isBlack) {
        assert row >= 0 && row < size;
        assert col >= 0 && col < size;

        for (Function<Boolean, BiConsumer<Integer, Integer>> f: registers) {
            f.apply(isBlack).accept(row, col);
        }
        indexs.set(row * size + col, isBlack ? 1 : -1);
    }

    @Override
    public void register(Function<Boolean, BiConsumer<Integer, Integer>> f) {
        registers.add(f);
    }
}
