package top.meethigher.controller;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import top.meethigher.entity.Location;
import top.meethigher.entity.repository.LocationRepository;
import top.meethigher.exception.DIYException;
import top.meethigher.utils.Resp;

import javax.annotation.Resource;
import java.util.function.Function;

@RestController
@RequestMapping("/location")
public class LocationController {

    @Resource
    private LocationRepository locationRepository;

    @Resource
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @PostMapping("/save")
    public Mono<Resp<Location>> save(
            @RequestBody Location location
    ) {
        Mono<Location> mono = Mono.justOrEmpty(location.getLid())
                .switchIfEmpty(Mono.error(new DIYException("主键不可为空")))
                .flatMap((Function<String, Mono<Boolean>>) id -> locationRepository.existsById(id))
                .flatMap((Function<Boolean, Mono<Location>>) exist -> exist ? r2dbcEntityTemplate.update(location) : r2dbcEntityTemplate.insert(location))
                .switchIfEmpty(Mono.error(new DIYException("保存数据失败")));
        return Resp.getSuccessResp(mono);
    }

    @GetMapping("/findById")
    public Mono<Resp<Location>> findById(
            String id
    ) {
        Mono<Location> mono = Mono.just(id)
                .flatMap((Function<String, Mono<Location>>) s -> locationRepository.findById(id))
                .switchIfEmpty(Mono.error(new DIYException("该数据不存在")));
        return Resp.getSuccessResp(mono);
    }
}
