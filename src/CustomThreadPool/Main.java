package CustomThreadPool;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        CountDownLatch latch = null;
        ThreadPool pool;
        List<Matrix> matrices;
        int inputNum;
        int matrixNum;
        inputNum = getThreadNumFromUser();
        pool = new ThreadPool(inputNum);
        while(true) {
            matrixNum = getMatrixNumFromUser();
            matrices = new LinkedList<>();
            inputNum = getMatrixDimFromUser();
            populateMatrixList(inputNum, matrixNum, matrices);
            latch = generateTasks(matrices, pool);
            try {
                latch.await();//waits for all tasks to complete
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            //matrices.forEach(matrix -> System.out.println(matrix.toString()));
        }
    }

    private static CountDownLatch generateTasks(List<Matrix> matrices, ThreadPool pool) {
        CountDownLatch latch;
        List<Pair<Matrix, Matrix>> taskPairs = matrices.stream()
                .flatMap(mat1 -> matrices.stream().map(mat2 -> new Pair<>(mat1, mat2))).collect(Collectors.toList());
        System.out.println("Excuting " + taskPairs.size() + " tasks...");
        latch = new CountDownLatch(taskPairs.size());
        pool.setLatch(latch);
        for(Pair<Matrix, Matrix> pair : taskPairs){
            MatMulTask task = new MatMulTask(pair.getKey(), pair.getValue(), latch);
            pool.execute(task);
        }
        return latch;
    }

    private static void populateMatrixList(int matrixDims, int matrixNum, List<Matrix> matrices) {
        IntStream.range(0, matrixNum).forEach(i -> {
            Matrix mat = new Matrix(matrixDims);
            mat.randomize();
            matrices.add(mat);
        });
    }

    private static int getMatrixDimFromUser(){
        int inputNum;
        Scanner scanner;
        while (true) {
            scanner = new Scanner(System.in);
            System.out.println("What dimensions should the matrices have?");
            try {
                inputNum = Integer.parseInt(scanner.nextLine());
                if (inputNum < 1)
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number over 0");
                continue;
            }
            return inputNum;
        }
    }


    private static int getMatrixNumFromUser() {
        int inputNum;
        Scanner scanner;
        while (true) {
            scanner = new Scanner(System.in);
            System.out.println("How many matrices would you like to generate?");
            try {
                inputNum = Integer.parseInt(scanner.nextLine());
                if (inputNum < 2)
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number over 1");
                continue;
            }
            return inputNum;
        }

    }

    private static int getThreadNumFromUser(){
        int inputNum;
        Scanner scanner;
        while(true) {
            scanner = new Scanner(System.in);
            System.out.println("How many threads would you like the pool to have?");
            try{
                inputNum = Integer.parseInt(scanner.nextLine());
                if(inputNum < 2 || inputNum > 20)
                    throw new NumberFormatException();
            }catch(NumberFormatException e){
                System.out.println("Please enter a number between 2 and 20");
                continue;
            }
            return inputNum;

    }

}}
