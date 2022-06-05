package com.rrs.redis.template;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import javax.print.DocFlavor;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * REDIS操作封装类,主要是为了熟悉redisson api
 */
@Slf4j
@Component
@ConditionalOnClass(RedissonClient.class)
public class RedisDB {
    @Autowired
    private RedissonClient redisson;

    private static final JsonJacksonCodec DEFAULT_CODEC = new JsonJacksonCodec();
    /*************string key 操作start**********************/

    /**
     * 返回字符串的get
     */
    public String getString(String key){
        RBucket<String> keyRBucket =  redisson.getBucket(key);
        String s = keyRBucket.get();
        return s;
    }

    /**
     * 追加append
     * @param key
     * @param append
     */
    public void append(String key,String append){
        RBucket<String> bucket = redisson.getBucket(key);
        String s = bucket.get();
        bucket.set(s+append);
    }
    /**
     * 返回字符串长度
     * @param key
     */
    public int strLen(String key){
        RBucket<String> bucket = redisson.getBucket(key);
        String s = bucket.get();
        return s.length();
    }
    /**
     * 返回obj
     */
    public Object getObj(String key){
        RBucket<Object> bucket = redisson.getBucket(key);
        Object s = bucket.get();
        return s;
    }

    /**
     * mget
     * @param key
     * @return
     */
    public Map<String, Object> mGet(String ... key){
        RBuckets buckets = redisson.getBuckets();
        Map<String, Object> stringObjectMap = buckets.get(key);
        return stringObjectMap;
    }

    /**
     * set
     * @param key
     * @param value
     */
    public void  set(String key, Object value){
        RBucket<Object> bucket = redisson.getBucket(key);
        bucket.set(value);
    }
    /**
     * 获取并返回
     * @param key
     * @param value
     */
    public Object  getSet(String key, Object value){
        RBucket<Object> bucket = redisson.getBucket(key);
        return bucket.getAndSet(value);
    }

    /**
     * set 增加过期时间
     * @param key
     * @param value
     */
    public void  set(String key, Object value, Long time, TimeUnit unit){
        RBucket<Object> bucket = redisson.getBucket(key);
        bucket.set(value,time,unit);
    }

    /**
     * 增加过期时间
     * @param key
     * @param time
     */
    public void expire(String key,Long time){
        RBucket<Object> bucket = redisson.getBucket(key);
        bucket.expire(time, TimeUnit.SECONDS);
    }

    /**
     * 增加过期时间
     * @param key
     * @param time
     */
    public void expire(String key,Long time,TimeUnit unit){
        RBucket<Object> bucket = redisson.getBucket(key);
        bucket.expire(time, unit);
    }


    /**
     * key是否存在
     * @param key
     */
    public boolean isExists(String key){
        RBucket<Object> bucket = redisson.getBucket(key);
        return bucket.isExists();
    }

    /**
     * mset 设置多个
     */
    public void  mSet(Map<String,Object> map){
        RBuckets buckets = redisson.getBuckets();
        buckets.set(map);
    }

    /**
     * value自增
     * @param key
     */
    public Long incr(String key){
        RAtomicLong atomicLong = redisson.getAtomicLong(key);
        long l = atomicLong.incrementAndGet();
        return l;
    }
    /**
     * value自增指定数字
     * @param key
     */
    public Long incr(String key,Long value){
        RAtomicLong atomicLong = redisson.getAtomicLong(key);
        return atomicLong.addAndGet(value);
    }

    /**
     * value自减
     * @param key
     * @return
     */
    public Long decr(String key){
        RAtomicLong atomicLong = redisson.getAtomicLong(key);
        return atomicLong.decrementAndGet();
    }

    /**
     * value自减某个值
     * @param key
     * @param value
     * @return
     */
    public Long decr(String key,Long value){
        RAtomicLong atomicLong = redisson.getAtomicLong(key);
        return atomicLong.addAndGet(-value);
    }

    /**
     * 删除key value
     * @param key
     */
    public void del(String key){
        redisson.getBucket(key).delete();
    }
    /*************string key 操作end**********************/

    /*************hash 操作start**********************/

    /**
     * 如果确认hash的key value为string可以这样
     * @param key
     * @param hKey
     * @return
     */
    public Object hGet(String key,String hKey){
        RMap<String, Object> map = redisson.getMap(key);
        return map.get(hKey);
    }

