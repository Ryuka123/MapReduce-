# MapReduce编程与算法设计

## flowsum （Mapreduce中的排序初步）
对日志数据中的上下行流量信息汇总，并输出按照总流量倒序排序的结果

## provinceflow （Mapreduce中的分区Partitioner）
根据归属地输出流量统计数据结果到不同文件，以便于在查询统计结果时可以定位到省级范围进行

## weblogwash （web日志预处理）
对web访问日志中的各字段识别切分，去除日志中不合法的记录，根据KPI统计需求，生成各类访问请求过滤数据
