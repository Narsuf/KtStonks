# DELETE Watchlist
Removes a stock from the watchlist.

## Endpoint
`/watchlist/{symbol}`

### Method
`DELETE`

### Response
#### Status code
- 200 — Stock removed successfully from the watchlist.
- 400 — Missing `symbol`.
- 500 — Failed to remove stock from the watchlist.