    /**
     *
     * @param key
     * @param keys
     * @return
     */
    public Map<String, String> hMGet(String key,Set<String> keys){
        RMap<String, String> map = redisson.getMap(key);
        Map<String, String> all = map.getAll(keys);
        return all;
    }
    /**
     * 重载方法
     * @param key
     * @param hKey
     * @return
     */
    public Object hGet(String key,Object hKey){
        RMap<Object, Object> map = redisson.getMap(key);
        return map.get(hKey);
    }

    /**
     * put key value
     * @param key
     * @param hKey
     * @param hValue
     */
    public void hSet(String key,Object hKey,Object hValue){
        RMap<Object, Object> map = redisson.getMap(key);
        map.put(hKey,hValue);
    }

    /**
     * put key value
     * @param key
     * @param hmap
     */
    public void hMSet(Map<String,String> hmap,String key){
        RMap<Object, Object> map = redisson.getMap(key);
        map.putAll(hmap);
    }
    /**
     * put key value
     * @param key
     * @param hmap
     */
    public void hMSet(String key,Map<Object,Object> hmap){
        RMap<Object, Object> map = redisson.getMap(key);
        map.putAll(hmap);
    }

    /**
     * 删除
     * @param key
     * @param hKey
     */
    public void hDel(String key,Object hKey){
        RMap<Object, Object> map = redisson.getMap(key);
        map.remove(hKey);
    }

    /**
     * 获取字段个数
     * @param key
     * @return
     */
    public int hLen(String key){
        RMap<Object, Object> map = redisson.getMap(key);
        return map.size();
    }

    /**
     * key是否存在
     * @param key
     * @param hKey
     * @return
     */
    public boolean hExists(String key ,Object hKey){
        return redisson.getMap(key).containsKey(hKey);
    }

    /**
     * 获取所有keys集合
     * @param key
     * @return
     */
    public Set<Object> hKeys(String key ){
        RMap<Object, Object> map = redisson.getMap(key);
        Set<Object> objects = map.keySet();
        return objects;
    }

    /**
     * 获取所有value
     * @param key
     * @return
     */
    public Collection<Object> hValues(String key){
        RMap<Object, Object> map = redisson.getMap(key);
        return map.values();
    }

    /**
     * 获取所有键值对集合
     * @param key
     * @return
     */
    public  Set<Map.Entry<Object, Object>> hGetAll(String key){
        RMap<Object, Object> map = redisson.getMap(key);
        Set<Map.Entry<Object, Object>> entries = map.entrySet();
        return entries;
    }

    /*************hash 操作end**********************/

    /*************lis 操作start**********************/
    /**
     * 左边push
     * @param key
     * @param value
     */
    public void lPush(String key,Object value){
        RList<Object> list = redisson.getList(key);
        list.add(value);
    }

    /**
     * 右边push
     * @param key
     * @param value
     */
    public void rPush(String key,Object value){
        RList<Object> list = redisson.getList(key);
        list.add(list.size(),value);
    }

    /**
     * 获取所有
     * @param key
     * @return
     */
    public List<Object> lRangeAll(String key){
        return redisson.getList(key).readAll();
    }

    /**
     * 获取一部分
     * @param key
     * @param index
     * @return
     */
    public List<Object> lRange(String key,Integer index){
        return redisson.getList(key).range(index);
    }

    /**
     * 获取一部分
     * @param key
     * @param index
     * @return
     */
    public List<Object> lRange(String key,Integer index,Integer toIndex){
        return redisson.getList(key).range(index,toIndex);
    }

    /**
     * 在..之前插入
     * @param key
     * @param find
     * @param item
     */
    public void lInsertBefore(String key,Object find, Object item){
        redisson.getList(key).addBefore(find,item);
    }

    /**
     * 在...之后插入
     * @param key
     * @param find
     * @param item
     */
    public void lInsertAfter(String key,Object find, Object item){
        redisson.getList(key).addAfter(find,item);
    }

    //查找

    /**
     * 获取制定index
     * @param key
     * @param index
     * @return
     */
    public Object lIndex(String key,Integer index){
        return redisson.getList(key).get(index);
    }

    /**
     * list的长度
     * @param key
     * @return
     */
    public Integer lLen(String key){
        return redisson.getList(key).size();
    }

