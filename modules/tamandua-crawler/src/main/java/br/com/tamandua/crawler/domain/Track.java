/**
 * 
 */
package br.com.tamandua.crawler.domain;

import java.util.Collection;

/**
 * Class Track
 *
 * @author Everton Rosario (erosario@gmail.com)
 */
public class Track {
    private String id;
    private String name;
    /** @return Obtem o campo name do Track. */
    public String getName() {
        return name;
    }
    /** @param name Ajusta o campo name do Track. */
    public void setName(String name) {
        this.name = name;
    }
    private Collection<Artist> artists;
    private String lyric;
    private String lyricHtmlBody;
    private String cypher;
    private String cypherHtmlBody;
    private String traduction;
    private String traductionHtmlBody;
    private String album;
    private String composer;

	public String getLyricHtmlBody() {
		return lyricHtmlBody;
	}
	public void setLyricHtmlBody(String lyricHtmlBody) {
		this.lyricHtmlBody = lyricHtmlBody;
	}
	public String getCypherHtmlBody() {
		return cypherHtmlBody;
	}
	public void setCypherHtmlBody(String cypherHtmlBody) {
		this.cypherHtmlBody = cypherHtmlBody;
	}
	public String getTraductionHtmlBody() {
		return traductionHtmlBody;
	}
	public void setTraductionHtmlBody(String traductionHtmlBody) {
		this.traductionHtmlBody = traductionHtmlBody;
	}
	/** @return Obtem o campo id do Track. */
    public String getId() {
        return id;
    }
    /** @param id Ajusta o campo id do Track. */
    public void setId(String id) {
        this.id = id;
    }
    /** @return Obtem o campo artists do Track. */
    public Collection<Artist> getArtists() {
        return artists;
    }
    /** @param artists Ajusta o campo artists do Track. */
    public void setArtists(Collection<Artist> artists) {
        this.artists = artists;
    }
    /** @return Obtem o campo lyric do Track. */
    public String getLyric() {
        return lyric;
    }
    /** @param lyric Ajusta o campo lyric do Track. */
    public void setLyric(String lyric) {
        this.lyric = lyric;
    }
    /** @return Obtem o campo cypher do Track. */
    public String getCypher() {
        return cypher;
    }
    /** @param cypher Ajusta o campo cypher do Track. */
    public void setCypher(String cypher) {
        this.cypher = cypher;
    }
    /** @return Obtem o campo traduction do Track. */
    public String getTraduction() {
        return traduction;
    }
    /** @param traduction Ajusta o campo traduction do Track. */
    public void setTraduction(String traduction) {
        this.traduction = traduction;
    }
    /** @return Obtem o campo album do Track. */
    public String getAlbum() {
        return album;
    }
    /** @param album Ajusta o campo album do Track. */
    public void setAlbum(String album) {
        this.album = album;
    }
    /** @return Obtem o campo composer do Track. */
    public String getComposer() {
        return composer;
    }
    /** @param composer Ajusta o campo composer do Track. */
    public void setComposer(String composer) {
        this.composer = composer;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cypher == null) ? 0 : cypher.hashCode());
        result = prime * result + ((lyric == null) ? 0 : lyric.hashCode());
        result = prime * result + ((traduction == null) ? 0 : traduction.hashCode());
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
        Track other = (Track) obj;
        if (cypher == null) {
            if (other.cypher != null)
                return false;
        } else if (!cypher.equals(other.cypher))
            return false;
        if (lyric == null) {
            if (other.lyric != null)
                return false;
        } else if (!lyric.equals(other.lyric))
            return false;
        if (traduction == null) {
            if (other.traduction != null)
                return false;
        } else if (!traduction.equals(other.traduction))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Track [");
        if (id != null) {
            builder.append("id=");
            builder.append(id);
            builder.append(", ");
        }
        if (name != null) {
            builder.append("name=");
            builder.append(name);
            builder.append(", ");
        }
        if (artists != null) {
            builder.append("artists=");
            builder.append(artists);
            builder.append(", ");
        }
        if (lyric != null) {
            builder.append("lyric=");
            builder.append(lyric);
            builder.append(", ");
        }
        if (lyricHtmlBody != null) {
            builder.append("lyricHtmlBody=");
            builder.append(lyricHtmlBody);
            builder.append(", ");
        }
        if (cypher != null) {
            builder.append("cypher=");
            builder.append(cypher);
            builder.append(", ");
        }
        if (cypherHtmlBody != null) {
            builder.append("cypherHtmlBody=");
            builder.append(cypherHtmlBody);
            builder.append(", ");
        }
        if (traduction != null) {
            builder.append("traduction=");
            builder.append(traduction);
            builder.append(", ");
        }
        if (traductionHtmlBody != null) {
            builder.append("traductionHtmlBody=");
            builder.append(traductionHtmlBody);
            builder.append(", ");
        }
        if (album != null) {
            builder.append("album=");
            builder.append(album);
            builder.append(", ");
        }
        if (composer != null) {
            builder.append("composer=");
            builder.append(composer);
        }
        builder.append("]");
        return builder.toString();
    }
}
