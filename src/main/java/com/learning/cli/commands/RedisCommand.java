package com.learning.cli.commands;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Command(
        name = "redis-test",
        version = "1.0.0",
        mixinStandardHelpOptions = true,
        requiredOptionMarker = '*',
        description = "This is a simple redis-test command",
        header = "Sample Command",
        optionListHeading = "%nOptions are%n"
)
public class RedisCommand implements Runnable {

    @Option(
            names = {"-c", "--count"},
            description = "Provide test count",
            paramLabel = "<test count>",
            required = true
    )
    int testCount;

    @Option(
            names = {"-k", "--key"},
            description = "Provide redis key",
            paramLabel = "<redis key>"
    )
    String key = "test";

    @Option(
            names = {"-v", "--value"},
            description = "Provide redis value",
            paramLabel = "<redis value>"
    )
    String value = "value";

    private static final String IP = "localhost";
    private static final int PORT = 6379;
    private static final int TIME_OUT = 1000;

    private static JedisPoolConfig jedisPoolConfig;
    private static JedisPool pool;
    private static Jedis jedis;

    public static void main(String[] args) {
        RedisCommand.settingJedisPoolConfig();

        int exitCode = new CommandLine(new RedisCommand()).execute(args);

        System.exit(exitCode);
    }

    public static void settingJedisPoolConfig() {
        /*
            jedis connection
         */
        System.out.println("redis connection start");
        jedisPoolConfig = new JedisPoolConfig();
        pool = new JedisPool(jedisPoolConfig, IP, PORT, TIME_OUT);
        jedis = pool.getResource();
    }

    @Override
    public void run() {
        System.out.println("This is redis get-set test");
        System.out.println("redis key: " + key);
        System.out.println("redis value: " + value);
        System.out.println("test count: " + testCount);

        long beforeTime = System.currentTimeMillis();
        System.out.println("시작시간: " + beforeTime +"ms");

        for (int count = 0; count < testCount; count++) {
            jedis.set(key, value);
            jedis.get(key);
        }

        long afterTime = System.currentTimeMillis();
        System.out.println("종료시간: " + afterTime + "ms");
        System.out.println("수행시간: " + (afterTime - beforeTime) + "ms");

        jedis.close();
        pool.close();
    }
}
