package br.com.tamandua.service.vo.validator;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author sma_erosario
 *
 */
@XmlRootElement
public class RequiredFieldVO {

    private String parents= "";
    private String name;

    public RequiredFieldVO() {
        super();
    }


    public RequiredFieldVO(String name) {
        this.name = name;
    }

    public RequiredFieldVO(String name, String parents) {
        this.name = name;
        this.parents = parents;
    }

    /**
     * @return the name
     */
    @XmlElement (name = "field")
    public String getName() {
        return parents + name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return the parents
     */
    @XmlTransient
    public String getParents() {
        return parents;
    }


    /**
     * @param parents the parents to set
     */
    public void setParents(String parents) {
        this.parents = parents;
    }
}
