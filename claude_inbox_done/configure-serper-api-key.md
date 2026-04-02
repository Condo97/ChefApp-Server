# Configure Serper.dev API Key

**Priority:** IMMEDIATE — the `/v1/searchImages` endpoint is live but won't work without this key.

Set the environment variable on production:

```
PANTRYPRO_SERPER_API_KEY=96015c51192c20160d6bf9f66b17adacfad8b3b6
```

If using `.env` file, add it there. If using systemd or docker-compose, add to the environment config.

Delete this file after configuring.
