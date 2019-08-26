package CustomThreadPool;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * A simple matrix class.
 */
public class Matrix {
    private int size;
    private int mat[][];


    public Matrix(int size){
        this.size = size;
        mat = new int[size][size];
    }

    /*
    Randomize this matrix values
     */
    public void randomize(){
        Random random = new Random();
        IntStream.range(0, size)
                .forEach(i -> mat[i] = random.ints(size, 0, 11).toArray());
    }

    /*
    Calculate the product of this matrix with matB
     */
    public Matrix matMul(Matrix matB){
        Matrix ans = new Matrix(mat.length);
        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++)
                for(int k = 0; k < size; k++)
                    ans.mat[i][j] += this.mat[i][k]* matB.mat[k][j];


        return ans;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int[] x: mat)
            builder.append(Arrays.toString(x) + System.lineSeparator());
        return builder.toString();
    }
}
