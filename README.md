## Skrape

Turn your HTML to JSON with graph based Kotlin DSL 💪

## Getting Started

Define your query in type-safe Kotlin DSL  

```kotlin
Page("https://news.ycombinator.com/") {
  "items" to query("td a.storylink") {
    "text" to text()
    "info" to container {
      "link" to attr("href")
    }
  }
}.run {
  Skrape(JsoupDocumentParser()).request(this)
}
```
To predictable JSON result

```javascript
{
    "items": [
        {
            "text": "SFO near miss could have triggered \u2018greatest aviation disaster in history'",
            "detail": {
                "link": "http://www.mercurynews.com/2017/07/10/exclusive-sfo-near-miss-might-have-triggered-greatest-aviation-disaster-in-history/"
            }
        },
        {
            "text": "Taking control of all .io domains with a targeted registration",
            "detail": {
                "link": "https://thehackerblog.com/the-io-error-taking-control-of-all-io-domains-with-a-targeted-registration/"
            }
        }
    ]
    ...
}
```

## Binaries

Coming soon

## License

[MIT](https://github.com/esafirm/skrape/blob/master/LICENSE)
     


