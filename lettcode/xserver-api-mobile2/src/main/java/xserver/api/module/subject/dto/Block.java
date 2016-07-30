package xserver.api.module.subject.dto;

import xserver.api.module.subject.constants.SubjectConstants;
import xserver.common.dto.BaseDto;

import java.util.List;

public class Block implements Comparable{

    private String name;

    /**
     * 模块展现样式，客户端会根据这个字段，决定以何种样式展示模块数据
     * 1. 焦点图
     * 2. 直播通栏
     * 3. 无标题推荐
     * 4. 有标题有更多推荐
     * 5. 有标题无更多推荐
     * 6. 乐看搜索
     * 7. 图片频道墙
     * 8. icon频道墙
     * 9. 二级导航
     * 10. 视频列表
     * 11. 检索表
     * 12. 检索视频列表
     * 13. 重磅
     * 14. 热词
     * 26. 1大图4小图，有更多---领先版使用
     * 27. 1大图4小图，无更多---领先版使用
     * 28. 图文列表---领先版使用
     * 29. 4张小图，有更多---领先版使用
     * 30. 4张小图，无更多---领先版使用
     */
    private String style;

    private List<BaseDto> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BaseDto> getList() {
        return list;
    }

    public void setList(List<BaseDto> list) {
        this.list = list;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public int compareTo(Object o) {
        Block bo = (Block) o;
        if (!this.style.equals(bo.getStyle())) {
            Integer sort1 = SubjectConstants.sortMap.get(this.getStyle());
            Integer sort2 = SubjectConstants.sortMap.get(bo.getStyle());
            if(!(sort1 == null || sort2 == null)){
                if(sort1.intValue() < sort2.intValue()){
                    return -1;
                } else if(sort1.intValue() > sort2.intValue()){
                    return 1;
                }
                return  0;
            }
        }
        return 0;
    }
}
