## Skrape

Turn your HTML to JSON with graph based Kotlin DSL 💪

## Getting Started

Define your query in type-safe Kotlin DSL  

```kotlin
Page("https://news.ycombinator.com/") {
  "items" to query("td a.storylink") {
    "text" to text()
    "link" to attr("href")
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
            "text": "Awk driven IoT",
            "link": "https://anisse.astier.eu/awk-driven-iot.html"
        },
        {
            "text": "Soti \u2013 A B firm built in a basement",
            "link": "http://www.bbc.com/news/business-40504764"
        },
        ...
    ]
}
```

## License

[MIT](https://github.com/esafirm/skrape/blob/master/LICENSE)
     


