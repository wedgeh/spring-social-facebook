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

import java.util.List;
import java.util.Map;

/**
 * Model class representing a Facebook page.
 * A Facebook page could represent any number of things, including businesses, government agencies, people, organizations, etc.
 * A page may even represent a place that a user may check into using Facebook Places, if the page has location data.
 * The data available for a page will vary depending on the category it belongs to and what data the page administrator has entered.
 * @author Craig Walls
 */
public class Page extends FacebookObject {

	private String id;

	private String about;
	
	private String affiliation;
	
	private String attire;
	
	private String bandMembers;
	
	private Page bestPage;
	
	private String birthday;
	
	private String bookingAgent;
	
	private boolean canPost;
	
	private String category;
	
	private List<Reference> categoryList;

	private int checkins;
	
	private String companyOverview;
	
	private CoverPhoto cover;
	
	private String currentLocation;
	
	private String description;
	
	private String directedBy;
	
	private String founded;
	
	private String generalInfo;
	
	private String generalManager;
	
	private String globalBrandPageName;
	
	private boolean hasAddedApp;
	
	private String hometown;
	
	private Map<String, String> hours;
	
	private boolean isCommunityPage;
	
	private boolean isPermanentlyClosed;
	
	private boolean isPublished;
	
	private boolean isUnclaimed;
	
	private int likes;
	
	private String link;

	private Location location;
	
	private String mission;
	
	private String name;
	
	private ParkingInfo parking;
	
	private String phone;
	
	private String picture;
	
	private String pressContact;
	
	private PriceRange priceRange;
	
	private String products;
	
	private RestaurantSpecialties restaurantSpecialties;
	
	private int talkingAboutCount;
	
	private String website;
	
	private int wereHereCount;

    private Metadata metadata;

	/**
	 * @return The page's ID.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return The page's name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return Link to the page on Facebook.
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @return The page's category.
	 */
	public String getCategory() {
		return category;
	}
	
	/**
	 * @return A description of this page.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @return Text telling what this page is about.
	 */
	public String getAbout() {
		return about;
	}
	
	/**
	 * @return The page's street address and longitude/latitude coordinates (if available).
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @return Link to the external website for the page.
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * @return the page's picture.
	 * @deprecated This method will be replaced in Spring 1.1.0 with a new version that returns an object with more details about the picture.
	 */
	@Deprecated
	public String getPicture() {
		return picture;
	}
	
	/**
	 * @return the page's cover photo.
	 */
	public CoverPhoto getCover() {
		return cover;
	}

	/**
	 * @return The phone number for the page.
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @return The name of an organization that this page is affiliated with.
	 */
	public String getAffiliation() {
		return affiliation;
	}

	/**
	 * @return Text giving a brief description of the company this page serves.
	 */
	public String getCompanyOverview() {
		return companyOverview;
	}
	
	/**
	 * @return The number of users who like this page. For Global Brand pages, this count is fall all pages across the brand.
	 */
	public int getLikes() {
		return likes;
	}
	
	
	public int getTalkingAboutCount() {
		return talkingAboutCount;
	}

	/**
	 * @return The total number of users that have checked in to the page.
	 */
	public int getCheckins() {
		return checkins;
	}

	/**
	 * Indicates whether or not the authenticated user can post on this page.
	 * @return true if the user can post to the page; false otherwise
	 */
	public boolean canPost() {
		return canPost;
	}
	
	/**
	 * @return true if the page has been published; false otherwise.
	 */
	public boolean isPublished() {
		return isPublished;
	}
	
	/**
	 * @return true if the page is a community page; false otherwise.
	 */
	public boolean isCommunityPage() {
		return isCommunityPage;
	}
	
	/**
	 * @return true if the page has added the app making the query in a Page tab; false otherwise.
	 */
	public boolean hasAddedApp() {
		return hasAddedApp;
	}

	public Map<String, String> getHours() {
		return hours;
	}
	
	public String getAttire() {
		return attire;
	}
	
	public String getBandMembers() {
		return bandMembers;
	}
	
	public Page getBestPage() {
		return bestPage;
	}
	
	public String getBookingAgent() {
		return bookingAgent;
	}
	
	public String getBirthday() {
		return birthday;
	}
	
	public List<Reference> getCategoryList() {
		return categoryList;
	}
	
	public String getCurrentLocation() {
		return currentLocation;
	}
	
	public String getDirectedBy() {
		return directedBy;
	}
	
	public String getFounded() {
		return founded;
	}
	
	public String getGeneralInfo() {
		return generalInfo;
	}
	
	public String getGeneralManager() {
		return generalManager;
	}
	
	public String getGlobalBrandPageName() {
		return globalBrandPageName;
	}
	
	public String getHometown() {
		return hometown;
	}
	
	public boolean isPermanentlyClosed() {
		return isPermanentlyClosed;
	}
	
	public boolean isUnclaimed() {
		return isUnclaimed;
	}
	
	public String getMission() {
		return mission;
	}
	
	public ParkingInfo getParking() {
		return parking;
	}
	
	public PriceRange getPriceRange() {
		return priceRange;
	}
	
	public String getPressContact() {
		return pressContact;
	}
	
	public String getProducts() {
		return products;
	}
	
	public RestaurantSpecialties getRestaurantSpecialties() {
		return restaurantSpecialties;
	}
	
	public int getWereHereCount() {
		return wereHereCount;
	}
	
    /**
     * @return the metadata
     */
    public Metadata getMetadata() {
        return metadata;
    }

    public static enum PriceRange {
		$,$$, $$$, $$$$, UNSPECIFIED;
	}
	
}
