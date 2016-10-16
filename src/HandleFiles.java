import java.util.*;
import java.io.*;

class HandleFiles {

    static Scanner openFile(String path) {
        Scanner result = null;
        try {
            result = new Scanner(new File(path));
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Error while opening " + path);
        }
        return result;
    }

    static String[] filesAt(String dir) {
        return new File(dir).list();
    }

    static void writeToFile(String fullPath, String[] lines) {
        try {
            Formatter output = new Formatter(fullPath + ".txt");
            for (String line : lines) {
                output.format("%s\n", line);
            }
            output.close();
        } catch (FileNotFoundException ignored) {
        }
    }

    static void writeToFile(String fullPath, int col1[], double col2[]) {
        try {
            Formatter output = new Formatter(fullPath + ".txt");
            for (int i = 0; i < col1.length; i++) {
                output.format(Locale.ENGLISH, "%.3f\t%d\n", col2[i], col1[i]);
            }
            output.close();
        } catch (FileNotFoundException ignored) {
        }
    }

    static void writeToFile(String fullPath, double col1[], double col2[]) {
        Sort(col1, col2);
        try {
            Formatter output = new Formatter(fullPath + ".txt");
            for (int i = 0; i < col1.length; i++) {
                output.format(Locale.ENGLISH, "%.3f\t%.3f\n", col1[i], col2[i]);
            }
            output.close();
        } catch (FileNotFoundException ignored) {
        }
    }

    private static void Sort(double[] mainColumn, double[] secondaryColumn) {
        for (int i = 0; i < mainColumn.length - 1; i++) {
            for (int j = 0; j < mainColumn.length - 1 - i; j++) {
                if (mainColumn[j] > mainColumn[j + 1]) {
                    double mainValue = mainColumn[j];
                    double secondaryValue = secondaryColumn[j];
                    mainColumn[j] = mainColumn[j + 1];
                    secondaryColumn[j] = secondaryColumn[j + 1];
                    mainColumn[j + 1] = mainValue;
                    secondaryColumn[j + 1] = secondaryValue;
                }
            }
        }
    }
}
