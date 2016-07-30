package xserver.api.module.video.dto;

import xserver.lib.mysql.table.BaseData;

/**
 * 半屏播放页 点赞、点踩 返回结果类
 *
 */
public class LikeDto extends BaseData {
	private static final long serialVersionUID = 1449209074271424203L;

	/**
	 * 返回点赞、点踩状态 true：点赞点踩成功 false：失败
	 */
	private Boolean status;

	/**
	 * 点赞的数量
	 */
	private Long likeNumber;

	/**
	 * 点踩的数量
	 */
	private Long unLikeNumber;

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Long getLikeNumber() {
		return likeNumber;
	}

	public void setLikeNumber(Long likeNumber) {
		this.likeNumber = likeNumber;
	}

	public Long getUnLikeNumber() {
		return unLikeNumber;
	}

	public void setUnLikeNumber(Long unLikeNumber) {
		this.unLikeNumber = unLikeNumber;
	}

}
