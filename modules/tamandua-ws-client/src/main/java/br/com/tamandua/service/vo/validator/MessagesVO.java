/**
 * 
 */
package br.com.tamandua.service.vo.validator;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author sma_erosario
 *
 */
@XmlRootElement (name = "messages")
public class MessagesVO {
    private List<RequiredFieldVO> requireds = new ArrayList<RequiredFieldVO>();
    private List<LevelMessageVO> errors = new ArrayList<LevelMessageVO>();
    private List<LevelMessageVO> warns = new ArrayList<LevelMessageVO>();
    private List<LevelMessageVO> infos = new ArrayList<LevelMessageVO>();
    private List<LevelMessageVO> fatals = new ArrayList<LevelMessageVO>();
    private List<ActionMessageVO> actions = new ArrayList<ActionMessageVO>();
    
    public void merge(MessagesVO anotherMessages) {
    	requireds.addAll(anotherMessages.requireds);
    	errors.addAll(anotherMessages.errors);
    	warns.addAll(anotherMessages.warns);
    	infos.addAll(anotherMessages.infos);
    	fatals.addAll(anotherMessages.fatals);
    	actions.addAll(anotherMessages.actions);
    }

    public List<ActionMessageVO> getActions() {
        return actions;
    }

    /**
     * @return the requireds
     */
    @XmlElement ( name = "required-fields" )
    public List<RequiredFieldVO> getRequireds() {
        return requireds;
    }

    /**
     * @param requireds the requireds to set
     */
    public void setRequireds(List<RequiredFieldVO> requireds) {
        this.requireds = requireds;
    }

    public void addAllRequireds(List<? extends RequiredFieldVO> validateFields) {
        requireds.addAll(validateFields);
    }

    public void addAllRequireds(MessagesVO messages) {
        requireds.addAll(messages.getRequireds());
    }

    public boolean hasError() {
        return !requireds.isEmpty() || !errors.isEmpty() || !fatals.isEmpty();
    }

    public void addRequired(RequiredFieldVO validateFields) {
        if (validateFields != null) {
            this.requireds.add(validateFields);
        }
    }
    
    public void addError(String message) {
        addError(message, null);
    }
    
    public void addAction(ActionMessageVO action) {
        this.actions.add(action);
    }
    
    public void addError(String message, Throwable throwable) {
        this.errors.add(new LevelMessageVO(Level.ERROR, message, throwable));
    }
    
    public void addInfo(String message) {
        addInfo(message, null);
    }
    
    public void addInfo(String message, Throwable throwable) {
        this.infos.add(new LevelMessageVO(Level.INFO, message, throwable));
    }

    public void addWarn(String message) {
        addWarn(message, null);
    }
    
    public void addWarn(String message, Throwable throwable) {
        this.warns.add(new LevelMessageVO(Level.WARN, message, throwable));
    }

    public void addFatal(String message) {
        addFatal(message, null);
    }
    
    public void addFatal(String message, Throwable throwable) {
        this.warns.add(new LevelMessageVO(Level.FATAL, message, throwable));
    }

    /**
     * @return the errors
     */
    public List<LevelMessageVO> getErrors() {
        return errors;
    }

    /**
     * @return the warns
     */
    public List<LevelMessageVO> getWarns() {
        return warns;
    }

    /**
     * @return the infos
     */
    public List<LevelMessageVO> getInfos() {
        return infos;
    }

    /**
     * @return the fatals
     */
    public List<LevelMessageVO> getFatals() {
        return fatals;
    }

    @Override
    public String toString() {
        return "MessagesVO [" + (actions != null ? "actions=" + actions + ", " : "") + (errors != null ? "errors=" + errors + ", " : "")
                + (fatals != null ? "fatals=" + fatals + ", " : "") + (infos != null ? "infos=" + infos + ", " : "")
                + (requireds != null ? "requireds=" + requireds + ", " : "") + (warns != null ? "warns=" + warns : "") + "]";
    }
    
    
}
