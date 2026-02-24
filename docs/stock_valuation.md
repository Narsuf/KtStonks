# POST Stock Valuation
Updates or sets a custom valuation for a given stock symbol.

## Endpoint
`/stock/{symbol}/valuation`

### Method
`POST`

### Query parameters
- `epsGrowth` (double, required) — EPS growth percentage used for valuation calculation.
- `valuationFloor` (double, optional) — Manual valuation floor (P/E) to apply.

### Response
#### Status code
- 200 — Valuation successfully saved.
- 400 — `epsGrowth` missing.
- 500 — Failed to save stock.
