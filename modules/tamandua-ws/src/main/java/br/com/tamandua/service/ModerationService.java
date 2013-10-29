package br.com.tamandua.service;

import org.springframework.stereotype.Service;

import br.com.tamandua.persistence.app.AppContext;
import br.com.tamandua.service.adapter.ModerationAdapter;
import br.com.tamandua.service.vo.ModerationVO;
import br.com.tamandua.service.vo.validator.MessagesVO;

@Service
public class ModerationService {
	public MessagesVO correctMusic(ModerationVO moderation) {
		
		ModerationAdapter adapter = (ModerationAdapter) AppContext.getApplicationContext().getBean("moderationAdapter");
		return adapter.correctMusic(moderation);
	}
}
