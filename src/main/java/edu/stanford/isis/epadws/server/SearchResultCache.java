/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.server;

import edu.stanford.isis.epadws.common.DicomSearchType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Caches SearchResults.
 *
 * @author amsnyder
 */
public class SearchResultCache {
    private static SearchResultCache ourInstance = new SearchResultCache();

    final Map<SearchKey,SearchResult> cache = new ConcurrentHashMap<SearchKey,SearchResult>();
    final Map<SearchKey,Long> cacheTimestamp = new ConcurrentHashMap<SearchKey,Long>();

    final Map<UserKey,SearchResult> mostRecent = new ConcurrentHashMap<UserKey,SearchResult>();

    public static SearchResultCache getInstance() {
        return ourInstance;
    }

    private SearchResultCache() {}

    /**
     * Cache a new SearchResult
     * @param searchResult
     * @param searchType
     * @param searchParam
     */
    public void cache(SearchResult searchResult, DicomSearchType searchType, String searchParam){
        SearchKey key = new SearchKey(searchType,searchParam);
        cache.put(key,searchResult);
        cacheTimestamp.put(key,System.currentTimeMillis());
    }

    /**
     * Set the most recent search result for this user.
     * @param searchResult
     * @param remoteAddr
     */
    public void setMostRecent(SearchResult searchResult,String remoteAddr){
        UserKey key = new UserKey(remoteAddr);
        mostRecent.put(key,searchResult);
    }

    /**
     *
     * @param remoteAddr
     * @return
     */
    public SearchResult getMostRecent(String remoteAddr){
        UserKey key = new UserKey(remoteAddr);
        return mostRecent.get(key);
    }

    /**
     * Look to see if a similar search has occurred recently.
     * @param searchType
     * @param searchParam
     * @return
     */
    public SearchResult find(DicomSearchType searchType, String searchParam){
        SearchKey key = new SearchKey(searchType,searchParam);
        //check the time-to-live of this request.
        Long timestamp = cacheTimestamp.get(key);
        if(timestamp==null){
            return null;
        }
        if( exceedsCacheTime(timestamp) ){
            cacheTimestamp.remove(key);
            cache.remove(key);
            return null;
        }
        return cache.get(key);
    }

    private boolean exceedsCacheTime(Long timestamp){
        long currTime = System.currentTimeMillis();
        long FIVE_MIN_IN_MILLISECONDS = 5 * 60 * 1000;

        if( currTime-FIVE_MIN_IN_MILLISECONDS>timestamp ){
            return true; //exceeds time limit.
        }
        return false;
    }


    class SearchKey {

        final DicomSearchType type;
        final String param;

        public SearchKey(DicomSearchType searchType, String searchParam){
            type=searchType;
            param=searchParam;
        }

        @Override
        public boolean equals(Object o){
            if(o==this) return true;
            if(!(o instanceof SearchKey)) return false;

            SearchKey sk = (SearchKey) o;

            return sk.type==type && sk.param.equals(param);
        }

        @Override
        public int hashCode(){
            int result=13;
            result = 17*result+type.hashCode();
            result = 19*result+param.hashCode();
            return result;
        }

    }

    /**
     * Key used to identify an unique user.
     */
    class UserKey {

        final String userId;

        public UserKey(String remoteAddr){
            userId = remoteAddr;
        }

        @Override
        public boolean equals(Object o){
            if(o==this) return true;
            if(!(o instanceof UserKey)) return false;

            UserKey uk = (UserKey) o;

            return userId.equals(uk.userId);
        }

        @Override
        public int hashCode(){
            return userId.hashCode()*7;
        }
    }//class UserKey

    /**
     * Use for debugging.
     * @return String
     */
    @Override
    public String toString(){

        int nCache = cache.size();
        int nTimestamp = cacheTimestamp.size();
        int nMostRecent = mostRecent.size();

        StringBuilder sb = new StringBuilder();
        sb.append(", cache.size: ").append(nCache);
        sb.append(", timestamp.size: ").append(nTimestamp);
        sb.append(", mostRecent.size: ").append(nMostRecent);

        return sb.toString();
    }

}
