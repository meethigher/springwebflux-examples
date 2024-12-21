package top.meethigher.entity.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import top.meethigher.entity.Location;

public interface LocationRepository extends R2dbcRepository<Location, String> {
}
