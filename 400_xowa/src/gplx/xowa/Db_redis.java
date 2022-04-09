/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2022 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa;
import gplx.Bry_;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
public class Db_redis {
	private static JedisPool redisPool = null;
	public static void Init() {
		if (redisPool != null) return;
		redisPool = new JedisPool(new JedisPoolConfig());
		Jedis redis = null;
		try {
			redis = redisPool.getResource();
			redis.select(1);
			redis.flushDB();
			redis.select(2);
			redis.flushDB();
			redis.select(3);
			redis.flushDB();
			redis.select(4);
			redis.flushDB();
		} catch (JedisConnectionException e) {
			if (redis != null) {
				redisPool.returnBrokenResource(redis);
				redis = null;
			}
			System.out.println("no connection INIT");
			//throw e;
			return;
		} finally {
			if (redis != null) {
				redisPool.returnResource(redis);
			}
		}
	}
	public static void Setfrompool(byte[] key, byte[] value) {
		Jedis redis = null;
		try {
			redis = redisPool.getResource();
			redis.select(2);
			redis.set(key, value);
			redis.select(1);
			redis.set(key, byte1);
		} catch (JedisConnectionException e) {
			if (redis != null) {
				redisPool.returnBrokenResource(redis);
				redis = null;
			}
			System.out.println("no connection SET");
			//throw e;
			return;
		} finally {
			if (redis != null) {
				redisPool.returnResource(redis);
			}
		}
	}
        private static byte[] byte1 = Bry_.new_a7("1");
        private static byte[] byte0 = Bry_.new_a7("0");

	public static byte[] Getfrompool(byte[] key) {
		Jedis redis = null;
		try {
			redis = redisPool.getResource();
			redis.select(2);
			byte[] ret = redis.get(key);
			if (ret != null) {
				redis.select(1);
				redis.incr(key);
			}
			return ret;
		} catch (JedisConnectionException e) {
			if (redis != null) {
				redisPool.returnBrokenResource(redis);
				redis = null;
			}
			System.out.println("no connection GET");
			//throw e;
			return null;
		} finally {
			if (redis != null) {
				redisPool.returnResource(redis);
			}
		}
	}
	public static void Set_filecache(byte[] key, boolean value) {
		Jedis redis = null;
		try {
			redis = redisPool.getResource();
			redis.select(3);
			redis.set(key, value ? byte1 : byte0);
		} catch (JedisConnectionException e) {
			if (redis != null) {
				redisPool.returnBrokenResource(redis);
				redis = null;
			}
			System.out.println("no connection SET");
			//throw e;
			return;
		} finally {
			if (redis != null) {
				redisPool.returnResource(redis);
			}
		}
	}

	public static byte[] Get_filecache(byte[] key) {
		Jedis redis = null;
		try {
			redis = redisPool.getResource();
			redis.select(3);
			byte[] ret = redis.get(key);
			return ret;
		} catch (JedisConnectionException e) {
			if (redis != null) {
				redisPool.returnBrokenResource(redis);
				redis = null;
			}
			System.out.println("no connection GET");
			//throw e;
			return null;
		} finally {
			if (redis != null) {
				redisPool.returnResource(redis);
			}
		}
	}

	// cache converted templatestyles
	public static void Set_templatestyles(byte[] key, byte[] value) {
		Jedis redis = null;
		try {
			redis = redisPool.getResource();
			redis.select(4);
			redis.set(key, value);
		} catch (JedisConnectionException e) {
			if (redis != null) {
				redisPool.returnBrokenResource(redis);
				redis = null;
			}
			System.out.println("no connection SET");
			//throw e;
			return;
		} finally {
			if (redis != null) {
				redisPool.returnResource(redis);
			}
		}
	}
	public static byte[] Get_templatestyles(byte[] key) {
		Jedis redis = null;
		try {
			redis = redisPool.getResource();
			redis.select(4);
			byte[] ret = redis.get(key);
			return ret;
		} catch (JedisConnectionException e) {
			if (redis != null) {
				redisPool.returnBrokenResource(redis);
				redis = null;
			}
			System.out.println("no connection GET");
			//throw e;
			return null;
		} finally {
			if (redis != null) {
				redisPool.returnResource(redis);
			}
		}
	}
}
