## Specification

1. Skrape default result is JSON
2. Skrape query can be exported and imported to/from readable text format 

## Example

Skrape

```groovy
page("https://example.com"){
  "items" to query("td.title"){
    "title" to text()
    "link" to attr("href")
    "others" to child {
      "liked" to text().toBoolean()
      "count" to attr("count").toInt()
   }
 } 
```

JSON

```json
{
 "items": [
   {
     "title":"Hellow",
     "link":"https://example.com/1",
     "others": {
       "liked": true,
       "count": 10
     }
   }
 ]
}
```