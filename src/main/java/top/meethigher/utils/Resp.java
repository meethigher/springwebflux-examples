package top.meethigher.utils;

import lombok.Data;
import reactor.core.publisher.Mono;

@Data
public class Resp<T> {

    private int code;

    private String msg;

    private T data;


    public static <T> Resp<T> ok(T t) {
        Resp<T> resp = new Resp<>();
        resp.setCode(0);
        resp.setMsg("成功");
        resp.setData(t);
        return resp;
    }

    public static Resp<Void> failure(String msg) {
        Resp<Void> resp = new Resp<>();
        resp.setCode(1);
        resp.setMsg("失败: " + msg);
        return resp;
    }

    public static Resp<Void> error() {
        Resp<Void> resp = new Resp<>();
        resp.setCode(500);
        resp.setMsg("服务器内部错误");
        return resp;
    }

    public static <T> Mono<Resp<T>> getSuccessResp(Mono<T> mono) {
        return mono.map(Resp::ok);
    }

    public static Mono<Resp<Void>> getFailureResp(String msg) {
        return Mono.just(failure(msg));
    }

    public static Mono<Resp<Void>> getErrorResp() {
        return Mono.just(error());
    }
}
