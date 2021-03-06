package br.com.tamandua.persistence;

// Generated 21/02/2010 23:04:42 by Hibernate Tools 3.2.4.GA

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Multimedia generated by hbm2java
 */
@Entity
@Table(name = "multimedia")
public class Multimedia implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Long idMultimedia;
    private EntityDescriptor entityDescriptor;
    private User user;
    private Long idEntity;
    private String mediaType;
    private String title;
    private String description;
    private String contactInformation;
    private String embedCode;
    private String uri;
    private String url;

    public Multimedia() {
    }

    public Multimedia(EntityDescriptor entityDescriptor, User user, Long idEntity, String mediaType, String title,
            String description, String contactInformation, String embedCode, String uri, String url) {
        this.entityDescriptor = entityDescriptor;
        this.user = user;
        this.idEntity = idEntity;
        this.mediaType = mediaType;
        this.title = title;
        this.description = description;
        this.contactInformation = contactInformation;
        this.embedCode = embedCode;
        this.uri = uri;
        this.url = url;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_multimedia", unique = true, nullable = false)
    public Long getIdMultimedia() {
        return this.idMultimedia;
    }

    public void setIdMultimedia(Long idMultimedia) {
        this.idMultimedia = idMultimedia;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entity_descriptor")
    public EntityDescriptor getEntityDescriptor() {
        return this.entityDescriptor;
    }

    public void setEntityDescriptor(EntityDescriptor entityDescriptor) {
        this.entityDescriptor = entityDescriptor;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "id_entity")
    public Long getIdEntity() {
        return this.idEntity;
    }

    public void setIdEntity(Long idEntity) {
        this.idEntity = idEntity;
    }

    @Column(name = "media_type", length = 21)
    public String getMediaType() {
        return this.mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    @Column(name = "title", length = 200)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "description", length = 1000)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "contact_information", length = 1000)
    public String getContactInformation() {
        return this.contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    @Column(name = "embed_code", length = 3000)
    public String getEmbedCode() {
        return this.embedCode;
    }

    public void setEmbedCode(String embedCode) {
        this.embedCode = embedCode;
    }

    @Column(name = "uri", length = 500)
    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Column(name = "url", length = 1500)
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
