# POST Stock Valuation
Updates or sets a custom valuation for a given stock symbol.

## Endpoint
`/stock/{symbol}/valuation`

### Method
`POST`

### Query parameters
- `valuationFloor` (double, required) — Manual valuation floor (P/S) to apply.

### Response
#### Status code
- 200 — Valuation successfully saved.
- 400 — Missing `valuationFloor`.
- 500 — Failed to save stock.
