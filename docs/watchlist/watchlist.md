# GET Watchlist
Returns a paginated list of watchlisted stocks.

## Endpoint
`/watchlist`

### Method
`GET`

### Query parameters
- `page` (integer, optional) — Page index (default 0).
- `pageSize` (integer, optional) — Page size (default 11).

### Response
```json
{
  "items": [
    {
      "symbol": "AAPL",
      "companyName": "Apple Inc",
      "logo": "/9j/2wCEAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEB...", // Base64 encoded image
      "logoUrl": "https://logo.clearbit.com/apple.com",
      "price": 284.15,
      "dividendYield": 0.0036,
      "eps": 7.42,
      "pe": 38.2951482479784,
      "pb": 52.58265,
      "earningsQuarterlyGrowth": 91.2,
      "expectedEpsGrowth": 7.74,
      "valuationFloor": 12.5,
      "currentIntrinsicValue": 119.2,
      "forwardIntrinsicValue": 133.5,
      "currency": "USD",
      "lastUpdated": 1764872173179,
      "isWatchlisted": false
    }
  ],
  "nextPage": 1
}
```
