# GET Stock details
Returns all the information available from a specific stock.

## Endpoint
`/stock/{symbol}`

### Method
`GET`

### Response
```json
{
  "symbol": "AAPL",
  "companyName": "Apple Inc",
  "logoUrl": "https://logo.clearbit.com/apple.com",
  "price": 284.15,
  "dividendYield": 0.0036,
  "eps": 7.42,
  "pe": 38.2951482479784,
  "earningsQuarterlyGrowth": 91.2,
  "expectedEpsGrowth": null,
  "currentIntrinsicValue": 9275,
  "forwardIntrinsicValue": null,
  "currency": "USD",
  "lastUpdated": 1764872173179
}
```
