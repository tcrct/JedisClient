/**
 * Copyright (c) 2011-2019, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duang.jedisclient.serializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisException;

import java.io.*;

/**
 * JdkSerializer.
 *
 * @author Laotang
 * @since 1.0
 * @date 2020-09-15
 */
public class JdkSerializer implements ISerializer {

	private static final Logger LOG = LoggerFactory.getLogger(JdkSerializer.class);

	@Override
	public String getName() {
		return JdkSerializer.class.getName();
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
		ObjectOutputStream objectOut = null;
		try {
			ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
			objectOut = new ObjectOutputStream(bytesOut);
			objectOut.writeObject(value);
			objectOut.flush();
			return bytesOut.toByteArray();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			if(objectOut != null)
				try {objectOut.close();} catch (Exception e) {LOG.error(e.getMessage(), e);}
		}
	}
	
	public <T> T deSerializerValue(byte[] bytes, Class<T> type) {
		if(bytes == null || bytes.length == 0)
			return null;
		
		ObjectInputStream objectInput = null;
		try {
			ByteArrayInputStream bytesInput = new ByteArrayInputStream(bytes);
			objectInput = new ObjectInputStream(bytesInput);
			return (T)objectInput.readObject();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			if (objectInput != null)
				try {objectInput.close();} catch (Exception e) {LOG.error(e.getMessage(), e);}
		}
	}
}



