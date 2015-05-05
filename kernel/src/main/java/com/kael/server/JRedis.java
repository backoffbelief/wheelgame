package com.kael.server;

import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JRedis {

	private final JedisPool jedisPool;
	private static final JRedis instance = new JRedis("localhost", 6379);
	
	public static JRedis getInstance(){
		return instance;
	}
	private JRedis(String host, int port) {
		jedisPool = new JedisPool(host, port);
	}


	public byte[] get(byte[] key){
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			broken = true;
			throw new RuntimeException(e);
		}finally{
			if(broken){
				jedisPool.returnBrokenResource(jedis);
			}else{
				jedisPool.returnResource(jedis);
			}
		}
	}
	
	public void set(byte[] key,byte[] value){
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			broken = true;
			throw new RuntimeException(e);
		}finally{
			if(broken){
				jedisPool.returnBrokenResource(jedis);
			}else{
				jedisPool.returnResource(jedis);
			}
		}
	}
	
	public Long incr(byte[] key){
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.incr(key);
		} catch (Exception e) {
			e.printStackTrace();
			broken = true;
			throw new RuntimeException(e);
		}finally{
			if(broken){
				jedisPool.returnBrokenResource(jedis);
			}else{
				jedisPool.returnResource(jedis);
			}
		}
	}
	
	public Set<byte[]> keys(byte[] pattern){
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.keys(pattern);
		} catch (Exception e) {
			e.printStackTrace();
			broken = true;
			throw new RuntimeException(e);
		}finally{
			if(broken){
				jedisPool.returnBrokenResource(jedis);
			}else{
				jedisPool.returnResource(jedis);
			}
		}
	}
	
	public Long del(byte[] key){
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
			broken = true;
			throw new RuntimeException(e);
		}finally{
			if(broken){
				jedisPool.returnBrokenResource(jedis);
			}else{
				jedisPool.returnResource(jedis);
			}
		}
	}
	
}
