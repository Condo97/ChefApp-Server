# New Endpoint: POST /v1/searchImages

**Priority:** HIGH — iOS app has been updated to call this endpoint instead of Bing Image Search directly. Image generation is broken until this endpoint is live.

## Purpose
Replace direct Bing Image Search from iOS client with a server-proxied image search using Serper.dev (Google Images API) with server-side caching to minimize API costs.

## Why
- Bing API key expired/expensive
- Server-side caching reduces API costs dramatically (same queries cached for 30 days)
- Serper.dev uses Google Images index — widest breadth for specific cuisines
- Cost: ~$1/1K queries after 2,500 free queries

## iOS Client Contract

### Request
```json
POST /v1/searchImages
{
    "authToken": "string",
    "query": "string",
    "count": 4
}
```

### Response
```json
{
    "Success": 1,
    "Body": {
        "imageURLs": [
            "https://example.com/image1.jpg",
            "https://example.com/image2.jpg"
        ]
    }
}
```

**IMPORTANT:** The iOS client expects exactly this response format. `Success` and `Body` are capitalized (matching existing BodyResponse pattern). `imageURLs` is camelCase.

## Implementation Steps

### 1. Create image_search_cache table
```sql
CREATE TABLE image_search_cache (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    query_hash VARCHAR(64) NOT NULL,
    query_text VARCHAR(500) NOT NULL,
    image_urls TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    UNIQUE KEY idx_query_hash (query_hash)
);
```
- `query_hash`: SHA-256 of lowercase trimmed query
- `image_urls`: JSON array of URL strings
- `expires_at`: created_at + 30 days

### 2. Create request/response DTOs
- `SearchImagesRequest`: authToken (String), query (String), count (int)
- `SearchImagesResponse`: extends BodyResponse with body containing `imageURLs` (List<String>)

### 3. Create SerperClient
Call Serper.dev API:
```
POST https://google.serper.dev/images
Headers:
  X-API-KEY: <serper_api_key>
  Content-Type: application/json
Body:
  {"q": "<query> food recipe", "num": <count>}
```

Response format from Serper.dev:
```json
{
  "images": [
    {
      "title": "...",
      "imageUrl": "https://...",
      "imageWidth": 1200,
      "imageHeight": 800,
      "thumbnailUrl": "https://...",
      "source": "...",
      "domain": "...",
      "link": "...",
      "position": 1
    }
  ]
}
```

Extract `imageUrl` from each result in the `images` array.

**IMPORTANT:** Append " food recipe" to the query to bias results toward food photography.

### 4. Create SearchImagesEndpoint
Implement `Endpoint<SearchImagesRequest>`:

```
Flow:
1. Validate authToken via UserAuthenticator
2. Normalize query: lowercase, trim whitespace
3. Hash query with SHA-256
4. Check image_search_cache for non-expired entry with matching query_hash
5. If cache HIT: return cached image_urls
6. If cache MISS:
   a. Call Serper.dev API with query + " food recipe"
   b. Extract imageUrl from each result
   c. INSERT into image_search_cache (ON DUPLICATE KEY UPDATE to handle races)
   d. Return image URLs
```

### 5. Register endpoint
In `Main.java`:
```java
post(apiVersion + "/searchImages", new SearchImagesEndpoint());
```

### 6. Configuration
Add Serper.dev API key to Keys.java:
```java
public static final String serperApiKey = "YOUR_KEY_HERE";
```

Get a free API key at https://serper.dev (2,500 free queries to start, then ~$1/1K queries).

### 7. Error handling
- If Serper.dev fails, return error code 60
- If cache read fails, proceed without cache (degrade gracefully)
- Log all Serper API calls with query text for cost monitoring
- Return empty imageURLs array (not error) if Serper returns no results

## Testing
- Test with query "chicken parmesan" — should return food image URLs
- Test cache hit — second identical query should NOT call Serper (check logs)
- Test with exotic query "Ethiopian injera with doro wat" — should return relevant results
- Test auth validation — request without valid token should fail
- Test empty query — should return error