    /**
     * 左边删除
     * @param key
     */
    public void lPop(String key){
        redisson.getList(key).remove(1);
    }

    /**
     * 右边删除
     * @param key
     */
    public void rPop(String key){
        RList<Object> list = redisson.getList(key);
        list.remove(list.size()-1);
    }

    /**
     * 删除制定元素
     * @param key
     * @param count
     * @param value
     */
    public void lRem(String key,Integer count,Object value){
        redisson.getList(key).remove(value,count);
    }

    /**
     * 修剪list
     * @param key
     * @param start
     * @param end
     */
    public void lTrim(String key,Integer start,Integer end){
        redisson.getList(key).trim(start,end);
    }

    /**
     * 修改制定下标元素
     * @param key
     * @param index
     * @param value
     */
    public void lSet(String key,Integer index,Object value){
        redisson.getList(key).set(index,value);
    }
    /*************lis 操作end **********************/

    /*************set 操作start**********************/
    /**
     * sadd
     * @param key
     * @param value
     */
    public void sAdd(String key,Object value){
        redisson.getSet(key).add(value);
    }

    /**
     * 批量添加
     * @param key
     * @param set
     */
    public void sAdd(String key,Set<Object> set){
        redisson.getSet(key).addAll(set);
    }

    /**
     * 删除
     * @param key
     * @param value
     */
    public void sRem(String key,Object value){
        redisson.getSet(key).remove(value);
    }

    /**
     * 获取size
     * @param key
     * @return
     */
    public Integer sCard(String key){
        return redisson.getSet(key).size();
    }

    /**
     * 是否是
     * @param key
     * @param ele
     * @return
     */
    public boolean sIsMember (String key,Object ele){
        return redisson.getSet(key).contains(ele);
    }

    /**
     * 随机获取
     * @param key
     * @param count
     * @return
     */
    public Set<Object>RandMember(String key,Integer count){
        return redisson.getSet(key).random(count);
    }

    /**
     * 获取所有
     * @param key
     * @return
     */
    public Set<Object> sMembers(String key){
        return redisson.getSet(key).readAll();
    }

    /**
     * 交集
     * @param key
     * @param other
     * @return
     */
    public Set<Object> sInter(String key,String ...other){
        Set<Object> objects = redisson.getSet(key).readIntersection(other);
        return objects;
    }

    /**
     * 求并济
     * @param key
     * @param other
     * @return
     */
    public Set<Object> sUinon(String key,String ...other){
        Set<Object> objects = redisson.getSet(key).readUnion(other);
        return objects;
    }

    /**
     * 求差集
     * @param key
     * @param other
     * @return
     */
    public Set<Object> sDiff(String key,String ...other){
        Set<Object> objects = redisson.getSet(key).readDiff(other);
        return objects;
    }

    /*************set 操作end**********************/

    /*************sset 操作start**********************/
    /**
     * 增加zadd
     * @param key
     * @param o
     */
    public void zAdd(String key,Object o){
        zAdd(key,o,0);
    }
    public void zAdd(String key,Object o,Integer value){
        redisson.getScoredSortedSet(key).add(value,o);
    }

    /**
     * 长度
     * @param key
     * @return
     */
    public Integer zCard(String key){
        return redisson.getScoredSortedSet(key).size();
    }
    /*
    * 获取分数
     */
    public Double zScore(String key,Object member){
        return redisson.getScoredSortedSet(key).getScore(member);
    }

    /**
     * 获取排名
     * @param key
     * @param member
     * @return
     */
    public Integer zRank(String key,Object member){
        return redisson.getScoredSortedSet(key).rank(member);
    }

    /**
     * 获取排名 倒叙
     * @param key
     * @param member
     * @return
     */
    public Integer zRevRank(String key,Object member){
        return redisson.getScoredSortedSet(key).revRank(member);
    }

    /**
     * 删除元素
     * @param key
     * @param member
     */
    public void zRem(String key,Object member){
        redisson.getScoredSortedSet(key).remove(member);
    }

    /**
     * 增加分数
     * @param key
     * @param member
     * @param inc
     */
    public void  zIncrBy(String key,Object member,Double inc){
        redisson.getScoredSortedSet(key).addScore(member,inc);
    }

