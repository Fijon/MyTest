package cn.xuhuiqiang.string;

import java.io.StringReader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class GsonParse {

	public static JsonObject toJson(String content) {
		JsonObject jsonObject = null;
		try {
			JsonReader reader = new JsonReader(new StringReader(content));
			reader.setLenient(true);
			jsonObject = new JsonParser().parse(reader).getAsJsonObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

}
