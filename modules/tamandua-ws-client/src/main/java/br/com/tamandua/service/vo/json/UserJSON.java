package br.com.tamandua.service.vo.json;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import br.com.tamandua.service.vo.ImageVO;
import br.com.tamandua.service.vo.UserVO;

public class UserJSON {

	public static String getString(UserVO vo){
		JSONObject json = new JSONObject();
		try{
			json.put("idUser", vo.getIdUser());
			json.put("email", vo.getEmail());
			json.put("name", vo.getName());
			if(vo.getImage() != null){
				json.put("imageUrl", vo.getImage().getUrl());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return json.toString();
	}
	
	public static UserVO getObject(String value){
		UserVO vo = null;
		try{
			JSONObject json = new JSONObject(value);

			vo = new UserVO();
			vo.setIdUser(json.getLong("idUser"));
			vo.setName(json.getString("name"));
			vo.setEmail(json.getString("email"));
			if(json.has("imageUrl")){
				ImageVO imageVO = new ImageVO();
				imageVO.setUrl(json.getString("imageUrl"));
				vo.setImage(imageVO);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return vo;
	}

}
