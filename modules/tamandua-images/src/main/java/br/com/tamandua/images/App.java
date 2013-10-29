package br.com.tamandua.images;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import br.com.tamandua.images.service.ImagesService;

public class App {
	
    // Opcoes existentes para a aplicacao
    private static Options options = getOptions();
	
    public static void main( String[] args ) throws Exception {
        // interpretador de comandos
        CommandLineParser parser = new PosixParser();
        
        try{
        	CommandLine line = parser.parse(options, args);
        	
            // Verifica se nao existe alguma inconsistencia com a chamada de prompt de comando
            if (!isValidLine(line) || line.hasOption("help")) {
                help();
                return;
            }
            
            if(line.hasOption("update-images-flag")){
            	ImagesService.updateImagesNotFound();
            }
            
	    } catch (ParseException e) {
			help();
		} catch (NumberFormatException e) {
			help();
		}
    }
    
    /**
     * Verificacao se os parametros informados sao validos.
     * @param line Linha parseada do commons-cli
     * @return se a linha esta valida (true) ou invalida(false)
     */
    private static boolean isValidLine(CommandLine line) {
        if ((line.hasOption("help") || 
             line.hasOption("update-images-flag") )) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Mostra como deve ser utilizado a aplicacao.
     */
    private static void help() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("tamandua-images", options);
    }
    
    /**
     * Obtem as opcoes utilizadas pelo sistema.
     * @return opcoes utilizadas pelo sistema 
     */
    private static Options getOptions() {
        // create the Options
        Options options = new Options();
        
        Option help = new Option("help", "Exibe as informacoes de ajuda.");
        Option update_images_flag = OptionBuilder.withLongOpt("update-images-flag")
													 .withDescription("Atualiza na base as images que nao foram encontradas no S3.")
													 .create();
        
        options.addOption(help);
        options.addOption(update_images_flag);
        
        return options;
    }
}
