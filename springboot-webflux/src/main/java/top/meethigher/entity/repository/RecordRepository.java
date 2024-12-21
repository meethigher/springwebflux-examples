package top.meethigher.entity.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import top.meethigher.entity.Record;

import javax.annotation.Resource;

@Repository
@Slf4j
public class RecordRepository {


    @Resource
    private DatabaseClient databaseClient;

    public Mono<Record> insert(Record record) {
        return databaseClient.sql("insert into record (record_id,record_name,create_time) values(:record_id,:record_name,:create_time)")
                .bind("record_id", record.getRecordId())
                .bind("record_name", record.getRecordName())
                .bind("create_time", record.getCreateTime())
                .fetch()// 调用sql
                .rowsUpdated()                    // 执行插入并获取受影响行数
                .flatMap(rows -> rows > 0 ? Mono.just(record) : Mono.empty());
    }

    public Mono<Record> update(Record record) {
        return databaseClient.sql("update record set record_name = :1, create_time = :2 where record_id = :3")
                .bind(0, record.getRecordName())
                .bind(1, record.getCreateTime())
                .bind(2, record.getRecordId())
                .fetch()
                .rowsUpdated()
                .flatMap(rows -> rows > 0 ? Mono.just(record) : Mono.empty());
    }
}
