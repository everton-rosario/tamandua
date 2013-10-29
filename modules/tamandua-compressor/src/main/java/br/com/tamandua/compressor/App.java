package br.com.tamandua.compressor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yahoo.platform.yui.compressor.Bootstrap;

public class App {
	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
	// Opcoes existentes para a aplicacao
	private static Options options = getOptions();

	/**
	 * @param args
	 * @throws Exception
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException, Exception {
		// interpretador de comandos
		CommandLineParser parser = new PosixParser();

		try {
			// Realiza o parse da linha de comando
			CommandLine line = parser.parse(options, args);
			List<String> minFilePaths = new ArrayList<String>();

			// Verifica se nao existe alguma inconsistencia com a chamada de
			// prompt de comando
			if (!isValidLine(line) || line.hasOption("help")) {
				showUsage();
				return;
			}

			// Minimiza os arquivos
			if (line.hasOption("minimize")) {
				String[] fileNames = line.getOptionValues("minimize");
				minFilePaths = minimize(fileNames);
			}

			// Combina os arquivos minimizados
			if (line.hasOption("combine")) {
				String combinedFilepath = line.getOptionValue("combine");
				combine(minFilePaths, combinedFilepath);
				remove(minFilePaths);
			}

		} catch (ParseException exp) {
			showUsage();
		}
	}

	public static List<String> minimize(String[] filePaths) throws Exception {
		System.out.println("Minimizando os arquivos...");
		List<String> minFilePaths = new ArrayList<String>();
		for (String filePath : filePaths) {
			System.out.println("Minimizando o arquivo: " + filePath);
			String[] fileParts = filePath.split("\\.");
			StringBuilder minFilePath = new StringBuilder();
			for(int i=0; i<fileParts.length-1; i++){
				minFilePath.append(fileParts[i]);
			}
			String fileExtension = fileParts[fileParts.length - 1];			
			minFilePath.append(".min." + fileExtension);
			minFilePaths.add(minFilePath.toString());
			String[] params = new String[] { filePath, "-o", minFilePath.toString() };
			Bootstrap.main(params);
		}
		System.out.println("Arquivos minimizados com sucesso!");
		return minFilePaths;
	}

	public static void combine(List<String> minFilePaths,
			String combinedFilepath) throws Exception {
		System.out.println("Lendo os arquivos minimizados...");
		StringBuilder sb = new StringBuilder("");
		for (String minFilePath : minFilePaths) {
			System.out.println("Lendo o arquivo minimizado: " + minFilePath);
			File file = new File(minFilePath);
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
		}
		System.out.println("Leitura dos arquivos minimizados feita com sucesso!");

		System.out.println("Combinando os arquivos minimizados...");
		File file = new File(combinedFilepath);
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write(sb.toString());
		out.close();
		System.out.println("Arquivos minimizados combinados com sucesso!");
	}

	public static void remove(List<String> minFilePaths) {
		System.out.println("Apagando os arquivos minimizados...");
		for (String minFilePath : minFilePaths) {
			System.out.println("Apagando o arquivo minimizado: " + minFilePath);
			File minFile = new File(minFilePath);
			minFile.delete();
		}
		System.out.println("Arquivos minimizados apagados com sucesso!");
	}

	/**
	 * Verificacao se os parametros informados sao validos individualmente
	 * utilizando obrigatoriamente o "PROVIDER"
	 * 
	 * @param line
	 *            Linha parseada do commons-cli
	 * @return Se a linha esta valida (true) ou invalida(false)
	 */
	private static boolean isValidLine(CommandLine line) {
		if (line.hasOption("help") || line.hasOption("minimize")
				|| line.hasOption("combine") || line.hasOption("remove")) {

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Mostra como deve ser utilizado a aplicacao.
	 */
	private static void showUsage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("tamandua", options);
	}

	/**
	 * @return opcoes utilizadas pelo sistema
	 */
	private static Options getOptions() {
		// create the Options
		Options options = new Options();

		Option help = new Option("help", "print this message");
		Option minimize = OptionBuilder
				.withLongOpt("minimize")
				.withDescription(
						"Minimiza um arquivo removendo os espacos e as linhas.")
				.hasArgs().create();
		Option combine = OptionBuilder.withLongOpt("combine").withDescription(
				"Combina arquivos minimizados em um unico arquivo.").hasArg()
				.create();
		Option remove = OptionBuilder.withLongOpt("remove").withDescription(
				"Remove os arquivos minimizados.").create();

		// Faz o bind das opcoes criadas ao conjunto de instrucoes aceitas
		options.addOption(help);
		options.addOption(minimize);
		options.addOption(combine);
		options.addOption(remove);

		return options;
	}
}
