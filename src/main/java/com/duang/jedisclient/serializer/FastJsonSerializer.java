package com.duang.jedisclient.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.duang.jedisclient.common.CacheException;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.util.SafeEncoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * FstSerializer.
 */
public class FastJsonSerializer implements ISerializer {

	private static final FastJsonConfig fastJsonConfig = new FastJsonConfig();

	public byte[] serializerKey(String key) {
		return SafeEncoder.encode(key);
	}

	public byte[] serializerField(String key) {
		return serializerValue(key);
	}

	public String deSerializerKey(byte[] bytes) {
		return SafeEncoder.encode(bytes);
	}

	public byte[] serializerValue(Object value) {
		if (null == value) {
			return new byte[0];
		}
		try {
			return JSON.toJSONBytesWithFastJsonConfig(
					fastJsonConfig.getCharset(),
					value,
					fastJsonConfig.getSerializeConfig(),
					fastJsonConfig.getSerializeFilters(),
					fastJsonConfig.getDateFormat(),
					JSON.DEFAULT_GENERATE_FEATURE,
					fastJsonConfig.getSerializerFeatures()
			);
		} catch (Exception e) {
			throw new CacheException("FastJsonSerializer将值序列化成Byte时出错: " + e.getMessage(), e);
		}
	}
	
	public <T> T deSerializerValue(byte[] bytes, Class<T> type) {
		if(bytes == null || bytes.length == 0) {
			return null;
		}
		try {
			return (T) JSON.parseObject(
					bytes,
					fastJsonConfig.getCharset(),
					type,
					fastJsonConfig.getParserConfig(),
					fastJsonConfig.getParseProcess(),
					JSON.DEFAULT_PARSER_FEATURE,
					fastJsonConfig.getFeatures()
			);
		} catch (Exception e) {
			throw new CacheException("FastJsonSerializer将Byte反序列化时出错: " + e.getMessage(), e);
		}

	}
}



