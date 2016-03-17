import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Math.abs;

public class ChMLab2 {
    public static void main(String[] args) {
        Scanner readingFromFile = null;
        try {
            readingFromFile = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        PrintWriter writeToFile = new PrintWriter(System.out);

        assert readingFromFile != null;
        int countOfRows = readingFromFile.nextInt();

        Double[][] inArray;
        inArray = new Double[countOfRows][countOfRows + 1];

        for (int i = 0; i < countOfRows; i++)
            for (int j = 0; j < countOfRows + 1; j++)
                inArray[i][j] = readingFromFile.nextDouble();

        double allowedError;
        allowedError = readingFromFile.nextDouble();

        Double[] answersVectorPrev;
        answersVectorPrev = new Double[countOfRows];
        for (int i = 0; countOfRows > i; i++)
            answersVectorPrev[i] = 0.0;

        while (true) {
            Double[] answersVectorCurr = new Double[countOfRows];
            for (int i = 0; i < countOfRows; i++) {
                answersVectorCurr[i] = inArray[i][countOfRows];

                for (int j = 0; j < countOfRows; j++) {
                    if (j < i)
                        answersVectorCurr[i] = answersVectorCurr[i] - (inArray[i][j] * answersVectorCurr[j]);
                    if (j > i)
                        answersVectorCurr[i] = answersVectorCurr[i] - (inArray[i][j] * answersVectorPrev[j]);
                }
                answersVectorCurr[i] = answersVectorCurr[i] / inArray[i][i];
            }

            double error = 0.0;

            for (int i = 0; i < countOfRows; i++)
                error = error + abs(answersVectorCurr[i] - answersVectorPrev[i]);

            if (allowedError <= error) answersVectorPrev = answersVectorCurr;
            else break;
        }

        for (int i = 0; i < countOfRows; i++)
            writeToFile.print(String.format("%s ", answersVectorPrev[i]));

        System.out.println(Arrays.toString(getResidual(answersVectorPrev, inArray, countOfRows)));
        readingFromFile.close();
        writeToFile.close();
    }

    public static Double[] getResidual(Double[] x, Double[][] inArray, int size){
        Double[] residual = new Double[size];
        Double[] AX = new Double[size];
        for (int i = 0; i < size; i++){
            AX[i] = 0d;
            for (int j = 0; j < size; j++)
                AX[i] += inArray[i][j] * x[j];
            residual[i] = inArray[i][size] - AX[i];
        }
        System.out.println();
        return residual;
    }
}