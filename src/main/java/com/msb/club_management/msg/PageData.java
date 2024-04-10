package com.msb.club_management.msg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PageData {

    // 当前页码
    private Long pageIndex;

    // 每页数据量
    private Long pageSize;

    // 查询的总页数
    private Long pageTotal;

    // 符合条件的总记录数
    private Long count;

    // 分页查询包含的结果集
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

    public Long getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Long pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Long pageTotal) {
        this.pageTotal = pageTotal;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public PageData() {

        super();
    }

    /**
     * 构造函数用于创建一个分页数据对象。
     * @param pageIndex 当前页码，从1开始。
     * @param pageSize 每页显示的记录数。
     * @param count 总记录数。
     * @param data 分页数据列表。
     */
    public PageData(Long pageIndex, Long pageSize, Long count, List<Map<String, Object>> data) {

        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.count = count;

        // 如果传入的数据非空且非零，则赋值给this.data，否则保持this.data为空。
        this.data = (data != null && data.size() > 0) ? data : this.data;

        // 当总记录数大于0时，计算总页数。
        if(count > 0){

            // 如果记录数能整除每页记录数，则总页数为记录数除以每页记录数；否则，总页数为记录数除以每页记录数再加1。
            this.pageTotal = (count % pageSize) == 0 ?  (count / pageSize) : (count / pageSize + 1);
        }else {

            // 如果总记录数为0，则总页数为0。
            this.pageTotal = 0L;
        }
    }


    @Override
    public String toString() {
        return "Page [pageIndex=" + pageIndex + ", pageSize=" + pageSize + ", pageTotal=" + pageTotal + ", count="
                + count + ", data=" + data + "]";
    }
}
