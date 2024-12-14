package top.meethigher.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("record")
public class Record {

    @Id
    @Column("record_id")
    private String recordId;
    @Column("record_name")
    private String recordName;
    @Column("create_time")
    private Long createTime;
}
