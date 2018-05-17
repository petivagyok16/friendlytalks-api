package com.friendlytalks.friendlytalksapi.common;

import com.friendlytalks.friendlytalksapi.model.Message;
import com.friendlytalks.friendlytalksapi.model.User;
import reactor.util.function.Tuple3;

/**
 * RatingFactory handles all the Rating flow
 */
public class RatingFactory {

	private static Message ratedMessage;
	private static User raterUser;
	private static User messageOwner;

	/**
	 *
	 * @param raterUserId userId of the user who sent the rating
	 * @param messageId messageId of the message which was rated by the RaterUser
	 * @param publisherList A Tuple which contains all the exact database documents
	 */
	public static void addNoRating(String raterUserId, String messageId, Tuple3<Message, User, User> publisherList) {
		RatingFactory.setPublishers(publisherList);
		RatingFactory.removeRatings(raterUserId, messageId);
	}

	public static void addLike(String raterUserId, String messageId, Tuple3<Message, User, User> publisherList) {
		RatingFactory.setPublishers(publisherList);
		RatingFactory.removeRatings(raterUserId, messageId);

		messageOwner.getRatings().getMy().getLikes().add(raterUserId);
		ratedMessage.getMeta().getLikes().add(raterUserId);
		raterUser.getRatings().getGiven().getLikes().add(messageId);
	}

	public static void addDislike(String raterUserId, String messageId, Tuple3<Message, User, User> publisherList) {
		RatingFactory.setPublishers(publisherList);
		RatingFactory.removeRatings(raterUserId, messageId);

		messageOwner.getRatings().getMy().getDislikes().add(raterUserId);
		ratedMessage.getMeta().getDislikes().add(raterUserId);
		raterUser.getRatings().getGiven().getDislikes().add(messageId);
	}

	private static void removeRatings(String raterUserId, String messageId) {

		if (ratedMessage.getMeta().getDislikes().contains(raterUserId)) {
			// Remove dislikes
			messageOwner.getRatings().getMy().getDislikes().remove(raterUserId);
			ratedMessage.getMeta().getDislikes().remove(raterUserId);
			raterUser.getRatings().getGiven().getDislikes().remove(messageId);

		} else if (ratedMessage.getMeta().getLikes().contains(raterUserId)) {
			// Remove likes
			messageOwner.getRatings().getMy().getLikes().remove(raterUserId);
			ratedMessage.getMeta().getLikes().remove(raterUserId);
			raterUser.getRatings().getGiven().getLikes().remove(messageId);
		}

	}

	/**
	 *
	 * @param publisherList A Tuple which contains all the exact database documents
	 *
	 * We set fresh publishers each time we perform a new Rating operation
	 */
	private static void setPublishers(Tuple3<Message, User, User> publisherList) {
		RatingFactory.ratedMessage = publisherList.getT1();
		RatingFactory.raterUser = publisherList.getT2();
		RatingFactory.messageOwner = publisherList.getT3();
	}
}
