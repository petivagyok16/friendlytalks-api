package com.friendlytalks.friendlytalksapi.common;

import com.friendlytalks.friendlytalksapi.model.*;
import com.friendlytalks.friendlytalksapi.repository.MessageRepository;
import com.friendlytalks.friendlytalksapi.repository.UserRepository;
import com.friendlytalks.friendlytalksapi.security.CustomPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class StartupRunner implements CommandLineRunner {

	private final MessageRepository messageRepository;
	private final UserRepository userRepository;
	private final CustomPasswordEncoder passwordEncryptor;

	public StartupRunner(
					MessageRepository messageRepository,
					UserRepository userRepository,
					CustomPasswordEncoder passwordEncryptor
	) {
		this.messageRepository = messageRepository;
		this.userRepository = userRepository;
		this.passwordEncryptor = passwordEncryptor;
	}

	@Override
	public void run(String... args) {
		log.info(" ------- Loading dummy data into MongoDB ------- ");
		this.addUsers();
		this.addMessages();
	}

	private void addUsers() {

		// Add messages
		Set<String> mockUser1Messages = new HashSet<>();
		mockUser1Messages.add("mockUser1Message1");
		mockUser1Messages.add("mockUser1Message2");

		// Add relations
		Relations mockUser1Relations = new Relations();
		Set<String> followers = new HashSet<>();
		followers.add("mockUser2");
		followers.add("testUser");
		mockUser1Relations.setFollowers(followers);

		Set<String> followings = new HashSet<>();
		followings.add("mockUser2");
		followings.add("testUser");
		mockUser1Relations.setFollowing(followings);

		// Init Ratings
		Ratings mockUser1Ratings = new Ratings();

		// Add my ratings
		RatingContainer myRatingsContainer = new RatingContainer();
		LinkedList<String> myLikes = new LinkedList<>();
		myLikes.add("mockUser2");
		myRatingsContainer.setLikes(myLikes);
		LinkedList<String> myDislikes = new LinkedList<>();
		myDislikes.add("testUser");
		myRatingsContainer.setDislikes(myDislikes);

		// Add given ratings
		RatingContainer givenRatingsContainer = new RatingContainer();
		LinkedList<String> givenLikes = new LinkedList<>();
		givenLikes.add("mockUser2Message1");
		LinkedList<String> givenDislikes= new LinkedList<>();
		givenDislikes.add("mockUser2Message2");
		givenDislikes.add("testUserMessage1");
		givenRatingsContainer.setLikes(givenLikes);
		givenRatingsContainer.setDislikes(givenDislikes);

		mockUser1Ratings.setMy(myRatingsContainer);
		mockUser1Ratings.setGiven(givenRatingsContainer);
		String hashedPassword1 = this.passwordEncryptor.encode("password");
		User katkun = new User(
						"mockUser1",
						"katkun",
						"katkun@asd.com",
						hashedPassword1,
						"kat",
						"kun",
						"Budapest",
						"https://i.imgur.com/vcVIGvg.jpg",
						mockUser1Messages,
						mockUser1Relations,
						mockUser1Ratings
		);
		List<String> userRole = new ArrayList<>();
		userRole.add("ROLE_USER");
		katkun.setRoles(userRole);

		// Add mock user 2

		// Add messages
		Set<String> mockUser2Messages = new HashSet<>();
		mockUser2Messages.add("mockUser2Message1");
		mockUser2Messages.add("mockUser2Message2");

		// Add relations
		Relations mockUser2Relations = new Relations();
		Set<String> followers2 = new HashSet<>();
		followers2.add("mockUser1");
		followers2.add("testUser");
		mockUser2Relations.setFollowers(followers2);

		Set<String> followings2 = new HashSet<>();
		followings2.add("mockUser1");
		followings2.add("testUser");
		mockUser2Relations.setFollowing(followings2);

		// Init Ratings
		Ratings mockUser2Ratings = new Ratings();

		// Add my ratings
		RatingContainer myRatingsContainer2 = new RatingContainer();
		LinkedList<String> myLikes2 = new LinkedList<>();
		myLikes2.add("mockUser1");
		myRatingsContainer2.setLikes(myLikes2);
		LinkedList<String> myDislikes2 = new LinkedList<>();
		myDislikes2.add("testUser");
		myRatingsContainer2.setDislikes(myDislikes2);

		// Add given ratings
		RatingContainer givenRatingsContainer2 = new RatingContainer();
		LinkedList<String> givenLikes2 = new LinkedList<>();
		givenLikes2.add("mockUser1Message1");
		givenLikes2.add("testUserMessage1");
		LinkedList<String> givenDislikes2 = new LinkedList<>();
		givenDislikes2.add("mockUser2Message2");
		givenRatingsContainer2.setDislikes(givenDislikes2);
		givenRatingsContainer2.setLikes(givenLikes2);

		mockUser2Ratings.setMy(myRatingsContainer2);
		mockUser2Ratings.setGiven(givenRatingsContainer2);

		User petplc = new User(
						"mockUser2",
						"petplc",
						"petplc@asd.com",
						hashedPassword1,
						"pet",
						"plc",
						"Budapest",
						"http://tventhusiast.nintendoenthusiast.com/wp-content/uploads/sites/8/2014/06/truedetective-1170x658.png",
						mockUser2Messages,
						mockUser2Relations,
						mockUser2Ratings
		);
		petplc.setRoles(userRole);

		// Add mock user 3

		// Add messages
		Set<String> mockUser3Messages = new HashSet<>();
		mockUser3Messages.add("testUserMessage1");
		mockUser3Messages.add("testUserMessage2");

		// Add relations
		Relations mockUser3Relations = new Relations();
		Set<String> followers3 = new HashSet<>();
		followers3.add("mockUser1");
		followers3.add("mockUser2");
		mockUser3Relations.setFollowers(followers3);

		Set<String> followings3 = new HashSet<>();
		followings3.add("mockUser1");
		followings3.add("mockUser2");
		mockUser3Relations.setFollowing(followings3);

		// Init Ratings
		Ratings mockUser3Ratings = new Ratings();

		// Add my ratings
		RatingContainer myRatingsContainer3 = new RatingContainer();
		LinkedList<String> myLikes3 = new LinkedList<>();
		myLikes3.add("mockUser1");
		myRatingsContainer3.setLikes(myLikes3);
		LinkedList<String> myDislikes3 = new LinkedList<>();
		myDislikes3.add("mockUser1");
		myRatingsContainer3.setDislikes(myDislikes3);

		// Add given ratings
		RatingContainer givenRatingsContainer3 = new RatingContainer();
		LinkedList<String> givenLikes3 = new LinkedList<>();
		givenLikes3.add("mockUser2Message1");
		givenLikes3.add("mockUser1Message1");
		LinkedList<String> givenDislikes3 = new LinkedList<>();
		givenDislikes3.add("mockUser2Message2");
		givenDislikes3.add("mockUser1Message2");
		givenRatingsContainer3.setDislikes(givenDislikes3);
		givenRatingsContainer3.setLikes(givenLikes3);

		mockUser3Ratings.setMy(myRatingsContainer3);
		mockUser3Ratings.setGiven(givenRatingsContainer3);
		String hashedPassword2 = this.passwordEncryptor.encode("test");
		User test = new User(
						"testUser",
						"test",
						"test@test.com",
						hashedPassword2,
						"test",
						"test",
						"test",
						"http://wp.patheos.com.s3.amazonaws.com/blogs/faithwalkers/files/2013/03/bigstock-Test-word-on-white-keyboard-27134336.jpg",
						mockUser3Messages,
						mockUser3Relations,
						mockUser3Ratings
		);
		ArrayList<String> adminRole = new ArrayList<>();
		adminRole.add("ROLE_ADMIN");
		test.setRoles(adminRole);

		ArrayList<User> users = new ArrayList<>();
		users.add(katkun);
		users.add(petplc);
		users.add(test);
		this.userRepository.saveAll(users).collectList().subscribe(savedUsers -> log.info(" ----- Users have been loaded -----"));
	}

	private void addMessages() {

		// Mock user 1 message 1
		Meta mockUser1Message1Meta = new Meta();
		Set<String> mockUser1Message1Likes = new HashSet<>();
		mockUser1Message1Likes.add("mockUser2");
		mockUser1Message1Likes.add("testUser");
		mockUser1Message1Meta.setLikes(mockUser1Message1Likes);
		Set<String> mockUser1Message1Dislikes= new HashSet<>();
		mockUser1Message1Meta.setDislikes(mockUser1Message1Dislikes);

		Message mockUser1Message1 = new Message(
						"mockUser1Message1",
						"Mock user message 1 content",
						new Date(),
						mockUser1Message1Meta,
						"mockUser1"
		);

		// Mock user 1 message 2
		Meta mockUser1Message2Meta = new Meta();
		Set<String> mockUser1Message2Likes = new HashSet<>();
		mockUser1Message2Likes.add("mockUser2");
		mockUser1Message2Meta.setLikes(mockUser1Message2Likes);
		Set<String> mockUser1Message2Dislikes= new HashSet<>();
		mockUser1Message2Dislikes.add("testUser");
		mockUser1Message2Meta.setDislikes(mockUser1Message2Dislikes);

		Message mockUser1Message2 = new Message(
						"mockUser1Message2",
						"Mock user message 2 content",
						new Date(),
						mockUser1Message1Meta,
						"mockUser1"
		);

		// Mock user 2 message 1
		Meta mockUser2Message1Meta = new Meta();
		Set<String> mockUser2Message1Likes = new HashSet<>();
		mockUser2Message1Likes.add("mockUser1");
		mockUser2Message1Likes.add("testUser");
		mockUser2Message1Meta.setLikes(mockUser2Message1Likes);
		Set<String> mockUser2Message1Dislikes= new HashSet<>();
		mockUser2Message1Meta.setDislikes(mockUser2Message1Dislikes);

		Message mockUser2Message1 = new Message(
						"mockUser2Message1",
						"Mock user message 1 content",
						new Date(),
						mockUser2Message1Meta,
						"mockUser2"
		);

		// Mock user 2 message 2
		Meta mockUser2Message2Meta = new Meta();
		Set<String> mockUser2Message2Likes = new HashSet<>();
		mockUser2Message2Likes.add("mockUser1");
		mockUser2Message2Meta.setLikes(mockUser2Message2Likes);
		Set<String> mockUser2Message2Dislikes = new HashSet<>();
		mockUser2Message2Dislikes.add("testUser");
		mockUser2Message2Meta.setDislikes(mockUser2Message2Dislikes);

		Message mockUser2Message2 = new Message(
						"mockUser2Message2",
						"Mock user 2 message 2 content",
						new Date(),
						mockUser2Message2Meta,
						"mockUser2"
		);

		// Mock user 3 message 1
		Meta testUserMessage1Meta = new Meta();
		Set<String> testUserMessage1Likes = new HashSet<>();
		testUserMessage1Likes.add("mockUser2");
		testUserMessage1Meta.setLikes(testUserMessage1Likes);
		Set<String> testUserMessage1Dislikes = new HashSet<>();
		testUserMessage1Meta.setDislikes(testUserMessage1Dislikes);

		Message testUserMessage1 = new Message(
						"testUserMessage1",
						"Test user message 1 content",
						new Date(),
						testUserMessage1Meta,
						"testUser"
		);

		// Mock user 3 message 2
		Meta testUserMessage2Meta = new Meta();
		Set<String> testUserMessage2Likes = new HashSet<>();
		testUserMessage2Meta.setLikes(testUserMessage2Likes);
		Set<String> testUserMessage2Dislikes = new HashSet<>();
		testUserMessage2Meta.setDislikes(testUserMessage2Dislikes);

		Message testUserMessage2 = new Message(
						"testUserMessage2",
						"Test user message 2 content",
						new Date(),
						testUserMessage2Meta,
						"testUser"
		);

		List<Message> messages = new ArrayList<>();
		messages.add(mockUser1Message1);
		messages.add(mockUser1Message2);
		messages.add(mockUser2Message1);
		messages.add(mockUser2Message2);
		messages.add(testUserMessage1);
		messages.add(testUserMessage2);

		this.messageRepository.saveAll(messages).collectList().subscribe(savedMessages -> log.info(" ----- Messages have been loaded ----- "));
	}

}
