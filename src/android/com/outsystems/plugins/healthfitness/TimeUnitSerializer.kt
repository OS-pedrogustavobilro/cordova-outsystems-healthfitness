import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.outsystems.plugins.healthfitness.data.HealthEnumTimeUnit
import java.lang.reflect.Type

class TimeUnitSerializer(
    private val onDeprecatedFound: () -> Unit
) : JsonSerializer<HealthEnumTimeUnit>, JsonDeserializer<HealthEnumTimeUnit> {

    // How to write Status to JSON
    override fun serialize(
        src: HealthEnumTimeUnit?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.name)
    }

    // How to read Status from JSON
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): HealthEnumTimeUnit {
        return when (val value = json?.asString) {
            "SECONDS", "MILLISECONDS" -> {
                onDeprecatedFound()
                HealthEnumTimeUnit.MINUTE
            }
            "MINUTE" -> HealthEnumTimeUnit.MINUTE
            "HOUR" -> HealthEnumTimeUnit.HOUR
            "DAY" -> HealthEnumTimeUnit.DAY
            "WEEK" -> HealthEnumTimeUnit.WEEK
            "MONTH" -> HealthEnumTimeUnit.MONTH
            "YEAR" -> HealthEnumTimeUnit.YEAR
            else -> throw JsonParseException("Unknown time unit: $value")
        }
    }
}