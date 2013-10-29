package br.com.tamandua.service.vo.validator;

import java.lang.reflect.Method;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Everton Rosario (erosario@gmail.com)
 */
public class RequiredFieldValidator {

    /**
     * Faz a validacao retornando um objeto de mensagens.
     * @param entity
     * @return
     */
    public static MessagesVO validateEntity(Object entity) {
        return validate(entity, null);
    }

    /**
     * 
     * @param entity
     * @param parents
     * @return
     */
    private static MessagesVO validate(Object entity, String parents) {
        MessagesVO erros = new MessagesVO();
        if (entity != null && entity.getClass().isAnnotationPresent(XmlRootElement.class)) {
            erros.addAllRequireds(validateFields(entity, entity.getClass().getMethods(), parents));
        }
        return erros;
    }

    private static MessagesVO validateFields(Object entity, Method[] methods, String parents) {
        MessagesVO requireds = new MessagesVO();
        for (Method method : methods) {
            // Trata conteudo de fields marcados como XmlElement
            if (method.isAnnotationPresent(XmlElement.class)) {
                XmlElement element = method.getAnnotation(XmlElement.class);
                try {
                    Object property = method.invoke(entity, null);
                    // Caso obtenha algum property que seja filho, como RootElement (precisa fazer recursao para tratar os subcampos)
                    if (property != null && property.getClass().isAnnotationPresent(XmlRootElement.class)) {
                        requireds.addAllRequireds(validate(property,getTreeName(element.name(),parents)));
                    }
                    // Verifica se o campo eh obrigatorio
                    if (element.required() && property == null) {
                        requireds.addRequired(new RequiredFieldVO(getTreeName(element.name(),parents)));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            // Trata conteudo de fields marcados como XmlAttribute
            if (method.isAnnotationPresent(XmlAttribute.class)) {
                XmlAttribute element = method.getAnnotation(XmlAttribute.class);
                try {
                    Object property = method.invoke(entity, null);
                    // Caso obtenha algum property que seja filho, como RootElement (precisa fazer recursao para tratar os subcampos)
                    if (property != null && property.getClass().isAnnotationPresent(XmlRootElement.class)) {
                        requireds.addAllRequireds(validate(property,getTreeName(element.name(),parents)));
                    }
                    // Verifica se o campo eh obrigatorio
                    if (element.required() && property == null) {
                        requireds.addRequired(new RequiredFieldVO(getTreeName(element.name(),parents)));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return requireds;
    }

    private static String getTreeName(String name, String parents) {
        if (parents == null || parents.isEmpty()) {
            return name;
        } else 
        return parents + '.' + name;
    }
}
