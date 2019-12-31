CREATE TABLE "Dashboard"."net_sniffer" (
-- 主键，自增
  "id" integer PRIMARY KEY AUTOINCREMENT,
-- 请求地址，不能为空
  "path" text NOT NULL,
-- 请求参数
  "query" text,
-- 请求Method
  "method" text,
-- 请求时长
  "duration" integer,
-- 网络状态，-1:无网 0:蓝牙 1:wifi 2:2g 3:3G 4:4G 5:5G
  "transport" integer,
-- 请求结果状态，100网络请求失败 200网络响应成功  300业务请求成功
  "state" integer,
-- 请求体大小
  "content_size" integer,
-- 请求体类型
  "content_type" text
);

-- 查询
SELECT * FROM "net_sniffer"

-- 命中次数统计
SELECT "path" as "请求地址",count(*) as "次数" FROM "net_sniffer" GROUP BY "path"
