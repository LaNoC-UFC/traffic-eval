import java.util.*;
import java.io.*;

public class HandleFiles {

	// Abertura de arquivos para leitura
	public static Scanner OpenFilestoRead(String path) {

		Scanner input = null;

		try {
			input = new Scanner(new File(path));
		} catch (FileNotFoundException fileNotFoundException) {
			System.out.println("Erro na abertura do arquivo");
		}
		return input;

	}

	// Lista arquivos de um diretorio
	public static String[] getFilepaths(String path) {

		File list = new File(path);
		String[] files = list.list(); // Vetor de strings com a lista e arquivos

		return files;
	}

	public static void WriteFile(String path, String FileName, String[] datas) {
		Formatter output;
		try {
			output = new Formatter(path + FileName + ".txt"); // path + Nome
																// desejado +
																// formato
			for (int a = 0; a < datas.length; a++)
				output.format("%s \r\n", datas[a]);
			output.close();
		} catch (FileNotFoundException filesNotFoundException) {
		}
	}

	public static void WriteFile(String path, String FileName, int col1[],
			double col2[], int dim) {
		Formatter output;
		try {
			//Sort(col1, col2, dim);
			output = new Formatter(path + FileName + ".txt"); // path + Nome
																// desejado +
																// formato
			for (int a = 0; a < dim; a++)
				output.format(Locale.ENGLISH, "%.3f\t%d\r\n", col2[a], col1[a]);
			output.close();
		} catch (FileNotFoundException filesNotFoundException) {
		}
	}

	public static void WriteFile(String path, String FileName, double col1[],
			double col2[], int dim) {
		Formatter output;
		try {
			Sort(col1, col2, dim);
			output = new Formatter(path + FileName + ".txt"); // path + Nome
																// desejado +
																// formato
			for (int a = 0; a < dim; a++)
				output.format(Locale.ENGLISH, "%.3f\t%.3f\r\n", col1[a],
						col2[a]);
			output.close();
		} catch (FileNotFoundException filesNotFoundException) {
		}
	}

	// Bubble Sort
	private static void Sort(int[] MainColum, double[] SecColum, int length) {

		for (int i = 0; i < length - 1; i++) {
			for (int j = 0; j < length - 1 - i; j++) {
				if (MainColum[j] > MainColum[j + 1]) {
					int aux = MainColum[j];
					double aux1 = SecColum[j];
					MainColum[j] = MainColum[j + 1];
					SecColum[j] = SecColum[j + 1];
					MainColum[j + 1] = aux;
					SecColum[j + 1] = aux1;
				}
			}
		}

	}

	// Bubble Sort
	private static void Sort(double[] MainColum, double[] SecColum, int length) {

		for (int i = 0; i < length - 1; i++) {
			for (int j = 0; j < length - 1 - i; j++) {
				if (MainColum[j] > MainColum[j + 1]) {
					double aux = MainColum[j];
					double aux1 = SecColum[j];
					MainColum[j] = MainColum[j + 1];
					SecColum[j] = SecColum[j + 1];
					MainColum[j + 1] = aux;
					SecColum[j + 1] = aux1;
				}
			}
		}

	}

}
