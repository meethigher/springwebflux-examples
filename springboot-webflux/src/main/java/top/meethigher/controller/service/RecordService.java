package top.meethigher.controller.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;
import top.meethigher.entity.Record;
import top.meethigher.entity.repository.RecordRepository;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class RecordService {

    @Resource
    private RecordRepository recordRepository;

    @Transactional(rollbackFor = Exception.class)
    public Mono<Record> save(Record record) {
        if (ObjectUtils.isEmpty(record.getRecordId())) {
            record.setRecordId(UUID.randomUUID().toString().replace("-", ""));
            return recordRepository.insert(record);
        } else {
            return recordRepository.update(record);
        }
    }
}
