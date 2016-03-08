package br.com.tamandua.service.vo;

// Generated 21/02/2010 23:04:42 by Hibernate Tools 3.2.4.GA

import javax.xml.bind.annotation.XmlRootElement;

/**
 * ImageVO generated by hbm2java
 */
@XmlRootElement(name = "image")
public class ImageVO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private Long idImage;
    private String uri;
    private String url;
    private String description;
    private String file;
    private String location;
    private Integer width;
    private Integer height;
    private String provider;

    public ImageVO() {
    }

    public ImageVO(String uri, String url, String description, String file, String location, Integer width, Integer height) {
        this.uri = uri;
        this.url = url;
        this.description = description;
        this.file = file;
        this.location = location;
        this.width = width;
        this.height = height;
    }

    public Long getIdImage() {
        return this.idImage;
    }

    public void setIdImage(Long idImage) {
        this.idImage = idImage;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile() {
        return this.file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getWidth() {
        return this.width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

	@Override
	public String toString() {
		return "ImageVO ["
				+ (description != null ? "description=" + description + ", "
						: "") + (file != null ? "file=" + file + ", " : "")
				+ (height != null ? "height=" + height + ", " : "")
				+ (idImage != null ? "idImage=" + idImage + ", " : "")
				+ (location != null ? "location=" + location + ", " : "")
				+ (uri != null ? "uri=" + uri + ", " : "")
				+ (url != null ? "url=" + url + ", " : "")
				+ (width != null ? "width=" + width : "") + "]";
	}

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}