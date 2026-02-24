# POST Watchlist
Adds a stock to the watchlist.

## Endpoint
`/watchlist/{symbol}`

### Method
`POST`

### Response
#### Status code
- 200 — Stock added successfully to the watchlist.
- 400 — Missing `symbol`.
- 500 — Failed to add stock to the watchlist.
