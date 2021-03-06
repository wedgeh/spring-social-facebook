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

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.social.ApiException;
import org.springframework.social.InsufficientPermissionException;
import org.springframework.social.MissingAuthorizationException;

/**
 * Interface defining operations that can be performed on a Facebook pages.
 * @author Craig Walls
 */
public interface PageOperations {
	
	/**
	 * Retrieves data for a page.
	 * @param pageId the page ID.
	 * @return a {@link Page}
	 */
	Page getPage(String pageId);

    /**
     * Retrieves data for a page including the page metadata.
     * 
     * @param pageId the page ID.
     * @return a {@link Page}
     */
    Page getPageWithMetadata(String pageId);
	
	/**
	 * Checks whether the logged-in user for this session is an admin of the page with the given page ID.
	 * Requires "manage_pages" permission.
	 * @param pageId the page ID
	 * @return true if the authenticated user is an admin of the specified page.
	 * @throws ApiException if there is an error while communicating with Facebook.
	 * @throws InsufficientPermissionException if the user has not granted "manage_pages" permission.
	 * @throws MissingAuthorizationException if FacebookTemplate was not created with an access token.
	 */	
	boolean isPageAdmin(String pageId);
	
	/**
	 * Retrieves a list of Account objects for the pages that the authenticated user is an administrator.
	 * Requires "manage_pages" permission.
	 * @return a paged list of accounts
	 */
	PagedList<Account> getAccounts();
	
	/**
	 * Posts a message to a page's feed as a page administrator.
	 * Requires that the application is granted "manage_pages" permission and that the authenticated user be an administrator of the page.
	 * To post to the page's feed as the authenticated user, use {@link FeedOperations#post(String, String)} instead.
	 * @param pageId the page ID
	 * @param message the message to post
	 * @return the ID of the new feed entry
	 * @throws ApiException if there is an error while communicating with Facebook.
	 * @throws InsufficientPermissionException if the user has not granted "manage_pages" permission.
	 * @throws PageAdministrationException if the user is not a page administrator.
	 * @throws MissingAuthorizationException if FacebookTemplate was not created with an access token.
	 */
	String post(String pageId, String message);
	
	/**
	 * Posts a link to the page's feed as a page administrator.
	 * Requires that the application is granted "manage_pages" permission and that the authenticated user be an administrator of the page.
	 * To post a link to the page's feed as the authenticated user, use {@link FeedOperations#postLink(String, String, FacebookLink)} instead.
	 * @param pageId the page ID
	 * @param message a message to send with the link.
	 * @param link the link details
	 * @return the ID of the new feed entry.
	 * @throws ApiException if there is an error while communicating with Facebook.
	 * @throws InsufficientPermissionException if the user has not granted "manage_pages" permission.
	 * @throws PageAdministrationException if the user is not a page administrator.
	 * @throws MissingAuthorizationException if FacebookTemplate was not created with an access token.
	 */
	String post(String pageId, String message, FacebookLink link);

	/**
	 * Posts a photo to a page's album as the page administrator.
	 * Requires that the application is granted "manage_pages" permission and that the authenticated user be an administrator of the page.
	 * @param pageId the page ID
	 * @param albumId the album ID
	 * @param photo A {@link Resource} for the photo data. The given Resource must implement the getFilename() method (such as {@link FileSystemResource} or {@link ClassPathResource}).
	 * @return the ID of the photo.
	 * @throws ApiException if there is an error while communicating with Facebook.
	 * @throws InsufficientPermissionException if the user has not granted "manage_pages" permission.
	 * @throws PageAdministrationException if the user is not a page administrator.
	 * @throws MissingAuthorizationException if FacebookTemplate was not created with an access token.
	 */
	String postPhoto(String pageId, String albumId, Resource photo);

	/**
	 * Posts a photo to a page's album as the page administrator.
	 * Requires that the application is granted "manage_pages" permission and that the authenticated user be an administrator of the page.
	 * @param pageId the page ID
	 * @param albumId the album ID
	 * @param photo A {@link Resource} for the photo data. The given Resource must implement the getFilename() method (such as {@link FileSystemResource} or {@link ClassPathResource}).
	 * @param caption A caption describing the photo.
	 * @return the ID of the photo.
	 * @throws ApiException if there is an error while communicating with Facebook.
	 * @throws InsufficientPermissionException if the user has not granted "manage_pages" permission.
	 * @throws PageAdministrationException if the user is not a page administrator.
	 * @throws MissingAuthorizationException if FacebookTemplate was not created with an access token.
	 */
	String postPhoto(String pageId, String albumId, Resource photo, String caption);
	
	/**
	 * Searches for pages for places near a given coordinate.
	 * @param query the search query (e.g., "Burritos")
	 * @param latitude the latitude of the point to search near
	 * @param longitude the longitude of the point to search near
	 * @param distance the radius to search within (in feet)
	 * @return a list of {@link Page}s matching the search
	 * @throws ApiException if there is an error while communicating with Facebook.
	 * @throws MissingAuthorizationException if FacebookTemplate was not created with an access token.
	 */
	PagedList<Page> search(String query, double latitude, double longitude, long distance);

}
