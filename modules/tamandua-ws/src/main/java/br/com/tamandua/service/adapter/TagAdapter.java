/**
 * 
 */
package br.com.tamandua.service.adapter;

import java.util.ArrayList;
import java.util.List;

import br.com.tamandua.persistence.EntityDescriptor;
import br.com.tamandua.persistence.Tag;
import br.com.tamandua.service.vo.EntityDescriptorVO;
import br.com.tamandua.service.vo.TagVO;

/**
 * @author BILL
 *
 */
public class TagAdapter {

    /* =====================================
     * 
     * PARSER's VO -->> ENTITY
     * 
     */
    public static List<Tag> parseVOs(List<TagVO> tagsVO) {
        if (tagsVO == null) {
            return null;
        }
        
        List<Tag> entities = new ArrayList<Tag>();
        for (TagVO tagVO : tagsVO) {
            entities.add(parse(tagVO));
        }
        return entities;
    }

    public static Tag parse(TagVO vo) {
        if (vo == null) {
            return null;
        }
        
        Tag entity = new Tag();
        entity.setDescription(vo.getDescription());
        entity.setEntityDescriptor(parse(vo.getEntityDescriptor()));
        entity.setIdEntity(vo.getIdEntity());
        entity.setIdTag(vo.getIdTag());
        entity.setName(vo.getName());
        entity.setOwner(vo.getOwner());
        entity.setType(vo.getType());

        return entity;
    }

    private static EntityDescriptor parse(EntityDescriptorVO vo) {
        if (vo == null) {
            return null;
        }
        
        EntityDescriptor entity = new EntityDescriptor();
        entity.setEntityName(vo.getEntityName());
        entity.setIdEntityDescriptor(vo.getIdEntityDescriptor());
        entity.setTableName(vo.getTableName());

        return entity;
    }

    /* =====================================
     * 
     * PARSER's ENTITY -->> VO
     * 
     */
    public static List<TagVO> parse(List<Tag> entities) {
        if (entities == null) {
            return null;
        }
        
        List<TagVO> vos = new ArrayList<TagVO>();
        for (Tag entity : entities) {
            vos.add(parse(entity));
        }
        return vos;
    }

    public static TagVO parse(Tag entity) {
        if (entity == null) {
            return null;
        }
        
        TagVO vo = new TagVO();
        vo.setDescription(entity.getDescription());
        vo.setEntityDescriptor(parse(entity.getEntityDescriptor()));
        vo.setIdEntity(entity.getIdEntity());
        vo.setIdTag(entity.getIdTag());
        vo.setName(entity.getName());
        vo.setOwner(entity.getOwner());
        vo.setType(entity.getType());

        return vo;
    }

    private static EntityDescriptorVO parse(EntityDescriptor entity) {
        if (entity == null) {
            return null;
        }
        
        EntityDescriptorVO vo = new EntityDescriptorVO();
        vo.setEntityName(entity.getEntityName());
        vo.setIdEntityDescriptor(entity.getIdEntityDescriptor());
        vo.setTableName(entity.getTableName());

        return vo;
    }

}
