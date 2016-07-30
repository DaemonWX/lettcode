package xserver.api.response;

import java.util.Collection;

/**
 * 基于index的分页Response
 * @param <T>
 */
public class PageResponse<T> extends BaseResponse {

    /**
     * 总数
     */
    private Integer totalCount;

    /**
     * 当前索引
     */
    private Integer currentIndex;

    /**
     * 下一页索引
     */
    private Integer nextIndex;

    /**
     * 数据列表
     */
    private Collection<T> data;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(Integer currentIndex) {
        this.currentIndex = currentIndex;
    }

    public Integer getNextIndex() {
        return nextIndex;
    }

    public void setNextIndex(Integer nextIndex) {
        this.nextIndex = nextIndex;
    }

    public Collection<T> getData() {
        return data;
    }

    public void setData(Collection<T> data) {
        this.data = data;
    }
}
