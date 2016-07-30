package xserver.api.module.video.dto;

import java.util.List;

public class SeriesPage {

    private Integer page;

    private Integer pageSize;

    private List<VideoDto> positiveSeries;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<VideoDto> getPositiveSeries() {
        return positiveSeries;
    }

    public void setPositiveSeries(List<VideoDto> positiveSeries) {
        this.positiveSeries = positiveSeries;
    }
}
