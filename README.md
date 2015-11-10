ElasticsearchSink使用com.fish.flume.sink.elasticsearch.ElasticSearchNginxEventSerializer解析nginx日志信息。

Nginx的logformat配置：
log_format main '["$remote_addr","$http_x_forwarded_for","$remote_user","$request","$request_body","$request_uri","$status","$body_bytes_sent","$bytes_sent","$connection","$connection_requests","$msec","$pipe","$http_referer","$http_user_agent","$request_length","$request_time","$upstream_response_time","$time_local","$gzip_ratio"]';

Flume的ElasticsearchSink配置：
a1.sinks.sink1.type = org.apache.flume.sink.elasticsearch.ElasticSearchSink

a1.sinks.sink1.batchSize = 50

a1.sinks.sink1.hostNames = 10.0.1.75:9300;10.0.1.76:9300;10.0.1.77:9300

a1.sinks.sink1.indexName = fish-test

a1.sinks.sink1.indexType = fish-yyyy-MM-dd

a1.sinks.sink1.clusterName = bicloud

a1.sinks.sink1.serializer=org.apache.flume.sink.elasticsearch.ElasticSearchNginxEventSerializer

a1.sinks.sink1.serializer.fields=remote_addr http_x_forwarded_for remote_user request request_body request_uri status body_bytes_sent bytes_sent connection connection_requests msec pipe http_referer http_user_agent request_length request_time upstream_response_time time_local gzip_ratio

a1.sinks.sink1.serializer.fields.status.serializer=int

a1.sinks.sink1.serializer.fields.time_local.serializer=date

a1.sinks.sink1.serializer.fields.time_local.format=dd/MMMMM/yyyy:HH:mm:ss z

a1.sinks.sink1.serializer.fields.time_local.locale=en


serializer包含：boolean，date，double，int，integer，long，string（不区分大小写）

注

boolean：

False：false, "false", "off", "no", "0", "" (empty string), 0, 0.0

True：其他值为true

date：

format：时间格式

locale：区域
