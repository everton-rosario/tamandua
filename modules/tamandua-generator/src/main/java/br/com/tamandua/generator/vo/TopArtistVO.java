package br.com.tamandua.generator.vo;

import javax.xml.bind.annotation.XmlRootElement;

import br.com.tamandua.service.vo.ArtistVO;

@XmlRootElement(name = "topArtist")
public class TopArtistVO extends ArtistVO implements java.io.Serializable, Comparable<TopArtistVO> {

	private static final long serialVersionUID = 6573855337581460542L;
	
	private Long pageViews;
		
	private Integer position;

	public Long getPageViews() {
		return pageViews;
	}

	public void setPageViews(Long pageViews) {
		this.pageViews = pageViews;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
	
	public void addPageViews(Long pageViews){
		this.pageViews += pageViews;
	}

	@Override
	public boolean equals(Object topArtist) {
		if(topArtist != null && topArtist instanceof TopArtistVO){
			return ((TopArtistVO)topArtist).getUri().equals(this.getUri());
		}
		return false;
	}

	@Override
	public int compareTo(TopArtistVO topArtistVO) {
		return topArtistVO.pageViews.compareTo(this.pageViews);
	}
	
}
