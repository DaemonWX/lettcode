package xserver.api.module.video.dto;

import java.util.List;

import xserver.lib.mysql.table.BaseData;

public class AlbumSeriesDto extends BaseData {

    /**
     * 
     */
    private static final long serialVersionUID = -1159915935465539895L;

    private List<SeriesPage> seriesPages; // 剧集列表

    public List<SeriesPage> getSeriesPages() {
        return seriesPages;
    }

    public void setSeriesPages(List<SeriesPage> seriesPages) {
        this.seriesPages = seriesPages;
    }

}
