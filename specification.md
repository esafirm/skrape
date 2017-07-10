## Specification

1. kgdom default result is JSON
2. kgdom query can be exported and imported to/from readable text format 

## Example

kgdom

```
page("https://example.com"){
  "items" to query("td.title"){
    "title" to text(),
    "link" to attr("href")
    "others" to child {
      "liked" to text().toBoolean(),
      "count" to text().toInt()
   }
 } 
```

json

```
{
 "items": [
   {
     "title":"Hellow",
     "link":"https://example.com/1",
     "others": {
       "liked": true,
       "count" 10
     }
   }
 ]
}
```