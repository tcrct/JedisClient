package com.duang.jedisclient.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.duang.jedisclient.common.CacheException;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisException;
import java.io.UnsupportedEncodingException;

/**
 * FastSerializer.
 * @author Laotang
 * @since 1.0
 * @date 2020-09-15
 */
public class FastJsonSerializer implements ISerializer {

	private static final FastJsonConfig fastJsonConfig = new FastJsonConfig();

	@Override
	public String getName() {
		return FastJsonSerializer.class.getName();
	}

	public byte[] serializerKey(String key) {
		try {
			return key.getBytes(Protocol.CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new JedisException(e);
		}
	}

	public byte[] serializerField(String key) {
		return serializerValue(key);
	}

	public String deSerializerKey(byte[] bytes) {
		try {
			return new String(bytes, Protocol.CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new JedisException(e);
		}
	}

	public byte[] serializerValue(Object value) {
		if (null == value) {
			return new byte[0];
		}
		try {
			if (value instanceof String) {
				return String.valueOf(value).getBytes();
			}
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



