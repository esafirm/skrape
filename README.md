![OSS Skrape Banner](https://user-images.githubusercontent.com/1691440/126060801-fc14e96d-d800-4fa6-be92-eb49847eb112.gif)

## Skrape [![](https://jitpack.io/v/esafirm/skrape.svg)](https://jitpack.io/#esafirm/skrape)

Turn your HTML to JSON with graph based Kotlin DSL 💪

# Support Me!

I would make myself more commited to this repo and OSS works in general.

Would you help me achieving this goals?

<a href='https://ko-fi.com/M4M41RRE0' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://cdn.ko-fi.com/cdn/kofi4.png?v=2' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a>

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

Add to your root `build.gradle` 

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
	}
}
```

Then add the dependency

```groovy 
dependencies {
    compile 'com.github.esafirm:skrape:x.y.z'
}
```

Where `x.y.z` is the latest release (can be viewed from [Github release page](https://github.com/esafirm/skrape/releases) or Badge.

## License

[MIT](https://github.com/esafirm/skrape/blob/master/LICENSE)
     


