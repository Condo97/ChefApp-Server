package com.pantrypro.database.dao;

import java.sql.*;
import java.time.LocalDateTime;

public class ImageSearchCacheDAO {

    /**
     * Gets cached image URLs for a given query hash, only if the cache entry has not expired.
     *
     * @param conn      Database connection
     * @param queryHash SHA-256 hash of the normalized query
     * @return The cached image_urls JSON string, or null if not found or expired
     */
    public static String getCachedImageUrls(Connection conn, String queryHash) throws SQLException {
        String sql = "SELECT image_urls FROM image_search_cache WHERE query_hash = ? AND expires_at > NOW()";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, queryHash);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("image_urls");
                }
                return null;
            }
        }
    }

    /**
     * Inserts or updates a cache entry for image search results.
     *
     * @param conn           Database connection
     * @param queryHash      SHA-256 hash of the normalized query
     * @param queryText      The original query text
     * @param imageUrlsJson  JSON array string of image URLs
     * @param ttlDays        Number of days until the cache entry expires
     */
    public static void cacheImageUrls(Connection conn, String queryHash, String queryText, String imageUrlsJson, long ttlDays) throws SQLException {
        String sql = "INSERT INTO image_search_cache (query_hash, query_text, image_urls, expires_at) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE image_urls = VALUES(image_urls), query_text = VALUES(query_text), expires_at = VALUES(expires_at)";

        LocalDateTime expiresAt = LocalDateTime.now().plusDays(ttlDays);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, queryHash);
            ps.setString(2, queryText);
            ps.setString(3, imageUrlsJson);
            ps.setTimestamp(4, Timestamp.valueOf(expiresAt));

            ps.executeUpdate();
        }
    }

}
