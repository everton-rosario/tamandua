package br.com.tamandua.service.adapter;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.tamandua.persistence.EntityDescriptor;
import br.com.tamandua.persistence.Moderation;
import br.com.tamandua.persistence.app.AppContext;
import br.com.tamandua.persistence.dao.LyricHome;
import br.com.tamandua.persistence.dao.ModerationHome;
import br.com.tamandua.service.vo.ModerationVO;
import br.com.tamandua.service.vo.validator.ActionMessageVO;
import br.com.tamandua.service.vo.validator.Level;
import br.com.tamandua.service.vo.validator.LevelMessageVO;
import br.com.tamandua.service.vo.validator.MessagesVO;
import br.com.tamandua.service.vo.validator.ActionMessageVO.Action;

@Component
public class ModerationAdapter {
	@Transactional
	public MessagesVO correctMusic(ModerationVO moderation) {
		MessagesVO messages = new MessagesVO();
		ModerationHome moderationHome = (ModerationHome)AppContext.getApplicationContext().getBean("moderationHome");
		Moderation parse = parse(moderation);
		parse.setEntityDescriptor(EntityDescriptor.ENTITY_LYRIC);
		parse.setStatus("MODIFIED");
		
		UserAdapter userAdapter = (UserAdapter)AppContext.getApplicationContext().getBean("userAdapter");
		parse.setUser(userAdapter.parse(moderation.getUser()));
		
		LyricHome lyricHome = (LyricHome) AppContext.getApplicationContext().getBean("lyricHome");
		parse.setOldValue(lyricHome.findById(moderation.getIdEntity()).getText());
		parse.setModerationDate(new Date());
		parse.setAffectedFieldName("text");
		
		moderationHome.persist(parse);
		moderationHome.flush();
		messages.addAction(new ActionMessageVO(Action.CREATE, new LevelMessageVO(Level.INFO, "moderation ["+moderation.getIdEntity()+"] created")));
		
		return messages;
	}
	public Moderation parse(ModerationVO vo){
		Moderation entity = new Moderation();
		entity.setIdEntity(vo.getIdEntity());
		entity.setStatus(vo.getStatus());
		entity.setNewValue(vo.getNewValue());
		return entity;
	}
}
