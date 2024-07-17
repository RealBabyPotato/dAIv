import com.google.gson.*;

import java.lang.reflect.Type;

public class EventDeserializer implements JsonDeserializer<Event> {
    @Override
    public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (jsonObject.has("remind")) {
            //return context.deserialize(json, Reminder.class);
            return new ReminderDeserializer().deserialize(json, Reminder.class, context);
        } else {
            return context.deserialize(json, Event.class);
        }
    }
}

class ReminderDeserializer implements JsonDeserializer<Reminder> {
    @Override
    public Reminder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String remind = jsonObject.get("remind").getAsString();
        long startTime = jsonObject.get("startTime").getAsLong();
        long expiryTime = jsonObject.get("expiryTime").getAsLong();
        long repeatInterval = jsonObject.get("repeatInterval").getAsLong();

        return new Reminder(startTime, expiryTime, remind, repeatInterval);
    }
}