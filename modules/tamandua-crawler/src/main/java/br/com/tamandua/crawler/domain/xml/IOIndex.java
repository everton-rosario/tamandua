/**
 * 
 */
package br.com.tamandua.crawler.domain.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;

import br.com.tamandua.crawler.domain.Artist;
import br.com.tamandua.crawler.domain.ArtistList;
import br.com.tamandua.crawler.domain.TrackList;
import br.com.tamandua.service.vo.MusicVO;

/**
 * Class IOIndex
 *
 * @author Everton Rosario (erosario@gmail.com)
 */
public class IOIndex {
    private static final String INVALID_CHARS_REGEXP = "[\u0000-\\u0008\\u000b\\u000c\\u000e-\\u001f]";

    public static void writeLetter(ArtistList artistList, String type) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(ArtistList.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
        artistList.getAppropriateFile(type).getParentFile().mkdirs(); // Try to create dirs
        marshaller.marshal(artistList, artistList.getAppropriateFile(type));
    }

    public static void writeLetter(ArtistList artistList) throws JAXBException {
        writeLetter(artistList, ArtistList.TYPE_DEFAULT);
    }

    public static ArtistList readLetter(String letter, String provider, String type) throws JAXBException {
        ArtistList artistList = new ArtistList(letter);
        artistList.setProvider(provider);
        JAXBContext jc = JAXBContext.newInstance(ArtistList.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        artistList = (ArtistList) unmarshaller.unmarshal(artistList.getAppropriateFile(type));
        return artistList;
    }

    public static ArtistList readLetter(String letter, String provider) throws JAXBException {
        return readLetter(letter, provider, ArtistList.TYPE_DEFAULT);
    }

    public static void writeTrackIndexFromArtist(TrackList trackList, String type) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(TrackList.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
        trackList.getAppropriateFile(type).getParentFile().mkdirs(); // Cria os diretorios
        marshaller.marshal(trackList, trackList.getAppropriateFile(type));
	}
	
    public static void writeTrackIndexFromArtist(TrackList trackList) throws JAXBException {
        writeTrackIndexFromArtist(trackList, TrackList.TYPE_DEFAULT);
    }

    public static TrackList readTrackIndexFromArtist(Artist artist, String provider) throws JAXBException, IOException {
        return readTrackIndexFromArtist(artist, provider, TrackList.TYPE_DEFAULT);
    }

	    
    public static TrackList readTrackIndexFromArtist(Artist artist, String provider, String type) throws JAXBException {
		TrackList trackList = new TrackList();
		trackList.setArtist(artist);
		trackList.setProvider(provider);
		File file = trackList.getAppropriateFile(type);
		JAXBContext jc = JAXBContext.newInstance(TrackList.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		try {
		    System.gc();
			trackList = (TrackList) unmarshaller.unmarshal(file);
		} catch (Exception ex) {
	        String fileContent;
            try {
                fileContent = FileUtils.readFileToString(file, "UTF-8");
    	        fileContent = fileContent.replaceAll(INVALID_CHARS_REGEXP, "");
    	        trackList = (TrackList) unmarshaller.unmarshal(new StringReader(fileContent));
    	        fileContent = null;
    	        System.gc();
            } catch (IOException e) {
                System.err.println("arquivo nao encontrado antigo: "+ file );
            }
		} catch (OutOfMemoryError memoryError) {
		    System.gc();
		    trackList = new TrackList();
		    System.gc();
		    System.err.print("###################### MEMORY ERROR ###############");
		    System.err.print("File impossible to read: [" + file + "] size ["+file.length()+"]");
		}
        return trackList;
	}

	public static void main(String[] args) throws JAXBException, IOException {
		// FAZ UMA CARGA DE UM ARQUIVO E REMOVE OS CARACTERES ESPECIAIS
//		TrackList trackList = new TrackList();
//		JAXBContext jc = JAXBContext.newInstance(TrackList.class);
//        Unmarshaller unmarshaller = jc.createUnmarshaller();
//        String fileContent = FileUtils.readFileToString(new File("index/t/the-beatles.xml"), "UTF-8");
//        fileContent = fileContent.replaceAll("[\\u0008\\u0000\\u000c]", "");
//        System.out.println(trackList.getTotalTracks());
//        FileUtils.writeStringToFile(new File("index/t/the-beatles.xml"), fileContent, "UTF-8");
        

		// FAZ A CARGA DO ARQUIVO E CONTA AS MUSICAS
        TrackList trackList = new TrackList();
		JAXBContext jc = JAXBContext.newInstance(TrackList.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        trackList = (TrackList) unmarshaller.unmarshal(new File("index/d/djavan.xml"));
        System.out.println(trackList.getTotalTracks());
		
//		String toBeAltered = "\u0000E\u0008ver\u0008ton";
//		String altered = toBeAltered.replaceAll("[\\u0008\\u0000]", "");
//		System.out.println(altered);
        
	}

    public static void writeErrorMusic(MusicVO musicVO, File dirToErrors) throws JAXBException, IOException {
        JAXBContext jc = JAXBContext.newInstance(MusicVO.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
        dirToErrors.mkdirs(); // Cria os diretorios
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMHHmmssSSS");
        File musicVOFile = new File(dirToErrors.getCanonicalPath()+File.separatorChar+sdf.format(new Date())+".xml");
        marshaller.marshal(musicVO, musicVOFile);
    }
}
