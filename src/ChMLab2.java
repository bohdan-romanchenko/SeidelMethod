import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class ChMLab2 {
    public static void main(String[] args) {
        // Для считывания воспользуемся классом Scanner
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Для вывода - классом PrintWriter
        PrintWriter printWriter = new PrintWriter(System.out);

        // Считываем размер вводимой матрицы
        int size;
        assert scanner != null;
        size = scanner.nextInt();

        // Будем хранить матрицу в векторе, состоящем из
        // векторов вещественных чисел
        Double[][] matrix = new Double[size][size + 1];

        // Матрица будет иметь размер (size) x (size + 1),
        // c учетом столбца свободных членов        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size + 1; j++) {
                matrix[i][j] = scanner.nextDouble();
            }
        }

        // Считываем необходимую точность решения
        double eps;
        eps = scanner.nextDouble();

        // Введем вектор значений неизвестных на предыдущей итерации,
        // размер которого равен числу строк в матрице, т.е. size,
        // причем согласно методу изначально заполняем его нулями
        Double[] previousVariableValues = new Double[size];
        for (int i = 0; i < size; i++) {
            previousVariableValues[i] = 0.0;
        }

        // Будем выполнять итерационный процесс до тех пор,
        // пока не будет достигнута необходимая точность
        while (true) {
            // Введем вектор значений неизвестных на текущем шаге
            Double[] currentVariableValues = new Double[size];

            // Посчитаем значения неизвестных на текущей итерации
            // в соответствии с теоретическими формулами
            for (int i = 0; i < size; i++) {
                // Инициализируем i-ую неизвестную значением
                // свободного члена i-ой строки матрицы
                currentVariableValues[i] = matrix[i][size];

                // Вычитаем сумму по всем отличным от i-ой неизвестным
                for (int j = 0; j < size; j++) {
                    // При j < i можем использовать уже посчитанные
                    // на этой итерации значения неизвестных
                    if (j < i) {
                        currentVariableValues[i] -= matrix[i][j] * currentVariableValues[j];
                    }

                    // При j > i используем значения с прошлой итерации
                    if (j > i) {
                        currentVariableValues[i] -= matrix[i][j] * previousVariableValues[j];
                    }
                }

                // Делим на коэффициент при i-ой неизвестной
                currentVariableValues[i] /= matrix[i][i];
            }

            // Посчитаем текущую погрешность относительно предыдущей итерации
            double error = 0.0;

            for (int i = 0; i < size; i++) {
                error += Math.abs(currentVariableValues[i] - previousVariableValues[i]);
            }

            // Если необходимая точность достигнута, то завершаем процесс
            if (error < eps) {
                break;
            }

            // Переходим к следующей итерации, так
            // что текущие значения неизвестных
            // становятся значениями на предыдущей итерации
            previousVariableValues = currentVariableValues;
        }

        // Выводим найденные значения неизвестных
        for (int i = 0; i < size; i++) {
            printWriter.print(previousVariableValues[i] + " ");
        }

        // После выполнения программы необходимо закрыть
        // потоки ввода и вывода
        System.out.println(Arrays.toString(getResidual(previousVariableValues, matrix, size)));
        scanner.close();
        printWriter.close();
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