# Getting Started

### Run application
./gradlew bootRun

### Run tests
./gradlew test

### Additional information
The application has methods to build Candlesticks from a given list of trades.

Tests verify the consistency of the Crypto.com Trades API. It fetches trades and candlesticks from the 'uat-api.3ona.co' environment.
It builds Candlesticks from the obtained Trades and compares them against the Candlesticks from the Crypto.com API.

Tests are run for the BTC_USDT and CRO_USDT instruments across all known intervals except for the TWO_WEEKS interval because of unclear specifications when the starting point for two weeks is.

#### CryptoTradeApiValidationTests
There is a Parameterized test that takes in three arguments - instrument name, interval and a flag which controls whether to ignore the cases where Crypto.com API returns insufficient number of trades, i.e. there are Candlesticks without matching Trades.

A number of tests fail when the 'validateMissingTrades' flag is true because of inconsistencies between the Candlesticks and the Trade count.
There are also a few tests that fail for the ONE_MONTH interval. The assumption is that the ONE_MONTH interval starts on the 1st of the month. However, some data from the Crypto.com API proves that this assumption is false.

The SEVEN_DAYS interval test errors are legit. The BTC_USDT is breaking due to a 'volume' mismatch. The CRO_USDT is breaking due to a 'low price' mismatch.

#### Documentation
There is a mismatch between the API Documentation for 'public/get-trades' endpoint and actual data seen on 'https://uat-api.3ona.co/v2/public/get-trades'. There is additional 'result' wrapper above 'data' node on the 'uat-api' environment.

###### Documentation Response Sample
```
{
  "code":0,
  "method":"public/get-trades",
  "result": [
    {"dataTime":1591710781947,"d":465533583799589409,"s":"BUY","p":2.96,"q":16.0,"t":1591710781946,"i":"ICX_CRO"},
    {"dataTime":1591707701899,"d":465430234542863152,"s":"BUY","p":0.007749,"q":115.0,"t":1591707701898,"i":"VET_USDT"},
    ...
    ...
  ]
}
```

###### UAT-API Response Sample
```
{
  "code": 0,
  "method": "public/get-trades",
  "result": {
    "instrument_name": "BTC_USDT",
    "data": [
	  {"dataTime": 1649521513631,"d": 2405339848939827648,"s": "SELL","p": 10013.00,"q": 1.000000,"t": 1649521513630,"i": "BTC_USDT"},
	  {"dataTime": 1649516704135,"d": 2405178468980222432,"s": "SELL","p": 10010.00,"q": 1.000000,"t": 1649516704132,"i": "BTC_USDT"},
	  ...
	  ...
    ]
  }
}
```