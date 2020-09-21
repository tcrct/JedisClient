package com.duang.jedisclient.serializer;

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
 *
 *  @author Laotang
 * @since 1.0
 * @date 2020-09-15
 */
public class FstSerializer implements ISerializer {

	private static final Logger LOG = LoggerFactory.getLogger(FstSerializer.class);

	@Override
	public String getName() {
		return FstSerializer.class.getName();
	}
	
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
		FSTObjectOutput fstOut = null;
		try {
			ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
			fstOut = new FSTObjectOutput(bytesOut);
			fstOut.writeObject(value);
			fstOut.flush();
			return bytesOut.toByteArray();
		}
		catch (Exception e) {
			throw new CacheException("FstSerializer将值序列化成Byte时出错: " + e.getMessage(), e);
		}
		finally {
			if(fstOut != null) {
				try {
					fstOut.close();
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
	}
	
	public <T> T deSerializerValue(byte[] bytes, Class<T> type) {
		if(bytes == null || bytes.length == 0) {
			return null;
		}
		FSTObjectInput fstInput = null;
		try {
			fstInput = new FSTObjectInput(new ByteArrayInputStream(bytes));
			return (T)fstInput.readObject();
		}
		catch (Exception e) {
			throw new CacheException("FstSerializer将Byte反序列化时出错: " + e.getMessage(), e);
		}
		finally {
			if(fstInput != null) {
				try {
					fstInput.close();
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
	}
}



