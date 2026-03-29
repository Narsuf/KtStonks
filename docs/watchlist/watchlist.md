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
      "price": 284.15,
      "dividends": {
        "dividendYield": 0.0036,
        "payoutRatio": 0.1476
      },
      "roe": 1.5202099,
      "profitMargin": 0.27037,
      "incomeStatement": {
        "eps": 7.42,
        "earningsQuarterlyGrowth": 91.2,
        "revenueQuarterlyGrowth": 5.2
      },
      "analysis": {
        "earningsEstimate": {
          "growthLow": 5.56,
          "growthHigh": 11.43,
          "growthAvg": 8.65
        },
        "revenueEstimate": {
          "growthLow": 2.83,
          "growthHigh": 7.22,
          "growthAvg": 5.54
        }
      },
      "valuationMeasures": {
        "pe": 38.2951482479784,
        "valuationFloor": 12.5,
        "intrinsicValue": 119.2
      },
      "balanceSheet": {
        "totalCashPerShare": 4.557,
        "de": 102.63,
        "currentRatio": 1.5
      },
      "currency": "USD",
      "lastUpdated": 1764872173179,
      "isWatchlisted": true
    }
  ],
  "nextPage": 1
}
```
