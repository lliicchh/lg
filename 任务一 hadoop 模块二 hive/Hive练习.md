## Hive练习

## 第一题

```sql
WITH tmp as (
  SELECT team, (year - row_number() over (partition by team order by year)) 
  num FROM t1
)
SELECT team
FROM tmp
GROUP BY team, num
HAVING COUNT(*) = 3;
```

## 第二题

```sql
WITH tmp AS (SELECT id,lpad(`time`,5,'0') tm, price FROM t2)
SELECT id, tm, price, feature
FROM (
   SELECT id, tm, price,
       if(price < prev AND price < next, '波谷', if(price > prev AND price > next, '波峰', NULL)) feature
   FROM (
       SELECT id, tm, price,
           nvl(lag(price) OVER(PARTITION BY id ORDER BY tm), NULL) prev,
           nvl(lead(price) OVER(PARTITION BY id ORDER BY tm), NULL) next
       FROM tmp
   ) tmp2 WHERE prev IS NOT NULL AND next IS NOT NULL
) tmp3 WHERE feature IS NOT NULL
```

## 第三题

```sql
WITH tmp AS (
    SELECT id, dt
      , unix_timestamp(regexp_replace(dt || ':00', '/', '-')) AS utime
    FROM t3
  )
SELECT id, MAX(duration) AS duration, COUNT(*) AS step
FROM (
  SELECT id, (utime - first_value(utime) OVER (PARTITION BY id ORDER BY utime)) / 60 AS duration
  FROM tmp
) tmp2
GROUP BY id;
```

```sql
WITH tmp AS (SELECT id, dt, unix_timestamp(regexp_replace(dt||':00','/','-')) AS utime FROM t3)
SELECT id, gid, max(duration) times, count(*) cnt 
FROM (
    SELECT id, gid, ((utime - (first_value(utime) OVER(PARTITION BY id, gid ORDER BY utime)))/60) duration
    FROM (
        SELECT id, utime, sum(grp) OVER(PARTITION BY id ORDER BY utime ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) gid
        FROM (
            SELECT id, utime, if(duration > 30, 1, 0) grp
            FROM (
              SELECT id, utime, duration
              FROM (SELECT id, utime, ((utime - (lag(utime) OVER(PARTITION BY id ORDER BY utime)))/60) duration from tmp) tmp2
            ) tmp3
        ) tmp4
    ) tmp5
) tmp6
GROUP BY id,gid;
```



