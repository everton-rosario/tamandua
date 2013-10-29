package br.com.tamandua.generator.vo;

import javax.xml.bind.annotation.XmlRootElement;

import br.com.tamandua.service.vo.MusicVO;

@XmlRootElement(name = "topMusic")
public class TopMusicVO extends MusicVO implements java.io.Serializable, Comparable<TopMusicVO> {
	
	private static final long serialVersionUID = -2700495714252255528L;
	
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
	
	@Override
	public boolean equals(Object topMusic) {
		if(topMusic != null && topMusic instanceof TopMusicVO){
			return ((TopMusicVO)topMusic).getUri().equals(this.getUri());
		}
		return false;
	}

	@Override
	public int compareTo(TopMusicVO topMusicVO) {
		return topMusicVO.pageViews.compareTo(this.pageViews);
	}
	
}
