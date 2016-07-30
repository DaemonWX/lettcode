package xserver.api.response;

import java.util.ArrayList;
import java.util.List;

/**
 * 传统基于page,pageSize的分页Response
 * @param <T>
 */
public class PageCommonResponse<T> extends BaseResponse {

    private static int PAGESIZE = 20;
    private int pageSize = PAGESIZE;
    private int pageCount = 0;
    private int count = 0;
    private int currentPage = 1;
    private List<T> data = new ArrayList<T>();

    public PageCommonResponse(int page, int pageSize) {
        this.pageSize = pageSize;
        this.currentPage = page;
    }

    public PageCommonResponse(int page, int pageSize, int count) {
        this.pageSize = pageSize;
        this.currentPage = page;
        setCount(count);
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public int getPageCount() {
        return this.pageCount;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int totalCount) {
        if (totalCount <= 0) {
            return;
        }
        this.count = totalCount;
        this.pageCount = totalCount / this.pageSize + ((totalCount % this.pageSize == 0) ? 0 : 1);
        if (this.currentPage > this.pageCount) {
            this.currentPage = this.pageCount;
        }
        if (this.currentPage <= 0) {
            this.currentPage = 1;
        }
    }

    public int getPrevious() {
        if (this.currentPage > 1) {
            return this.currentPage - 1;
        } else {
            return 1;
        }
    }

    public int getNext() {
        if (this.currentPage < this.pageCount) {
            return this.currentPage + 1;
        } else {
            return this.pageCount;
        }
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

}
