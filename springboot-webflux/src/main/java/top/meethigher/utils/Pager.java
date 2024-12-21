package top.meethigher.utils;

import lombok.Data;

import java.util.List;

@Data
public class Pager<T> {

    private Integer pageIndex;

    private Integer pageSize;

    private Integer total;

    private Integer totalPages;

    private List<T> list;
}
