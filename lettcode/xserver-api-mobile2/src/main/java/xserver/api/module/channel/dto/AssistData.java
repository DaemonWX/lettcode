package xserver.api.module.channel.dto;

/**
 * Created by xuli3 on 2015/7/14.
 */
public class AssistData {

    /**角标需要的辅助数据*/
    String isPay;//是否付费
    String pageid;//页面id
    //以下是针对专辑大类的,dataType是必传数据且属于大类型，例如只要视频属于某一个专辑，这里就传专辑
    String dataType;//数据类型，参考ChannelConstant中的数据类型定义
    String isPlayMark;//是否是独播
    String isHomemade;//是否是自制
    String aType;//专辑类型，正片、预告、花絮...
    String playStream;//视频的播放码流
    
    public AssistData() {
    }

    public AssistData(String isPay, String dataType, String isPlayMark, String isHomemade, String aType) {
        this.isPay = isPay;
        this.dataType = dataType;
        this.isPlayMark = isPlayMark;
        this.isHomemade = isHomemade;
        this.aType = aType;
    }

    public AssistData(String isPay, String dataType, String isPlayMark, String isHomemade,
    		String aType, String pageid, String playStream) {
        this.isPay = isPay;
        this.dataType = dataType;
        this.isPlayMark = isPlayMark;
        this.isHomemade = isHomemade;
        this.aType = aType;
        this.pageid = pageid;
        this.playStream = playStream;
    }

    public AssistData(String isPay, String dataType, String isPlayMark, String isHomemade, String aType, String pageid) {
        this.isPay = isPay;
        this.dataType = dataType;
        this.isPlayMark = isPlayMark;
        this.isHomemade = isHomemade;
        this.aType = aType;
        this.pageid = pageid;
    }

    public AssistData(String pageid) {
        this.pageid = pageid;
    }

    public AssistData(String isPay, String dataType,String pageid) {
        this.isPay = isPay;
        this.dataType = dataType;
        this.pageid = pageid;
    }

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getIsPlayMark() {
        return isPlayMark;
    }

    public void setIsPlayMark(String isPlayMark) {
        this.isPlayMark = isPlayMark;
    }

    public String getIsHomemade() {
        return isHomemade;
    }

    public void setIsHomemade(String isHomemade) {
        this.isHomemade = isHomemade;
    }

    public String getaType() {
        return aType;
    }

    public void setaType(String aType) {
        this.aType = aType;
    }

    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }

	public String getPlayStream() {
		return playStream;
	}

	public void setPlayStream(String playStream) {
		this.playStream = playStream;
	}
}
