/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.facebook.api;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.social.InsufficientPermissionException;
import org.springframework.social.NotAuthorizedException;

public class LikeTemplateTest extends AbstractFacebookApiTest {
	
	private static final PagingParameters PAGING_PARAMS_25_50 = new PagingParameters(25, 50, null, null);

	@Test
	public void like() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456/likes"))
			.andExpect(method(POST))
			.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));			
		facebook.likeOperations().like("123456");
		mockServer.verify();
	}
	
	@Test(expected = InsufficientPermissionException.class)
	public void like_objectAccessNotPermitted() {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456/likes"))
			.andExpect(method(POST))
			.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withStatus(HttpStatus.FORBIDDEN).body(jsonResource("error-permission")).contentType(MediaType.APPLICATION_JSON));
		facebook.likeOperations().like("123456");
	}
	
	@Test(expected = NotAuthorizedException.class)
	public void like_unauthorized() {
		unauthorizedFacebook.likeOperations().like("123456");
	}

	@Test
	public void unlike() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456/likes"))
			.andExpect(method(POST))
			.andExpect(content().string("method=delete"))
			.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));
		facebook.likeOperations().unlike("123456");
		mockServer.verify();
	}

	@Test(expected = NotAuthorizedException.class)
	public void unlike_unauthorized() {
		unauthorizedFacebook.likeOperations().unlike("123456");
	}
	
	@Test
	public void getLikes() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/12345678/likes?limit=1&offset=0&summary=true")).andExpect(method(GET))
		    .andExpect(header("Authorization", "OAuth someAccessToken")).andRespond(
		        withSuccess(jsonResource("user-references-with-summary"), MediaType.APPLICATION_JSON));
		Integer likeCount = facebook.likeOperations().getLikeCount("12345678");
		assertEquals(Integer.valueOf(3), likeCount);
	}

	@Test
	public void getLikeCount() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/12345678/likes")).andExpect(method(GET)).andExpect(
		    header("Authorization", "OAuth someAccessToken")).andRespond(withSuccess(jsonResource("user-references"), MediaType.APPLICATION_JSON));
		PagedList<Reference> likes = facebook.likeOperations().getLikes("12345678");
		assertEquals(3, likes.size());
		assertEquals("Michael Scott", likes.get(0).getName());
		assertEquals("100000737708615", likes.get(0).getId());
		assertEquals("Michael Scott", likes.get(1).getName());
		assertEquals("100000354483321", likes.get(1).getId());
		assertEquals("Michael Scott", likes.get(2).getName());
		assertEquals("1184963857", likes.get(2).getId());
	}

	@Test
	public void getPagesLiked() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/me/likes?fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getPagesLiked();
		assertLikes(likes);
	}

	@Test
	public void getPagesLiked_withPagingParameters() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/me/likes?limit=25&offset=50&fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getPagesLiked(PAGING_PARAMS_25_50);
		assertLikes(likes);
	}

	@Test(expected = NotAuthorizedException.class)
	public void getPagesLiked_unauthorized() {
		unauthorizedFacebook.likeOperations().getPagesLiked();
	}
	
	@Test
	public void getPagesLiked_forSpecificUser() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456789/likes?fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getPagesLiked("123456789");
		assertLikes(likes);
	}

	@Test
	public void getPagesLiked_forSpecificUser_withPagingParameters() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456789/likes?limit=25&offset=50&fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getPagesLiked("123456789", PAGING_PARAMS_25_50);
		assertLikes(likes);
	}

	@Test(expected = NotAuthorizedException.class)
	public void getLikes_forSpecificUser_unauthorized() {
		unauthorizedFacebook.likeOperations().getPagesLiked("123456789");
	}
	
	@Test
	public void getBooks() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/me/books?fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getBooks();
		assertLikes(likes);
	}

	@Test
	public void getBooks_withPagingParameters() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/me/books?limit=25&offset=50&fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getBooks(PAGING_PARAMS_25_50);
		assertLikes(likes);
	}

	@Test(expected = NotAuthorizedException.class)
	public void getBooks_unauthorized() {
		unauthorizedFacebook.likeOperations().getBooks();
	}
	
	@Test
	public void getBooks_forSpecificUser() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456789/books?fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getBooks("123456789");
		assertLikes(likes);
	}	
	
	@Test
	public void getBooks_forSpecificUser_withPagingParameters() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456789/books?limit=25&offset=50&fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getBooks("123456789", PAGING_PARAMS_25_50);
		assertLikes(likes);
	}	
	
	@Test(expected = NotAuthorizedException.class)
	public void getBooks_forSpecificUser_unauthorized() {
		unauthorizedFacebook.likeOperations().getBooks("123456789");
	}
	
	@Test
	public void getMovies() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/me/movies?fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getMovies();
		assertLikes(likes);
	}
	
	@Test
	public void getMovies_withPagingParameters() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/me/movies?limit=25&offset=50&fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getMovies(PAGING_PARAMS_25_50);
		assertLikes(likes);
	}
	
	@Test(expected = NotAuthorizedException.class)
	public void getMovies_unauthorized() {
		unauthorizedFacebook.likeOperations().getMovies();
	}

	@Test
	public void getMovies_forSpecificUser() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456789/movies?fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getMovies("123456789");
		assertLikes(likes);
	}
	
	@Test
	public void getMovies_forSpecificUser_withPagingParameters() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456789/movies?limit=25&offset=50&fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getMovies("123456789", PAGING_PARAMS_25_50);
		assertLikes(likes);
	}
	
	@Test(expected = NotAuthorizedException.class)
	public void getMovies_forSpecificUser_unauthorized() {
		unauthorizedFacebook.likeOperations().getMovies("123456789");
	}
	
	@Test
	public void getMusic() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/me/music?fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getMusic();
		assertLikes(likes);
	}

	@Test
	public void getMusic_withPagingParameters() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/me/music?limit=25&offset=50&fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getMusic(PAGING_PARAMS_25_50);
		assertLikes(likes);
	}

	@Test(expected = NotAuthorizedException.class)
	public void getMusic_unauthorized() {
		unauthorizedFacebook.likeOperations().getMusic();
	}
	
	@Test
	public void getMusic_forSpecificUser() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456789/music?fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getMusic("123456789");
		assertLikes(likes);
	}

	@Test
	public void getMusic_forSpecificUser_withPagingParameters() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456789/music?limit=25&offset=50&fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getMusic("123456789", PAGING_PARAMS_25_50);
		assertLikes(likes);
	}

	@Test(expected = NotAuthorizedException.class)
	public void getMusic_forSpecificUser_unauthorized() {
		unauthorizedFacebook.likeOperations().getMusic("123456789");
	}
	
	@Test
	public void getTelevision() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/me/television?fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getTelevision();
		assertLikes(likes);
	}

	@Test
	public void getTelevision_withPagingParameters() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/me/television?limit=25&offset=50&fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getTelevision(PAGING_PARAMS_25_50);
		assertLikes(likes);
	}

	@Test(expected = NotAuthorizedException.class)
	public void getTelevision_unauthorized() {
		unauthorizedFacebook.likeOperations().getTelevision();
	}
	
	@Test
	public void getTelevision_forSpecificUser() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456789/television?fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getTelevision("123456789");
		assertLikes(likes);
	}
		
	@Test
	public void getTelevision_forSpecificUser_withPagingParameters() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456789/television?limit=25&offset=50&fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getTelevision("123456789", PAGING_PARAMS_25_50);
		assertLikes(likes);
	}
		
	@Test(expected = NotAuthorizedException.class)
	public void getTelevision_forSpecificUser_unauthorized() {
		unauthorizedFacebook.likeOperations().getTelevision("123456789");
	}
	
	@Test
	public void getActivities() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/me/activities?fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getActivities();
		assertLikes(likes);
	}

	@Test
	public void getActivities_withPagingParameters() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/me/activities?limit=25&offset=50&fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getActivities(PAGING_PARAMS_25_50);
		assertLikes(likes);
	}

	@Test(expected = NotAuthorizedException.class)
	public void getActivities_unauthorized() {
		unauthorizedFacebook.likeOperations().getActivities();
	}
	
	@Test
	public void getActivities_forSpecificUser() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456789/activities?fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getActivities("123456789");
		assertLikes(likes);
	}
	
	@Test
	public void getActivities_forSpecificUser_withPagingParameters() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456789/activities?limit=25&offset=50&fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getActivities("123456789", PAGING_PARAMS_25_50);
		assertLikes(likes);
	}
	
	@Test(expected = NotAuthorizedException.class)
	public void getActivities_forSpecificUser_unauthorized() {
		unauthorizedFacebook.likeOperations().getActivities("123456789");
	}

	@Test
	public void getInterests() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/me/interests?fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getInterests();
		assertLikes(likes);
	}

	@Test
	public void getInterests_withPagingParameters() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/me/interests?limit=25&offset=50&fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getInterests(PAGING_PARAMS_25_50);
		assertLikes(likes);
	}

	@Test(expected = NotAuthorizedException.class)
	public void getInterests_unauthorized() {
		unauthorizedFacebook.likeOperations().getInterests();
	}

	@Test
	public void getInterests_forSpecificUser() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456789/interests?fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getInterests("123456789");
		assertLikes(likes);
	}
	
	@Test
	public void getInterests_forSpecificUser_withPagingParameters() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456789/interests?limit=25&offset=50&fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("new-user-likes"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getInterests("123456789", PAGING_PARAMS_25_50);
		assertLikes(likes);
	}
	
	@Test(expected = NotAuthorizedException.class)
	public void getInterests_forSpecificUser_unauthorized() {
		unauthorizedFacebook.likeOperations().getInterests("123456789");
	}

	@Test
	public void getGames() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/me/games?fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("games"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getGames();
		assertGames(likes);
	}

	@Test
	public void getGames_withPagingParameters() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/me/games?limit=25&offset=50&fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("games"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getGames(PAGING_PARAMS_25_50);
		assertGames(likes);
	}

	@Test(expected = NotAuthorizedException.class)
	public void getGames_unauthorized() {
		unauthorizedFacebook.likeOperations().getGames();
	}

	@Test
	public void getGames_forSpecificUser() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456789/games?fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("games"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getGames("123456789");
		assertGames(likes);
	}
	
	@Test
	public void getGames_forSpecificUser_withPagingParameters() {
		mockServer.expect(requestTo("https://graph.facebook.com/v2.2/123456789/games?limit=25&offset=50&fields=id%2Cname%2Ccategory%2Cdescription%2Clocation%2Cwebsite%2Cpicture%2Cphone%2Caffiliation%2Ccompany_overview%2Clikes%2Ccheckins%2Ccover")).andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("games"), MediaType.APPLICATION_JSON));
		List<Page> likes = facebook.likeOperations().getGames("123456789", PAGING_PARAMS_25_50);
		assertGames(likes);
	}
	
	@Test(expected = NotAuthorizedException.class)
	public void getGames_forSpecificUser_unauthorized() {
		unauthorizedFacebook.likeOperations().getGames("123456789");
	}
		
	@SuppressWarnings("deprecation")
	private void assertLikes(List<Page> likes) {
		assertEquals(3, likes.size());
		Page like1 = likes.get(0);
		assertEquals("5678046685", like1.getId());
		assertEquals("U2", like1.getName());
		assertEquals("Musician/band", like1.getCategory());
		assertEquals("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-snc4/277150_5678046685_1509018948_q.jpg", like1.getPicture());
		Page like2 = likes.get(1);
		assertEquals("113294925350820", like2.getId());
		assertEquals("Pirates of the Caribbean", like2.getName());
		assertEquals("Movie", like2.getCategory());
		assertEquals("https://fbexternal-a.akamaihd.net/safe_image.php?d=AQCawivbIbiha8dS&w=50&h=50&url=http\u00253A\u00252F\u00252Fupload.wikimedia.org\u00252Fwikipedia\u00252Fen\u00252F6\u00252F68\u00252FPiratesDVDs.jpg&crop&fallback=hub_movie&prefix=q", like2.getPicture());
		Page like3 = likes.get(2);
		assertEquals("10264922373", like3.getId());
		assertEquals("Freebirds World Burrito", like3.getName());
		assertEquals("Food/beverages", like3.getCategory());
		assertEquals("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-ash2/50427_10264922373_2128398611_q.jpg", like3.getPicture());
	}

	private void assertGames(List<Page> games) {
		assertEquals(2, games.size());
		Page game1 = games.get(0);
		assertEquals("113744711969537", game1.getId());
		assertEquals("Super Mario Stadium Baseball", game1.getName());
		assertEquals("Games/toys", game1.getCategory());
		Page game2 = games.get(1);
		assertEquals("128165803879512", game2.getId());
		assertEquals("Disney Epic Mickey Video Game", game2.getName());
		assertEquals("Games/toys", game2.getCategory());

	}

}
