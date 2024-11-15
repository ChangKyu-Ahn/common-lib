package co.kr.common.mapper;

import co.kr.common.code.BaseEnumCode;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;
import java.io.IOException;
import java.util.Arrays;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class CustomObjectMapper {

	public static ObjectMapper getObjectMapper() {
		return builder().build();
	}

	private static Jackson2ObjectMapperBuilder builder() {
		return Jackson2ObjectMapperBuilder
			.json()
			.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
			.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.featuresToEnable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
			.modules(new JavaTimeModule(), customModule(), new BlackbirdModule());
	}

	private static SimpleModule customModule() {
		SimpleModule simpleModule = new SimpleModule();

		// enum 데이터를 객체 형태로 리턴
		simpleModule.addSerializer(Enum.class, new StdSerializer<>(Enum.class) {
			@Override
			public void serialize(Enum value, JsonGenerator gen, SerializerProvider provider)
					throws IOException {
				if (value instanceof BaseEnumCode) {
					gen.writeStartObject();
					gen.writeStringField("name", value.name());
					gen.writeObjectField("code", ((BaseEnumCode) value).getCode());
					gen.writeObjectField("desc", ((BaseEnumCode) value).getDesc());
					gen.writeEndObject();
				} else {
					gen.writeString(value.name());
				}
			}
		});

		// String 필드에 enum 또는 string 데이터 들어오는 경우 처리
		simpleModule.addDeserializer(String.class, new StdDeserializer<>(String.class) {
			@Override
			public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
				final JsonNode jsonNode = p.readValueAsTree();

				return Strings.trimToNull(
						jsonNode.has("name") ? jsonNode.get("code").asText() : jsonNode.asText());
			}
		});

		// enum 필드에 enum 또는 string 데이터가 들어오는 경우 데이터 처리
		simpleModule.setDeserializerModifier(new BeanDeserializerModifier() {
			@Override
			public JsonDeserializer<Enum<?>> modifyEnumDeserializer(DeserializationConfig config,
					final JavaType type,
					BeanDescription beanDesc,
					final JsonDeserializer<?> deserializer) {
				return new JsonDeserializer<>() {
					@Override
					@SuppressWarnings("unchecked")
					public Enum<?> deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
						Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) type.getRawClass();
						final JsonNode jsonNode = p.readValueAsTree();

						boolean isEnumData = jsonNode.has("name");

						final String value = isEnumData ? jsonNode.get("code").asText() : jsonNode.asText();

						return Arrays.stream(enumClass.getEnumConstants())
								.filter(e -> {
									if (e instanceof BaseEnumCode) {
										return e.name().equalsIgnoreCase(value) || String.valueOf(
												((BaseEnumCode<?>) e).getCode()).equalsIgnoreCase(value);
									} else {
										return e.name().equalsIgnoreCase(value);
									}
								}).findFirst().orElse(null);
					}
				};
			}
		});

		return simpleModule;
	}
}
