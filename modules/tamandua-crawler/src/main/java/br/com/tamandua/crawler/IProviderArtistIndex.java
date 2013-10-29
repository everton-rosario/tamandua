package br.com.tamandua.crawler;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import br.com.tamandua.crawler.domain.ArtistList;
import br.com.tamandua.crawler.domain.TrackList;
import br.com.tamandua.crawler.parser.TrackReport;

public interface IProviderArtistIndex {

    /**
     * Obtem todas as letras do provider
     * @return
     */
    public String getAllLetters();

    /**
     * Obtem os artistas existentes em uma letra
     * @param letter letra sendo carregada
     * @return
     * @throws IOException 
     */
    public ArtistList crawlLetter(String letter, CrawlerHTTP http) throws IOException;

    /**
     * Busca todos os artistas existentes nas letras indicadas na web
     * @param indicatedLettersToCrawl Letras onde serao buscados artistas na web.
     * @return
     * @throws JAXBException
     * @throws IOException 
     */
    public int crawlAllArtistLetters(String indicatedLettersToCrawl, CrawlerHTTP http) throws JAXBException, IOException;

    /**
     * Faz a sumarizacao das letras, gerando o arquivo "full.xml"
     * @param type O tipo da sumarizacao que vai ser carregada valores possiveis: TrackList.TYPE_DEFAULT, TrackList.TYPE_BKP, TrackList.TYPE_MERGE, TrackList.TYPE_OLD 
     * @return
     * @throws JAXBException
     */
    public ArtistList summarizeLetters(String type) throws JAXBException;
    
    /**
     * Servico para carregar do disco os arquivos previamente crawleados/summarizados.
     * @param lettersToUse Indica as letras a serem utilizadas. Caso o conteudo seja muito grande, e melhor carregar o "default" informando null no letters to use.
     * @return Lista de artistas summarizada
     * @throws JAXBException
     */
    public ArtistList loadSummarizedLetters(String lettersToUse, String type) throws JAXBException;

    /**
     * Realiza uma busca do indice das musicas de um determinado arista/lista de artistas
     * @param artistList Lista dos artistas, dos quais ser�o buscadas as musicas.
     * @throws JAXBException
     * @throws InterruptedException 
     * @throws IOException 
     */
    public void crawlTracksIndexFromArtists(ArtistList artistList) throws JAXBException, IOException, InterruptedException;

    /**
     * Realiza a contagem das musicas dos artistas informados em multithread.
     * @see TrackCrawlerProcessController
     * @param fullArtistList Lista dos artistas a serem crawleadas o conteudo das musicas.
     * @return Quantia de musicas crawleadas
     * @throws JAXBException
     * @throws InterruptedException
     */
    public TrackReport countTracks(ArtistList fullArtistList, int numberThreads, String type) throws JAXBException, InterruptedException;

    /**
     * Realiza o crawl do conteudo das musicas dos artistas informados em multithread.
     * @see TrackCrawlerProcessController
     * @param fullArtistList Lista dos artistas a serem crawleadas o conteudo das musicas.
     * @return Quantia de musicas crawleadas
     * @throws JAXBException
     * @throws InterruptedException
     */
    public TrackReport crawlTrackContents(ArtistList fullArtistList, int threads, String type) throws JAXBException, InterruptedException;
}