package cn.xuhuiqiang.string;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.JsonObject;

public class TestGson {

	@Test
	public void test() {
		String content = "{name:\"fdipzone\"}";
		JsonObject obj = GsonParse.toJson(content);
		assertNotNull(obj);
		System.out.println(obj.get("name").getAsString());
	}

}
