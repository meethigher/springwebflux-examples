package top.meethigher.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import top.meethigher.controller.service.RecordService;
import top.meethigher.entity.Record;

import javax.annotation.Resource;

@RestController
@RequestMapping("/db")
public class DatabaseClientController {

    @Resource
    private RecordService recordService;

    @GetMapping("/save")
    public Mono<Record> save(
            @RequestParam(value = "recordId", required = false) String recordId,
            @RequestParam(value = "recordName", required = true) String recordName) {
        Record record = new Record();
        record.setRecordId(recordId);
        record.setRecordName(recordName);
        record.setCreateTime(System.currentTimeMillis());
        return recordService.save(record);
    }
}
