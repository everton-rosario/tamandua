/**
 * 
 */
package br.com.tamandua.crawler.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Class Artist
 *
 * @author Everton Rosario (erosario@gmail.com)
 */
public class Artist {
    private String name;
    private String letter;
    private String id;
    private String url;

    
    public Artist() {
        this(null, null);
    }
    
    /**
     * @param name
     * @param letter
     */
    public Artist(String name, String letter) {
        this(name, letter, null);
    }

    /**
     * @param name
     * @param letter
     * @param url
     */
    public Artist(String name, String letter, String url) {
        super();
        this.name = name;
        this.letter = letter;
        this.url = url;
    }

    public String getKey() {
        return name + "-"+ letter;
    }
    /** @return Obtem o campo name do Artist. */
    @XmlElement
    public String getName() {
        return name;
    }
    /** @param name Ajusta o campo name do Artist. */
    public void setName(String name) {
        this.name = name;
    }
    /** @return Obtem o campo letter do Artist. */
    @XmlElement
    public String getLetter() {
        return letter;
    }
    /** @param letter Ajusta o campo letter do Artist. */
    public void setLetter(String letter) {
        this.letter = letter;
    }
    /** @return Obtem o campo id do Artist. */
    @XmlTransient
    public String getId() {
        return id;
    }
    /** @param id Ajusta o campo id do Artist. */
    public void setId(String id) {
        this.id = id;
    }
    /** @return Obtem o campo url do Artist. */
    @XmlElement
    public String getUrl() {
        return url;
    }
    /** @param url Ajusta o campo url do Artist. */
    public void setUrl(String url) {
        this.url = url;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Artist [");
        if (id != null) {
            builder.append("id=");
            builder.append(id);
            builder.append(", ");
        }
        if (url != null) {
            builder.append("url=");
            builder.append(url);
            builder.append(", ");
        }
        if (name != null) {
            builder.append("name=");
            builder.append(name);
            builder.append(", ");
        }
        if (letter != null) {
            builder.append("letter=");
            builder.append(letter);
        }
        builder.append("]");
        return builder.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((letter == null) ? 0 : letter.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Artist other = (Artist) obj;
        if (letter == null) {
            if (other.letter != null)
                return false;
        } else if (!letter.equals(other.letter))
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }
}
