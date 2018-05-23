package com.friendlytalks.friendlytalksapi.converters;

import com.friendlytalks.friendlytalksapi.model.EditedUser;
import com.friendlytalks.friendlytalksapi.model.User;
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
public class UserConverter implements Converter<EditedUser, User> {

	@Synchronized
	@Nullable
	@Override
	public User convert(EditedUser editedUser, User user) {

		if (editedUser == null) {
			return null;
		}

		user.setCity(editedUser.getCity());
		user.setEmail(editedUser.getEmail());
		user.setFirstName(editedUser.getFirstName());
		user.setLastName(editedUser.getLastName());
		user.setPictureUrl(editedUser.getPictureUrl());
		return user;
	}
}
