package com.pantrypro.database.dao.pooled;

import com.pantrypro.connectionpool.SQLConnectionPoolInstance;
import com.pantrypro.database.dao.ImageSearchCacheDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class ImageSearchCacheDAOPooled {

    /**
     * Gets cached image URLs for a given query hash, using a pooled connection.
     *
     * @param queryHash SHA-256 hash of the normalized query
     * @return The cached image_urls JSON string, or null if not found or expired
     */
    public static String getCachedImageUrls(String queryHash) throws SQLException, InterruptedException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            return ImageSearchCacheDAO.getCachedImageUrls(conn, queryHash);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    /**
     * Inserts or updates a cache entry for image search results, using a pooled connection.
     *
     * @param queryHash      SHA-256 hash of the normalized query
     * @param queryText      The original query text
     * @param imageUrlsJson  JSON array string of image URLs
     * @param ttlDays        Number of days until the cache entry expires
     */
    public static void cacheImageUrls(String queryHash, String queryText, String imageUrlsJson, long ttlDays) throws SQLException, InterruptedException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            ImageSearchCacheDAO.cacheImageUrls(conn, queryHash, queryText, imageUrlsJson, ttlDays);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

}
