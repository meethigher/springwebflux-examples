package top.meethigher.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;


/**
 * org.springframework.data.repository.reactive.ReactiveCrudRepository#save(java.lang.Object)
 * r2dbc的默认isNew逻辑是，如果主键为空就insert，否则就update。
 * 如果想要实现自定义isNew逻辑，可以通过实现org.springframework.data.domain.Persistable
 * 。
 *
 * @author <a href="https://meethigher.top">chenchuancheng</a>
 * @since 2024/12/21 21:45
 */
@Data
public class Location {

    @Id
    private String lid;

    private Double x;

    private Double y;
}
