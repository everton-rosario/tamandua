package br.com.tamandua.service.vo;

// Generated 21/02/2010 23:04:42 by Hibernate Tools 3.2.4.GA

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * UserVO generated by hbm2java
 */
@XmlRootElement(name = "user")
public class UserVO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private Long idUser;
    private ImageVO image;
    private String email;
    private String password;
    private String role;
    private String cpf;
    private String rg;
    private String street;
    private String city;
    private Integer streetNumber;
    private String streetComplement;
    private String state;
    private String country;
    private String status;
    private String activationCode;
    private byte[] file;
    private Set<MultimediaVO> multimedias = new HashSet<MultimediaVO>(0);
    private Set<AnnotationVO> annotations = new HashSet<AnnotationVO>(0);
    private Set<ModerationVO> moderations = new HashSet<ModerationVO>(0);

    public UserVO() {
    }

    public UserVO(String name, ImageVO image, String email, String password, String role, String cpf, String rg, String street,
    		String city, Integer streetNumber, String streetComplement, String state, String country, String status, String activationCode,
    		byte[] file, Set<MultimediaVO> multimedias, Set<AnnotationVO> annotations, Set<ModerationVO> moderations) {
    	this.name = name;
        this.image = image;
        this.email = email;
        this.password = password;
        this.role = role;
        this.cpf = cpf;
        this.rg = rg;
        this.street = street;
        this.city = city;
        this.streetNumber = streetNumber;
        this.streetComplement = streetComplement;
        this.state = state;
        this.country = country;
        this.status = status;
        this.file = file;
        this.activationCode = activationCode;
        this.multimedias = multimedias;
        this.annotations = annotations;
        this.moderations = moderations;
    }

    public Long getIdUser() {
        return this.idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ImageVO getImage() {
        return this.image;
    }

    public void setImage(ImageVO image) {
        this.image = image;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCpf() {
        return this.cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getStreetNumber() {
        return this.streetNumber;
    }

    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreetComplement() {
        return this.streetComplement;
    }

    public void setStreetComplement(String streetComplement) {
        this.streetComplement = streetComplement;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public Set<MultimediaVO> getMultimedias() {
        return this.multimedias;
    }

    public void setMultimedias(Set<MultimediaVO> multimedias) {
        this.multimedias = multimedias;
    }

    public Set<AnnotationVO> getAnnotations() {
        return this.annotations;
    }

    public void setAnnotations(Set<AnnotationVO> annotations) {
        this.annotations = annotations;
    }

    public Set<ModerationVO> getModerations() {
        return this.moderations;
    }

    public void setModerations(Set<ModerationVO> moderations) {
        this.moderations = moderations;
    }

}