    /**
     * 获取某段排名的元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Collection<Object> zRange(String key, int start,int end){
        RScoredSortedSet<Object> scoredSortedSet = redisson.getScoredSortedSet(key);
        Collection<Object> objects = scoredSortedSet.valueRange(start, end);
        return objects;
    }

    /**
     * 获取某段排名的元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Collection<Object> zRevRange(String key, int start,int end){
        RScoredSortedSet<Object> scoredSortedSet = redisson.getScoredSortedSet(key);
        Collection<Object> objects = scoredSortedSet.valueRangeReversed(start,end);
        return objects;
    }

    /**
     * 获取某段排名的元素 按照分数 是否开闭区间
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Collection<Object> zRangeByScore(String key, double start,double end){
        RScoredSortedSet<Object> scoredSortedSet = redisson.getScoredSortedSet(key);
        Collection<Object> objects = scoredSortedSet.valueRange(start,true,end,true);
        return objects;
    }

    /**
     *  获取某段排名的元素 按照分数 是否开闭区间 倒
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Collection<Object> zRevRangeByScore(String key, double start,double end){
        RScoredSortedSet<Object> scoredSortedSet = redisson.getScoredSortedSet(key);
        Collection<Object> objects = scoredSortedSet.valueRangeReversed(start,true,end,true);
        return objects;
    }

    /**
     * 获取某段分数 的数量
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Integer zCount(String key, double start,double end){
        RScoredSortedSet<Object> scoredSortedSet = redisson.getScoredSortedSet(key);
        return scoredSortedSet.count(start,true,end,true);
    }

    /**
     * 删除 按照排名
     * @param key
     * @param start
     * @param end
     */
    public void zRemRangeByRank(String key, int start,int end){
        redisson.getScoredSortedSet(key).removeRangeByRank(start,end);
    }
    /**
     * 删除 按照分数
     * @param key
     * @param start
     * @param end
     */
    public void zRemRangeByScore(String key, double start,double end){
        redisson.getScoredSortedSet(key).removeRangeByScore(start,true,end,true);
    }

    /**
     * 交集
     * @param key
     * @param names
     * @return
     */
    public Object zInter(String key,String ...names){
        Collection<Object> objects = redisson.getScoredSortedSet(key).readIntersection(names);
        return objects;
    }

    /**
     * 交集
     * @param key
     * @param names
     * @return
     */
    public Object zUnion(String key,String ...names){
        Collection<Object> objects = redisson.getScoredSortedSet(key).readUnion(names);
        return objects;
    }

    /**
     * diff
     * @param key
     * @param names
     * @return
     */
    public Object zDiff(String key,String ...names){
        Collection<Object> objects = redisson.getScoredSortedSet(key).readDiff(names);
        return objects;
    }
    /*************sset 操作end**********************/

    /*************other 操作start**********************/
    /**
     * setbit
     * @param key
     * @param offset
     * @param value
     */
    public void setBit(String key,Long offset,Integer value){
        RBitSet bitSet = redisson.getBitSet(key);
        bitSet.setInteger(offset,value);
    }

    /**
     * 获取bit
     * @param key
     * @param offset
     * @return
     */
    public Integer getBit(String key,Long offset){
        RBitSet bitSet = redisson.getBitSet(key);
        return bitSet.getInteger(offset);
    }

    /**
     *
     * @param key
     * @param keys
     */
    public void bitAnd(String key,String ... keys){
        RBitSet bitSet = redisson.getBitSet(key);
        bitSet.and(keys);
    }

    /**
     * hyper
     * @param key
     * @param set
     */
    public void pfAdd(String key,Collection<Object> set){
        RHyperLogLog<Object> hyperLogLog = redisson.getHyperLogLog(key);
        hyperLogLog.addAll(set);
    }

    /**
     *
     * @param key
     * @return
     */
    public Long pfCount(String key){
        RHyperLogLog<Object> hyperLogLog = redisson.getHyperLogLog(key);
        return hyperLogLog.count();
    }

    /**
     *
     * @param key
     * @param source
     */
    public void pfMergr(String key,String ... source){
        RHyperLogLog<Object> hyperLogLog = redisson.getHyperLogLog(key);
        hyperLogLog.mergeWith(source);
    }

    /**
     *
     * @param key
     * @param lon
     * @param lat
     * @param member
     */
    public void geoAdd(String key,Long  lon,Long lat,String member){
        RGeo<Object> geo = redisson.getGeo(key);
        GeoEntry geoEntry=new GeoEntry(lon,lat,member);
        geo.add(geoEntry);
    }

