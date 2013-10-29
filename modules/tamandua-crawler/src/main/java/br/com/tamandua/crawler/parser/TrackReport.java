/**
 * 
 */
package br.com.tamandua.crawler.parser;

/**
 * @author Everton Rosario (erosario@gmail.com)
 * TrackReport
 */
public class TrackReport {
    private int totalTracks;
    private int totalLyrics;
    private int totalCyphers;
    private int totalTraductions;

    /**
     * Realiza a soma da instancia atual com a informada
     * @param anotherReport
     */
    public void increment(TrackReport anotherReport) {
        this.totalTracks      += anotherReport.getTotalTracks();
        this.totalLyrics      += anotherReport.getTotalLyrics();
        this.totalCyphers     += anotherReport.getTotalCyphers();
        this.totalTraductions += anotherReport.getTotalTraductions();
    }
    
    public void incTotalTracks(int tracks) {
        this.totalTracks += tracks;
    }
    
    public void incTotalLyrics(int lyrics) {
        this.totalLyrics += lyrics;
    }
    
    public void incTotalCyphers(int cyphers) {
        this.totalCyphers += cyphers;
    }

    public void incTotalTraductions(int traductions) {
        this.totalTraductions += traductions;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + totalCyphers;
        result = prime * result + totalLyrics;
        result = prime * result + totalTracks;
        result = prime * result + totalTraductions;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TrackReport other = (TrackReport) obj;
        if (totalCyphers != other.totalCyphers)
            return false;
        if (totalLyrics != other.totalLyrics)
            return false;
        if (totalTracks != other.totalTracks)
            return false;
        if (totalTraductions != other.totalTraductions)
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "TrackReport [totalCyphers=" + totalCyphers + ", totalLyrics="
                + totalLyrics + ", totalTracks=" + totalTracks
                + ", totalTraductions=" + totalTraductions + "]";
    }
    public int getTotalTracks() {
        return totalTracks;
    }
    public void setTotalTracks(int totalTracks) {
        this.totalTracks = totalTracks;
    }
    public int getTotalLyrics() {
        return totalLyrics;
    }
    public void setTotalLyrics(int totalLyrics) {
        this.totalLyrics = totalLyrics;
    }
    public int getTotalCyphers() {
        return totalCyphers;
    }
    public void setTotalCyphers(int totalCyphers) {
        this.totalCyphers = totalCyphers;
    }
    public int getTotalTraductions() {
        return totalTraductions;
    }
    public void setTotalTraductions(int totalTraductions) {
        this.totalTraductions = totalTraductions;
    }
}
