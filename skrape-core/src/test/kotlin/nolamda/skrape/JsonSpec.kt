package nolamda.skrape

import com.github.salomonbrys.kotson.jsonObject
import io.kotlintest.specs.StringSpec

class JsonSpec : StringSpec({

    val mainJson = jsonObject(
        "items" to jsonObject(
            "title" to "Title 1",
            "detail" to "This is the detail"
        )
    )

    "json is valid" {
        println(mainJson.toString())
    }
})