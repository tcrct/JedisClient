## JedisClient

jedis封装工具类组件，统一使用方式

####使用方法
以springboot为例，在系统启动完成里加入以下代码：

@Component
@Order(value = 1)
 public class StartupAfterRunner implements CommandLineRunner {     
     private static final Logger LOGGER = LoggerFactory.getLogger(StartupAfterRunner.class); 
     @Override
     public void run(String... args) throws Exception {
         RedisPlugin redisPlugin = new RedisPlugin();
         try {
             redisPlugin.start();
         } catch (Exception e){
             LOGGER.info("启动RedisPlugin时出错: " + e.getMessage(), e);
         }
     }     
 }
 
 其中 RedisPlugin 代码为：
 
 public class RedisPlugin {
        #设置应用ID
        private String appId = "5f20d9cd209bce9e41176fbb";
        #设置应用安全码
        private String appSecret = "8338958d2e994292a21d5195b349704b";
        #访问redis密码
        private String password;
        #序列化方式，默认用fastjson方式
        private ISerializer serializer = new FastJsonSerializer();
        #redis节点的host port
        private Set< HostAndPort > nodeSet = new HashSet<HostAndPort>();
        # jedisClient对象
        private JedisClient jedisClient;        
        public RedisPlugin() {
            init();
         }
         private void init(Set<HostAndPort> nodeSet, RedisConfig.RedisType redisType) {
                jedisClient = new JedisClient.Builder()
                        .appId(appId)
                        .secret(appSecret)
                        .password(password)
                        .nodeSet(nodeSet)
                        .redisType(redisType)
                        .serializer(serializer)
                        .build();
            }        
            public void start() throws Exception {
                jedisClient.start();
            }
 }
 
 ####以上代码为系统启动完成后，链接redis对象，使用示例：
 创建一个CacheKeyEnum类，定义好每一个redis缓存key的前缀，过期时间，说明等，例如：
 
 public enum UserCacheKeyEnum implements ICacheKeyEnums {
  
     USER_ID("duang:userid:", ICacheKeyEnums.DEFAULT_TTL, "用户ID");
 
 
     private String keyPrefix;
     private int  ttl;
     private String keyDesc;
     private TestCacheKeyEnum(String keyPrefix, int ttl, String keyDesc) {
         this.keyPrefix = keyPrefix;
         this.ttl = ttl;
         this.keyDesc = keyDesc;
     }
 
     public String getKeyPrefix() {
         return keyPrefix;
     }
 
     public int getKeyTTL() {
         return ttl;
     }
 
     public String getKeyDesc() {
         return keyDesc;
     }
 }
 
 然后再创建一个CacheService层，在CacheService层里封装一下Cache的操作方法，例如创建一个UserCacheService
 
 @Service
 public class UserCacheService {
 
     private static final CacheKeyModel cacheKeyModel = new CacheKeyModel.Builder(UserCacheKeyEnum.USER).build();
 
     public void save(User user) {
         if (ToolsKit.isEmpty(user)) {
             throw new NullPointerException("UserCacheService save时出错: user对象不能为空");
         }
         if (ToolsKit.isEmpty(user.getId())) {
             throw new NullPointerException("UserCacheService save时出错: userId不能为空");
         }
         RedisFactory.getClient().hset(cacheKeyModel, user.getId(), user);
     }
 
     public Stpusrinf get(String id) {
         return RedisFactory.getClient().hget(cacheKeyModel, Stpusrinf.class, id);
     }
 
     public void del(String... ids) {
         RedisFactory.getClient().hdel(cacheKeyModel, ids);
     } 
 
 }
 
 

