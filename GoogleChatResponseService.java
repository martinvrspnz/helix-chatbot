package com.bbva.chatbot.helix.service.impl;

import com.bbva.chatbot.helix.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GoogleChatResponseService {

	public Map<String, Object> createIncidentFormCard() {
		String json = """
				{
				  "text": "Registre su incidencia en el siguiente formulario:\\n",
				  "cardsV2": [
					{
					  "cardId": "cardRegistryIncident",
					  "card": {
						"name": "RegistryIncident",
						"header": {
						  "title": "HELIX",
						  "subtitle": "Soporte",
						  "imageUrl": "https://developers.google.com/chat/images/quickstart-app-avatar.png",
						  "imageType": "CIRCLE"
						},
						"sections": [
						  {
							"header": "Registrar Incidencia",
							"collapsible": true,
							"uncollapsibleWidgetsCount": 4,
							"widgets": [
							  {
								"textInput": {
								  "name": "registry",
								  "label": "Registro",
								  "type": "SINGLE_LINE"
								}
							  },
							  {
								"textInput": {
								  "name": "title",
								  "label": "Título de incidencia",
								  "type": "SINGLE_LINE"
								}
							  },
							  {
								"textInput": {
								  "name": "description",
								  "label": "Descripción de incidencia",
								  "type": "MULTIPLE_LINE"
								}
							  },
							  {
								"buttonList": {
								  "buttons": [
									{
									  "text": "Aceptar",
									  "type": "FILLED",
									  "color": {
										"red": 0,
										"green": 0.5,
										"blue": 1,
										"alpha": 1
									  },
									  "onClick": {
										"action": {
										  "function": "onClickOkButton"
										}
									  }
									},
									{
									  "text": "Cancelar",
									  "color": {
										"red": 1,
										"green": 0,
										"blue": 0,
										"alpha": 1
									  },
									  "type": "FILLED",
									  "onClick": {
										"action": {
										  "function": "onClickCancelButton"
										}
									  }
									}
								  ]
								}
							  }
							]
						  }
						]
					  }
					}
				  ]
				}
				""";

		return JsonUtil.jsonToMap(json);
	}

	public Map<String, Object> createTextResponse(String text) {
		String json = """
				{
				    "text": "<MESSAGE>"
				}
				""";
		String response = json.replace("<MESSAGE>", text);
		return JsonUtil.jsonToMap(response);
	}
}
