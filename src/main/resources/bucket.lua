--- 限流KEY资源唯一标识
local key = "rate.limit:" .. KEYS[1]
--- 时间窗最大并发数
local limit = tonumber(ARGV[1])
--- 时间窗内当前并发数
local current = tonumber(redis.call('get', key) or "0")
--如果超出限流大小
if current + 1 > limit then
  return 0
else  --请求数+1，并设置1秒过期
   redis.call("INCRBY", key,"1")
   redis.call("expire", key,"1")
   return current + 1
end