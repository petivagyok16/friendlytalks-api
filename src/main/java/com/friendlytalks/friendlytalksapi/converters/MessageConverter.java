package com.friendlytalks.friendlytalksapi.converters;

import com.friendlytalks.friendlytalksapi.model.Message;
import com.friendlytalks.friendlytalksapi.model.EditedMessage;
import io.micrometer.core.lang.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Synchronized;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class MessageConverter implements Converter<EditedMessage, Message> {

	@Synchronized
	@Nullable
	@Override
	public Message convert(EditedMessage editedMessage, Message message) {

		if (editedMessage == null) {
			return null;
		}

		message.setContent(editedMessage.getContent());
		return message;
	}
}

