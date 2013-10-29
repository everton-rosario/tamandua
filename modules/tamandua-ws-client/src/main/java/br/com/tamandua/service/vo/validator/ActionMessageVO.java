package br.com.tamandua.service.vo.validator;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Everton Rosario (erosario@gmail.com)
 * ActionMessageVO
 */
@XmlRootElement(name="action")
public class ActionMessageVO {
    public ActionMessageVO(Action action, LevelMessageVO message) {
        super();
        this.action = action;
        this.message = message;
    }
    public ActionMessageVO() {
        super();
    }
    public enum Action {
        CREATE, UPDATE, DELETE, READ;
    };
    
    private Action action;
    private LevelMessageVO message;
    public Action getAction() {
        return action;
    }
    public void setAction(Action action) {
        this.action = action;
    }
    public LevelMessageVO getMessage() {
        return message;
    }
    public void setMessage(LevelMessageVO message) {
        this.message = message;
    }
}