    /**
     *
     * @param key
     * @param member
     * @return
     */
    public  Map<Object, GeoPosition> geoPos(String key,String member){
        RGeo<Object> geo = redisson.getGeo(key);
        Map<Object, GeoPosition> pos = geo.pos(member);
        return pos;
    }

    /**
     *
     * @param key
     * @param member1
     * @param member2
     * @return
     */
    public double geoDist(String key,String member1,String member2){
        RGeo<Object> geo = redisson.getGeo(key);
        Double dist = geo.dist(member1, member2, GeoUnit.KILOMETERS);
        return dist;
    }

    /**
     *
     * @param key
     * @param lon
     * @param lat
     * @param member
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public Map<Object, Double> geoRadius(String key,Long  lon,Long lat,String member) throws ExecutionException, InterruptedException {
        RGeo<Object> geo = redisson.getGeo(key);
        RFuture<Map<Object, Double>> mapRFuture = geo.radiusWithDistanceAsync(member, 150, GeoUnit.KILOMETERS);
        Map<Object, Double> objectDoubleMap = mapRFuture.get();
        return objectDoubleMap;
    }

    /**
     *
     * @param key
     * @param member
     * @return
     */
    public Map<Object, String> geoHash(String key,String ... member){
        RGeo<Object> geo = redisson.getGeo(key);
        Map<Object, String> hash = geo.hash(member);
        return hash;
    }

    /**
     *
     * @param key
     * @param members
     */
    public void geoRem(String key,Collection<String> members){
        RScoredSortedSet<Object> scoredSortedSet = redisson.getScoredSortedSet(key);
        scoredSortedSet.removeAll(members);
    }
    /*************other 操作end**********************/
    /*************lua 操作start**********************/
    //脚本管理

    /**
     * 加载脚本
     * @param script
     */
    public void scriptLoad(String script){
        redisson.getScript().scriptLoad(script);
    }

    /**
     * 脚本是否存在
     * @param sha
     * @return
     */
    public List<Boolean> scriptExists(String ...sha){
        return redisson.getScript().scriptExists(sha);
    }

    /**
     * 清除所有脚本
     */
    public void scriptFlush(){
        redisson.getScript().scriptFlush();
    }

    /**
     * 杀死正在执行的脚本,但当lua正在执行写操作,将会失败.
     */
    public void scriptKill(){
        redisson.getScript().scriptKill();
    }

    /**
     * 执行脚本
     * @param mode
     * @param luaScript
     * @param returnType
     * @return
     */
    public Object eval(RScript.Mode mode, String luaScript, RScript.ReturnType returnType){
        RScript script = redisson.getScript();
        Object eval = script.eval(mode, luaScript, returnType);
        return eval;
    }

    /**
     * 执行脚本 带参数
     * @param mode
     * @param luaScript
     * @param returnType
     * @param keys
     * @param values
     * @return
     */
    public Object eval(RScript.Mode mode, String luaScript, RScript.ReturnType returnType,List<Object> keys,Object ...values){
        RScript script = redisson.getScript();
        Object eval = script.eval(mode, luaScript, returnType,keys,values);
        return eval;
    }

    /**
     * 脚本执行 根据shell
     * @param mode
     * @param shaDigest
     * @param returnType
     * @return
     */
    public Object evalSha(RScript.Mode mode, String shaDigest, RScript.ReturnType returnType){
        RScript script = redisson.getScript();
        return script.evalSha(mode, shaDigest, returnType);
    }

    /**
     *
     * @param mode
     * @param shaDigest
     * @param returnType
     * @param keys
     * @param values
     * @return
     */
    public Object evalSha(RScript.Mode mode, String shaDigest, RScript.ReturnType returnType, List<Object> keys, Object... values){
        RScript script = redisson.getScript();
        return script.evalSha(mode, shaDigest, returnType, keys, values);
    }

    /**
     * 发布
     * @param key
     * @param message
     */
    public void publish(String key,String message){
        RQueue<Object> queue = redisson.getQueue(key);
        queue.add(message);
    }

    /**
     * sub
     * @param key
     * @return
     */
    public Object subscribe(String key){
        RQueue<Object> queue = redisson.getQueue(key);
        Object poll = queue.poll();
        return poll;
    }
}
