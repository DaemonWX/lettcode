package xserver.api.module.search.dto;

import java.util.List;

public class FilterResultDto {
    private Integer total;
    private Integer curPage;
    private Integer nextPage;
    private List<AlbumInfoDto> result;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCurPage() {
        return curPage;
    }

    public void setCurPage(Integer curPage) {
        this.curPage = curPage;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public List<AlbumInfoDto> getResult() {
        return result;
    }

    public void setResult(List<AlbumInfoDto> result) {
        this.result = result;
    }

}
