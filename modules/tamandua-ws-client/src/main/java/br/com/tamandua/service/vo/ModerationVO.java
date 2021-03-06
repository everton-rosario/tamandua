package br.com.tamandua.service.vo;

// Generated 21/02/2010 23:04:42 by Hibernate Tools 3.2.4.GA

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * ModerationVO generated by hbm2java
 */
@XmlRootElement(name = "moderation")
public class ModerationVO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private Long idModeration;
    private EntityDescriptorVO entityDescriptor;
    private UserVO user;
    private Date moderationDate;
    private Long idEntity;
    private String status;
    private String oldValue;
    private String newValue;
    private String affectedFieldName;

    public ModerationVO() {
    }

    public ModerationVO(EntityDescriptorVO entityDescriptor, UserVO user, Date moderationDate, Long idEntity, String status,
            String oldValue, String newValue, String affectedFieldName) {
        this.entityDescriptor = entityDescriptor;
        this.user = user;
        this.moderationDate = moderationDate;
        this.idEntity = idEntity;
        this.status = status;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.affectedFieldName = affectedFieldName;
    }

    public Long getIdModeration() {
        return this.idModeration;
    }

    public void setIdModeration(Long idModeration) {
        this.idModeration = idModeration;
    }

    public EntityDescriptorVO getEntityDescriptor() {
        return this.entityDescriptor;
    }

    public void setEntityDescriptor(EntityDescriptorVO entityDescriptor) {
        this.entityDescriptor = entityDescriptor;
    }

    public UserVO getUser() {
        return this.user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public Date getModerationDate() {
        return this.moderationDate;
    }

    public void setModerationDate(Date moderationDate) {
        this.moderationDate = moderationDate;
    }

    public Long getIdEntity() {
        return this.idEntity;
    }

    public void setIdEntity(Long idEntity) {
        this.idEntity = idEntity;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOldValue() {
        return this.oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return this.newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getAffectedFieldName() {
        return this.affectedFieldName;
    }

    public void setAffectedFieldName(String affectedFieldName) {
        this.affectedFieldName = affectedFieldName;
    }

}
