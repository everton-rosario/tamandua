package br.com.tamandua.crawler.domain.xml.vagalume;


/**
 * @author Everton Rosario (erosario@gmail.com)
 *
 */
public class TestHtmlParser {
//    public static void main(String[] args) throws Exception {
//
//        // FAZ A CARGA DO ARQUIVO PARA TESTES
//        TrackList trackList = new TrackList();
//        JAXBContext jc = JAXBContext.newInstance(TrackList.class);
//        Unmarshaller unmarshaller = jc.createUnmarshaller();
//        trackList = (TrackList) unmarshaller.unmarshal(new File("c:/crawled/index/d/djavan.xml"));
//        System.out.println("Total de faixas de DJAVAN: " + trackList.getTotalTracks());
//        Track track = trackList.getTracks().get(0);
//        System.out.println("Track sendo parseada: " + track.getName());
//        TrackPageParser trackParser = new TrackPageParser(track);
//        
//        Parser parser = new Parser();
//        parser.setInputHTML(trackParser.getHtmlBody());
//        NodeIterator iterator = parser.elements();
//        printContents(iterator);
//  }
//
//    private static void printContents(NodeIterator iterator) throws ParserException {
//        while (iterator.hasMoreNodes()) {
//            Node node = iterator.nextNode();
//            System.out.println("---------------------------------------");
//            System.out.println(node.getText());
//            if (node.getChildren() != null) {
//                printContents(node.getChildren().elements());
//            }
//        }
//    }

}
