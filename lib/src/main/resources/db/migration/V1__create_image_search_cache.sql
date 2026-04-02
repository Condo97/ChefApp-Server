CREATE TABLE IF NOT EXISTS image_search_cache (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    query_hash VARCHAR(64) NOT NULL,
    query_text VARCHAR(500) NOT NULL,
    image_urls TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    UNIQUE KEY idx_query_hash (query_hash)
);
