package br.com.tamandua.crawler.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import net.htmlparser.jericho.MasonTagTypes;
import net.htmlparser.jericho.MicrosoftTagTypes;
import net.htmlparser.jericho.PHPTagTypes;
import net.htmlparser.jericho.Source;

/**
 * @author Everton Rosario (erosario@gmail.com)
 *
 */
public abstract class AbstractParser {
    /* Bloco static com os registros do Jericho */
    static {
        MicrosoftTagTypes.register();
        PHPTagTypes.register();
        PHPTagTypes.PHP_SHORT.deregister(); // remove PHP short tags otherwise they override processing instructions
        MasonTagTypes.register();
    }
    
    private Source source;

    public AbstractParser(InputStream is) throws IOException {
        super();
        this.source = new Source(is);
    }

    public AbstractParser(Reader r) throws IOException {
        super();
        this.source = new Source(r);
    }

    public AbstractParser(String htmlBody) throws IOException {
        super();
        this.source = new Source(new StringReader(htmlBody));
    }
    
    public AbstractParser(Source source) {
        super();
        this.source = source;
    }

    /**
     * @return the source
     */
    public Source getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(Source source) {
        this.source = source;
    }
    
    

}
