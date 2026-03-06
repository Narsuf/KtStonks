# GET Stocks
Returns a paginated list of stocks.

## Endpoint
`/stocks`

### Method
`GET`

### Query parameters
- `page` (integer, optional) — Page index (default 0).
- `pageSize` (integer, optional) — Page size (default 11).
- `filterWatchlist` (boolean, optional) — If true, only return watchlisted stocks.
- `symbol` (string, optional) — Filter results by symbol.

### Response
```json
{
  "items": [
    {
      "symbol": "AAPL",
      "companyName": "Apple Inc",
      "logo": "/9j/2wCEAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEB...", // Base64 encoded image
      "price": 284.15,
      "dividendYield": 0.0036,
      "incomeStatement": {
        "eps": 7.42,
        "earningsQuarterlyGrowth": 91.2,
        "revenueQuarterlyGrowth": 5.2
      },
      "analysis": {
        "earningsEstimate": {
          "growthLow": 5.56,
          "growthHigh": 11.43
        },
        "revenueEstimate": {
          "growthLow": 2.83,
          "growthHigh": 7.22
        }
      },
      "valuationMeasures": {
        "pe": 38.2951482479784,
        "pb": 52.58265,
        "ps": 8.78231,
        "valuationFloor": 12.5,
        "intrinsicValue": 119.2
      },
      "currency": "USD",
      "lastUpdated": 1764872173179,
      "isWatchlisted": false
    }
  ],
  "nextPage": 1
}
```
