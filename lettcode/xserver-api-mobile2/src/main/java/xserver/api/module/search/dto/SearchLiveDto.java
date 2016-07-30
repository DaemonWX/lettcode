package xserver.api.module.search.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xserver.api.dto.BaseDto;
import xserver.api.module.superlive.SuperLiveConstant;
import xserver.api.module.superlive.dto.StreamDto;
import xserver.lib.constant.StreamConstants;
import xserver.lib.tp.search.response.SearchLiveTp;
import xserver.lib.tp.search.response.SearchLiveTp.BranchData;
import xserver.lib.tp.search.response.SearchLiveTp.MultiProgram;
import xserver.lib.tp.search.response.SearchLiveTp.MusicLiveInfo;
import xserver.lib.tp.search.response.SearchLiveTp.ProgramInfo;
import xserver.lib.tp.search.response.SearchLiveTp.SearchLiveProgramResponse;
import xserver.lib.tp.search.response.SearchLiveTp.SuperStream;
import xserver.lib.util.CalendarUtil;
import xserver.lib.util.MessageUtils;

public class SearchLiveDto extends BaseDto {

    private final static String LIVE_PLAY_STATUS_NOT_START = "notstart";
    private final static String LIVE_PLAY_STATUS_PLAYING = "play";
    private final static String LIVE_PLAY_STATUS_END = "end";

    public final static String LIVE_SOURCE_ID_LUNBO = "7"; // 轮播数据类型的sourceId
    public final static String LIVE_SOURCE_ID_WEISHI = "2"; // 卫视台数据类型的sourceId

    /**
     *
     */
    private static final long serialVersionUID = 5722345035494411769L;

    /**
     * 1：点播关联直播；
     * 2：轮播台、卫视台；
     * 3：体育频道；
     * 4：世界杯比赛信息；
     * 5：世界杯分组信息；
     * 6：音乐频道、娱乐频道
     */
    private Integer type;

    private String channel;

    private String sport;

    private String gameName;

    private String season;

    private String id;

    private String channelId;

    private String imgUrl;

    private String nameCn;

    private String nameEn;

    private String sourceId;

    private String liveUrl;

    private String categoryName;
    private String categoryId;
    private String liveTypeName;

    private List<ProgramInfo> programInfos;

    private List<SearchLiveProgramDto> games;

    private List<Music> play_list; // 娱乐，音乐直播数据

    private List<StreamDto> superStream;
    private Map<String, String> defaultLogo;
    private String satelliteTvType;
    private String channelClass;
    private String favorite;
    private String streamTips; // 盗播流提示
    private List<String> splatids; // 版权方 例如1036(超级live);1048(超级live后门)
    private String numericKeys; // 台号

    
    public String getNumericKeys() {
		return numericKeys;
	}

	public void setNumericKeys(String numericKeys) {
		this.numericKeys = numericKeys;
	}

	public List<String> getSplatids() {
        return splatids;
    }

    public void setSplatids(List<String> splatids) {
        this.splatids = splatids;
    }
    public String getStreamTips() {
        return streamTips;
    }

    public void setStreamTips(String streamTips) {
        this.streamTips = streamTips;
    }

    public SearchLiveDto() {
    }

    public SearchLiveDto(SearchLiveTp liveTp) {
        if (liveTp != null) {
            this.channel = liveTp.getChannel();
            this.gameName = liveTp.getGameName();
            this.season = liveTp.getSeason();
            this.sport = liveTp.getSport();
            this.type = liveTp.getType();
            this.id = liveTp.getId();
            this.channelId = liveTp.getChannelId();
            this.imgUrl = liveTp.getImgUrl();
            this.nameCn = liveTp.getNameCn();
            this.nameEn = liveTp.getNameEn();
            this.liveUrl = liveTp.getDefaultLiveUrl();
            this.sourceId = liveTp.getSourceId();
            this.categoryName = liveTp.getCategoryName();
            this.liveTypeName = liveTp.getLiveTypeName();
            this.splatids = liveTp.getSplatids();
            this.numericKeys = liveTp.getNumericKeys();
            if (liveTp.getDefaultLogo() != null) {
                defaultLogo = new HashMap<String, String>();
                int i = 1;
                for (String name : liveTp.getDefaultLogo().keySet()) {
                    defaultLogo.put("pic" + (i++), liveTp.getDefaultLogo().get(name));
                }
            }
            this.satelliteTvType = liveTp.getSatelliteTvType();
            if ("2".equals(liveTp.getSourceId())) { // 卫视台
                this.categoryId = liveTp.getSourceId() + liveTp.getSatelliteTvType();
            } else if ("7".equals(liveTp.getSourceId())) { // 轮播
                this.categoryId = liveTp.getSourceId() + liveTp.getChannelClass();
            } else { // 其他类型归属于直播
                this.categoryId = SuperLiveConstant.SUPERLIVE_CATEGORY_LIVE.getCode();
            }

            // 盗播流提示
            if (SearchLiveTp.HIDDEN_PROGRAM_TIPS_NO.equals(liveTp.getIsHiddenProgSrc())) {
                this.streamTips = liveTp.getProgramSource();
            }

            this.programInfos = liveTp.getProgramInfos();
            filterProgramInfo();
            this.games = this.parseGames(liveTp.getGames());
            this.play_list = this.parseMusic(liveTp.getPlay_list());
            this.superStream = this.parseSuperStream(liveTp.getSuper_streams());
        }
    }

    private void filterProgramInfo() {
        if (this.programInfos != null && this.programInfos.size() > 0) {
            List<ProgramInfo> tempList = new ArrayList<SearchLiveTp.ProgramInfo>();
            // 节目单只取当前直播的节目以及下一个节目
            Date date = new Date();
            String day = new SimpleDateFormat("yyyy-MM-dd").format(date);
            for (ProgramInfo program : this.programInfos) {
                if (StringUtils.isNotBlank(program.getDisplayStartTime())) {
                    try {
                        int dur = Integer.parseInt(program.getDuration());
                        String time = day + " " + program.getDisplayStartTime() + ":00";
                        Date programDate = CalendarUtil.parseDate(time, CalendarUtil.SIMPLE_DATE_FORMAT);
                        if (programDate != null && (programDate.getTime() + dur * 1000) > date.getTime()) {
                            tempList.add(program);
                        }
                    } catch (Exception e) {
                        continue;
                    }
                    if (tempList.size() == 2) {
                        break;
                    }
                }
            }
            this.programInfos = tempList;
        }
    }

    private List<StreamDto> parseSuperStream(List<SuperStream> superStreamTpList) {
        List<StreamDto> superList = null;
        if (superStreamTpList != null && superStreamTpList.size() > 0) {
            superList = new LinkedList<StreamDto>();
            StreamDto info = null;
            for (SuperStream stream : superStreamTpList) {
                info = new StreamDto();
                String sCode = stream.getRateType().replace("flv_", "");
                info.setRateType(stream.getRateType());
                info.setStreamId(stream.getStreamId());
                info.setStreamName(stream.getStreamName());
                info.setStreamUrl(stream.getStreamUrl());
                info.setCode(sCode);
                info.setCode(sCode);
                if (StringUtils.isNotEmpty(MessageUtils.getMessage(StreamConstants.STREAM_CODE_NAME_MAP.get(sCode),
                        "zh_cn"))) {
                    info.setName(MessageUtils.getMessage(StreamConstants.STREAM_CODE_NAME_MAP.get(sCode), "zh_cn"));
                } else {
                    info.setName(StreamConstants.STREAM_CODE_NAME_MAP.get(sCode));
                }
                superList.add(info);
            }
            if (superList != null) {
                Collections.sort(superList);
            }
        }

        return superList;
    }

    private List<Music> parseMusic(List<MusicLiveInfo> musicLiveTpList) {
        List<Music> liveList = new LinkedList<SearchLiveDto.Music>();
        if (musicLiveTpList != null && musicLiveTpList.size() > 0) {
            Music music = null;
            long now = System.currentTimeMillis();
            for (MusicLiveInfo liveInfo : musicLiveTpList) {
                music = new Music();
                try {
                    long startTime = CalendarUtil.parseDate(liveInfo.getBeginTime(), CalendarUtil.SIMPLE_DATE_FORMAT)
                            .getTime();
                    long endTime = CalendarUtil.parseDate(liveInfo.getEndTime(), CalendarUtil.SIMPLE_DATE_FORMAT)
                            .getTime();
                    // 过滤3天以前的直播
                    // if ((now - startTime) > 259200000L) {
                    // continue;
                    // }

                    if (startTime > now) {
                        music.setPlayStatus(LIVE_PLAY_STATUS_NOT_START);
                    } else if (endTime < now) {
                        music.setPlayStatus(LIVE_PLAY_STATUS_END);
                    } else {
                        music.setPlayStatus(LIVE_PLAY_STATUS_PLAYING);
                    }
                } catch (Exception e) {
                    continue;
                }

                music.setBeginTime(liveInfo.getBeginTime());
                music.setEndTime(liveInfo.getEndTime());
                music.setDt(liveInfo.getDt());
                music.setGlobalId(liveInfo.getGlobal_id());
                music.setId(liveInfo.getId());
                music.setLiveUrl(liveInfo.getLive_url());
                music.setLiveUrlRate(liveInfo.getLive_url_rate());
                music.setLiveUrl_1300(liveInfo.getLive_url_1300());
                music.setLiveUrlRate_1300(liveInfo.getLive_url_1300_rate());
                music.setLiveUrl_720p(liveInfo.getLive_url_720p());
                music.setLiveUrlRate_720p(liveInfo.getLive_url_720p_rate());
                music.setPlayDate(liveInfo.getPlayDate());
                music.setPlayUrl(liveInfo.getPlayUrl());
                music.setPreVid(liveInfo.getPreVid());
                music.setRecordingId(liveInfo.getRecordingId());
                music.setRelevanceStar(liveInfo.getRelevanceStar());
                music.setStatus(liveInfo.getStatus());
                music.setTitle(liveInfo.getTitle());
                music.setVid(liveInfo.getVid());
                music.setPlatform(liveInfo.getPlatform());
                music.setPayPlatForm(liveInfo.getPayPlatForm());
                music.setScreenings(liveInfo.getScreenings());
                if (liveInfo.getPayPlatForm() != null && liveInfo.getPayPlatForm().contains("1036")) { // 1036代表付费平台为超级live
                    music.setIsPay(1);
                } else {
                    music.setIsPay(0);
                }
                music.setSuperStream(parseSuperStream(liveInfo.getSuper_streams()));
                if (liveInfo.getDefaultLogo() != null) {
                    Map<String, String> map = new HashMap<String, String>();
                    int i = 1;
                    for (String name : liveInfo.getDefaultLogo().keySet()) {
                        map.put("pic" + (i++), liveInfo.getDefaultLogo().get(name));
                    }
                    music.setDefaultLogo(map);
                }
                music.setSplatids(liveInfo.getSplatids());
                // 处理多视角,当前的直播放在多视角的中间位置
                if (music.getMultiProgram() != null) {
                    MultiProgramDto multiProgramDto = this.parseMultiProgram(liveInfo.getMultiProgram());
                    if (multiProgramDto != null && multiProgramDto.getBranches() != null
                            && multiProgramDto.getBranches().size() > 0) {
                        BranchDto branchDto = new BranchDto();
                        branchDto.setChannelId(music.getId());
                        branchDto.setChannelName(music.getTitle());
                        if (music.getDefaultLogo() != null) {
                            branchDto.setChannelPic(music.getDefaultLogo().get("pic1"));
                        }
                        branchDto.setStreams(music.getSuperStream());
                        multiProgramDto.getBranches().add(multiProgramDto.getBranches().size() / 2, branchDto);
                        music.setMultiProgram(multiProgramDto);
                    }
                }

                liveList.add(music);
            }

        }
        return liveList;
    }

    private List<SearchLiveProgramDto> parseGames(List<SearchLiveProgramResponse> gameList) {
        List<SearchLiveProgramDto> gameDtoList = null;
        if (gameList != null && gameList.size() > 0) {
            SearchLiveProgramDto gameDto = null;
            gameDtoList = new ArrayList<SearchLiveProgramDto>();
            long now = System.currentTimeMillis();
            // 对比赛进行过滤,只给出开始时间距现在最近的的比赛
            SearchLiveProgramResponse closestGame = null;
            long closetTime = Long.MAX_VALUE;
            for (SearchLiveProgramResponse resp : gameList) {
                SimpleDateFormat sdf = new SimpleDateFormat(CalendarUtil.SIMPLE_DATE_FORMAT);
                try {
                    Date d = sdf.parse(resp.getMatchStartTime());
                    long misTime = Math.abs(now - d.getTime());
                    if (misTime < closetTime || closestGame == null) {
                        closestGame = resp;
                        closetTime = misTime;
                    }
                } catch (Exception e) {
                    continue;
                }

            }

            if (closestGame != null) {
                gameDto = new SearchLiveProgramDto();
                gameDto.setDisplayEndTime(closestGame.getDisplayEndTime());
                gameDto.setDisplayStartTime(closestGame.getDisplayStartTime());
                gameDto.setEndTime(closestGame.getEndTime());
                gameDto.setId(closestGame.getId());
                gameDto.setPlayDate(closestGame.getPlayDate());
                gameDto.setPlayUrl(closestGame.getPlayUrl());
                gameDto.setPreVid(closestGame.getPreVid());
                gameDto.setRecordingId(closestGame.getRecordingId());
                gameDto.setStartTime(closestGame.getStartTime());
                gameDto.setTitle(closestGame.getTitle());
                gameDto.setVid(closestGame.getVid());
                gameDto.setGroup(closestGame.getGroup());
                gameDto.setGuestScore(closestGame.getGuestScore());
                gameDto.setGuestTeam(closestGame.getGuestTeam());
                gameDto.setHomeScore(closestGame.getHomeScore());
                gameDto.setHomeTeam(closestGame.getHomeTeam());
                gameDto.setIsVs(closestGame.getIsVs());
                gameDto.setPlace(closestGame.getPlace());
                gameDto.setStage(closestGame.getStage());
                gameDto.setGuestImgUrl(closestGame.getGuestImgUrl());
                gameDto.setHomeImgUrl(closestGame.getHomeImgUrl());
                gameDto.setMatch(closestGame.getMatch());
                gameDto.setPlatform(closestGame.getPlatform());
                gameDto.setMatchStage(closestGame.getMatchStage());
                gameDto.setMatchStartTime(closestGame.getMatchStartTime());
                gameDto.setMatchEndTime(closestGame.getMatchEndTime());
                gameDto.setLiveUrl(closestGame.getLive_url());
                gameDto.setLiveUrl_1080p3m(closestGame.getLive_url_1080p3m());
                gameDto.setLiveUrl_1300(closestGame.getLive_url_1300());
                gameDto.setLiveUrl_720p(closestGame.getLive_url_720p());
                gameDto.setLiveUrlRate(closestGame.getLive_url_rate());
                gameDto.setLiveUrlRate_1080p3m(closestGame.getLive_url_1080p3m_rate());
                gameDto.setLiveUrlRate_1300(closestGame.getLive_url_1300_rate());
                gameDto.setLiveUrlRate_720p(closestGame.getLive_url_720p_rate());
                gameDto.setSuperStream(parseSuperStream(closestGame.getSuper_streams()));
                gameDto.setScreenings(closestGame.getScreenings());
                gameDto.setPayPlatForm(closestGame.getPayPlatForm());
                if (closestGame.getPayPlatForm() != null && closestGame.getPayPlatForm().contains("1036")) { // 1036代表付费平台是超级live,临时写死
                    gameDto.setIsPay(1);
                } else {
                    gameDto.setIsPay(0);
                }
                if (closestGame.getDefaultLogo() != null) {
                    Map<String, String> map = new HashMap<String, String>();
                    int i = 1;
                    for (String name : closestGame.getDefaultLogo().keySet()) {
                        map.put("pic" + (i++), closestGame.getDefaultLogo().get(name));
                    }
                    gameDto.setDefaultLogo(map);
                }

                try {
                    long startTime = CalendarUtil.parseDate(closestGame.getMatchStartTime(),
                            CalendarUtil.SIMPLE_DATE_FORMAT).getTime();
                    long endTime = CalendarUtil.parseDate(closestGame.getMatchEndTime(),
                            CalendarUtil.SIMPLE_DATE_FORMAT).getTime();
                    if (startTime > now) {
                        gameDto.setPlayStatus(LIVE_PLAY_STATUS_NOT_START);
                    } else if (endTime < now) {
                        gameDto.setPlayStatus(LIVE_PLAY_STATUS_END);
                    } else {
                        gameDto.setPlayStatus(LIVE_PLAY_STATUS_PLAYING);
                    }

                } catch (Exception e) {
                }
                gameDto.setSplatids(closestGame.getSplatids());

                // 处理多视角,当前的直播放在多视角的中间位置
                if (closestGame.getMultiProgram() != null) {
                    MultiProgramDto multiProgramDto = this.parseMultiProgram(closestGame.getMultiProgram());
                    if (multiProgramDto != null && multiProgramDto.getBranches() != null
                            && multiProgramDto.getBranches().size() > 0) {
                        BranchDto branchDto = new BranchDto();
                        branchDto.setChannelId(gameDto.getId());
                        branchDto.setChannelName(gameDto.getTitle());
                        if (gameDto.getDefaultLogo() != null) {
                            branchDto.setChannelPic(gameDto.getDefaultLogo().get("pic1"));
                        }
                        branchDto.setStreams(gameDto.getSuperStream());
                        multiProgramDto.getBranches().add(multiProgramDto.getBranches().size() / 2, branchDto);
                        gameDto.setMultiProgram(multiProgramDto);
                    }
                }

                gameDtoList.add(gameDto);
            }

        }
        return gameDtoList;
    }

    public MultiProgramDto parseMultiProgram(MultiProgram multiProgram) {
        if (multiProgram != null && multiProgram.getBranches() != null && multiProgram.getBranches().size() > 0) {
            MultiProgramDto multiProgramDto = new MultiProgramDto();
            multiProgramDto.setBranchDesc(multiProgram.getBranchDesc());
            List<BranchDto> branchList = new ArrayList<SearchLiveDto.BranchDto>();
            multiProgramDto.setBranches(branchList);
            for (BranchData branchData : multiProgram.getBranches()) {
                BranchDto branchDto = new BranchDto();
                branchDto.setChannelId(branchData.getChannelId());
                branchDto.setChannelName(branchData.getTitle());
                branchDto.setChannelEname(branchData.getChannel_ename());
                branchDto.setChannelPic(branchData.getViewImg());
                branchDto.setStreams(parseSuperStream(branchData.getSuper_streams()));
                branchList.add(branchDto);
            }
            return multiProgramDto;
        }
        return null;
    }

    public String getLiveUrl() {
        return this.liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNameCn() {
        return this.nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return this.nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getSourceId() {
        return this.sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getChannel() {
        return this.channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSport() {
        return this.sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getGameName() {
        return this.gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getSeason() {
        return this.season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public List<SearchLiveProgramDto> getGames() {
        return this.games;
    }

    public void setGames(List<SearchLiveProgramDto> games) {
        this.games = games;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public static class SearchLiveProgramDto implements Serializable {

        /**
         *
         */
        private static final long serialVersionUID = -6802154253285761838L;
        private String displayEndTime;
        private String displayStartTime;
        private Integer endTime;
        private String id;
        private String playDate;
        private String playUrl;
        private String preVid;
        private String recordingId;
        private Integer startTime;
        private String title;
        private String vid;
        private String group;
        private String guestScore;
        private String guestTeam;
        private String homeScore;
        private String homeTeam;
        private String isVs;
        private String place;
        private String stage;
        private String guestImgUrl;
        private String homeImgUrl;
        private String match;
        private String platform;

        private Integer matchStage;
        private String matchStartTime;
        private String matchEndTime;

        private String liveUrl;
        private String liveUrlRate;
        private String liveUrl_1080p3m;
        private String liveUrlRate_1080p3m;
        private String liveUrl_1300;
        private String liveUrlRate_1300;
        private String liveUrl_720p;
        private String liveUrlRate_720p;
        private List<StreamDto> superStream;
        private String playStatus; // 直播状态
        private Map<String, String> defaultLogo;
        private String payPlatForm; // 付费平台
        private Integer isPay; // 是否付费
        private String screenings; // 直播场次id
        private List<String> splatids;
        private MultiProgramDto multiProgram; // 多视角

        public MultiProgramDto getMultiProgram() {
            return multiProgram;
        }

        public void setMultiProgram(MultiProgramDto multiProgram) {
            this.multiProgram = multiProgram;
        }

        public List<String> getSplatids() {
            return splatids;
        }

        public void setSplatids(List<String> splatids) {
            this.splatids = splatids;
        }

        public String getScreenings() {
            return screenings;
        }

        public void setScreenings(String screenings) {
            this.screenings = screenings;
        }

        public Integer getIsPay() {
            return isPay;
        }

        public void setIsPay(Integer isPay) {
            this.isPay = isPay;
        }

        public String getPayPlatForm() {
            return payPlatForm;
        }

        public void setPayPlatForm(String payPlatForm) {
            this.payPlatForm = payPlatForm;
        }

        public Map<String, String> getDefaultLogo() {
            return defaultLogo;
        }

        public void setDefaultLogo(Map<String, String> defaultLogo) {
            this.defaultLogo = defaultLogo;
        }

        public String getPlayStatus() {
            return playStatus;
        }

        public void setPlayStatus(String playStatus) {
            this.playStatus = playStatus;
        }

        public List<StreamDto> getSuperStream() {
            return superStream;
        }

        public void setSuperStream(List<StreamDto> superStream) {
            this.superStream = superStream;
        }

        public String getLiveUrl() {
            return this.liveUrl;
        }

        public void setLiveUrl(String liveUrl) {
            this.liveUrl = liveUrl;
        }

        public String getLiveUrlRate() {
            return this.liveUrlRate;
        }

        public void setLiveUrlRate(String liveUrlRate) {
            this.liveUrlRate = liveUrlRate;
        }

        public String getLiveUrl_1080p3m() {
            return this.liveUrl_1080p3m;
        }

        public void setLiveUrl_1080p3m(String liveUrl_1080p3m) {
            this.liveUrl_1080p3m = liveUrl_1080p3m;
        }

        public String getLiveUrlRate_1080p3m() {
            return this.liveUrlRate_1080p3m;
        }

        public void setLiveUrlRate_1080p3m(String liveUrlRate_1080p3m) {
            this.liveUrlRate_1080p3m = liveUrlRate_1080p3m;
        }

        public String getLiveUrl_1300() {
            return this.liveUrl_1300;
        }

        public void setLiveUrl_1300(String liveUrl_1300) {
            this.liveUrl_1300 = liveUrl_1300;
        }

        public String getLiveUrlRate_1300() {
            return this.liveUrlRate_1300;
        }

        public void setLiveUrlRate_1300(String liveUrlRate_1300) {
            this.liveUrlRate_1300 = liveUrlRate_1300;
        }

        public String getLiveUrl_720p() {
            return this.liveUrl_720p;
        }

        public void setLiveUrl_720p(String liveUrl_720p) {
            this.liveUrl_720p = liveUrl_720p;
        }

        public String getLiveUrlRate_720p() {
            return this.liveUrlRate_720p;
        }

        public void setLiveUrlRate_720p(String liveUrlRate_720p) {
            this.liveUrlRate_720p = liveUrlRate_720p;
        }

        public Integer getMatchStage() {
            return this.matchStage;
        }

        public void setMatchStage(Integer matchStage) {
            this.matchStage = matchStage;
        }

        public String getMatchStartTime() {
            return this.matchStartTime;
        }

        public void setMatchStartTime(String matchStartTime) {
            this.matchStartTime = matchStartTime;
        }

        public String getMatchEndTime() {
            return this.matchEndTime;
        }

        public void setMatchEndTime(String matchEndTime) {
            this.matchEndTime = matchEndTime;
        }

        public String getGroup() {
            return this.group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getGuestScore() {
            return this.guestScore;
        }

        public void setGuestScore(String guestScore) {
            this.guestScore = guestScore;
        }

        public String getGuestTeam() {
            return this.guestTeam;
        }

        public void setGuestTeam(String guestTeam) {
            this.guestTeam = guestTeam;
        }

        public String getHomeScore() {
            return this.homeScore;
        }

        public void setHomeScore(String homeScore) {
            this.homeScore = homeScore;
        }

        public String getHomeTeam() {
            return this.homeTeam;
        }

        public void setHomeTeam(String homeTeam) {
            this.homeTeam = homeTeam;
        }

        public String getIsVs() {
            return this.isVs;
        }

        public void setIsVs(String isVs) {
            this.isVs = isVs;
        }

        public String getPlace() {
            return this.place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getStage() {
            return this.stage;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }

        public String getDisplayEndTime() {
            return this.displayEndTime;
        }

        public void setDisplayEndTime(String displayEndTime) {
            this.displayEndTime = displayEndTime;
        }

        public String getDisplayStartTime() {
            return this.displayStartTime;
        }

        public void setDisplayStartTime(String displayStartTime) {
            this.displayStartTime = displayStartTime;
        }

        public Integer getEndTime() {
            return this.endTime;
        }

        public void setEndTime(Integer endTime) {
            this.endTime = endTime;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPlayDate() {
            return this.playDate;
        }

        public void setPlayDate(String playDate) {
            this.playDate = playDate;
        }

        public String getPlayUrl() {
            return this.playUrl;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }

        public String getPreVid() {
            return this.preVid;
        }

        public void setPreVid(String preVid) {
            this.preVid = preVid;
        }

        public String getRecordingId() {
            return this.recordingId;
        }

        public void setRecordingId(String recordingId) {
            this.recordingId = recordingId;
        }

        public Integer getStartTime() {
            return this.startTime;
        }

        public void setStartTime(Integer startTime) {
            this.startTime = startTime;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVid() {
            return this.vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public String getGuestImgUrl() {
            return this.guestImgUrl;
        }

        public void setGuestImgUrl(String guestImgUrl) {
            this.guestImgUrl = guestImgUrl;
        }

        public String getHomeImgUrl() {
            return this.homeImgUrl;
        }

        public void setHomeImgUrl(String homeImgUrl) {
            this.homeImgUrl = homeImgUrl;
        }

        public String getMatch() {
            return this.match;
        }

        public void setMatch(String match) {
            this.match = match;
        }

        public String getPlatform() {
            return this.platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }
    }

    public List<ProgramInfo> getProgramInfos() {
        return this.programInfos;
    }

    public void setProgramInfos(List<ProgramInfo> programInfos) {
        this.programInfos = programInfos;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLiveTypeName() {
        return this.liveTypeName;
    }

    public void setLiveTypeName(String liveTypeName) {
        this.liveTypeName = liveTypeName;
    }

    public List<Music> getPlay_list() {
        return play_list;
    }

    public void setPlay_list(List<Music> play_list) {
        this.play_list = play_list;
    }

    public List<StreamDto> getSuperStream() {
        return superStream;
    }

    public void setSuperStream(List<StreamDto> superStream) {
        this.superStream = superStream;
    }

    public Map<String, String> getDefaultLogo() {
        return defaultLogo;
    }

    public void setDefaultLogo(Map<String, String> defaultLogo) {
        this.defaultLogo = defaultLogo;
    }

    public String getSatelliteTvType() {
        return satelliteTvType;
    }

    public void setSatelliteTvType(String satelliteTvType) {
        this.satelliteTvType = satelliteTvType;
    }

    public static class Music {
        private String globalId; // 节目唯一标识（搜索用于记录点击日志，各端日志需记录）
        private String dt; // 数据类型（搜索用于记录点击日志，各端日志需记录
        private String playDate; // 播放日期
        private String relevanceStar; // 关联明星(optional,有些数据有，有些为空。例如：娱乐数据没有，音乐数据部分有)
        private String playUrl; // 播放地址(optional,有些数据有，有些为空。例如：娱乐数据没有，音乐数据部分有)
        private String status; // 播放状态
        private String beginTime; // 开始时间
        private String endTime; // 结束时间
        private String recordingId; // 录制ID
        private String title; // 节目标题;
        private String preVid; // 预告视频id
        private String vid; // 视频ID;
        private String id; // 数据ID，主键
        private String platform; // 播放平台 PC TV等
        private String liveUrl; // 直播流地址
        private String liveUrlRate;
        private String liveUrl_1300;
        private String liveUrlRate_1300;
        private String liveUrl_720p;
        private String liveUrlRate_720p;
        private List<StreamDto> superStream;
        private String playStatus;
        private Map<String, String> defaultLogo;
        private String payPlatForm; // 付费平台
        private Integer isPay; // 是否付费
        private String screenings; // 直播场次id
        private List<String> splatids;
        private MultiProgramDto multiProgram; // 多视角

        public MultiProgramDto getMultiProgram() {
            return multiProgram;
        }

        public void setMultiProgram(MultiProgramDto multiProgram) {
            this.multiProgram = multiProgram;
        }

        public List<String> getSplatids() {
            return splatids;
        }

        public void setSplatids(List<String> splatids) {
            this.splatids = splatids;
        }

        public String getScreenings() {
            return screenings;
        }

        public void setScreenings(String screenings) {
            this.screenings = screenings;
        }

        public Integer getIsPay() {
            return isPay;
        }

        public void setIsPay(Integer isPay) {
            this.isPay = isPay;
        }

        public String getPayPlatForm() {
            return payPlatForm;
        }

        public void setPayPlatForm(String payPlatForm) {
            this.payPlatForm = payPlatForm;
        }

        public Map<String, String> getDefaultLogo() {
            return defaultLogo;
        }

        public void setDefaultLogo(Map<String, String> defaultLogo) {
            this.defaultLogo = defaultLogo;
        }

        public String getPlayStatus() {
            return playStatus;
        }

        public void setPlayStatus(String playStatus) {
            this.playStatus = playStatus;
        }

        public List<StreamDto> getSuperStream() {
            return superStream;
        }

        public void setSuperStream(List<StreamDto> superStream) {
            this.superStream = superStream;
        }

        public String getGlobalId() {
            return this.globalId;
        }

        public void setGlobalId(String globalId) {
            this.globalId = globalId;
        }

        public String getDt() {
            return this.dt;
        }

        public void setDt(String dt) {
            this.dt = dt;
        }

        public String getPlayDate() {
            return this.playDate;
        }

        public void setPlayDate(String playDate) {
            this.playDate = playDate;
        }

        public String getRelevanceStar() {
            return this.relevanceStar;
        }

        public void setRelevanceStar(String relevanceStar) {
            this.relevanceStar = relevanceStar;
        }

        public String getPlayUrl() {
            return this.playUrl;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getBeginTime() {
            return this.beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getEndTime() {
            return this.endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getRecordingId() {
            return this.recordingId;
        }

        public void setRecordingId(String recordingId) {
            this.recordingId = recordingId;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPreVid() {
            return this.preVid;
        }

        public void setPreVid(String preVid) {
            this.preVid = preVid;
        }

        public String getVid() {
            return this.vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getLiveUrl() {
            return this.liveUrl;
        }

        public void setLiveUrl(String liveUrl) {
            this.liveUrl = liveUrl;
        }

        public String getLiveUrlRate() {
            return this.liveUrlRate;
        }

        public void setLiveUrlRate(String liveUrlRate) {
            this.liveUrlRate = liveUrlRate;
        }

        public String getLiveUrl_1300() {
            return this.liveUrl_1300;
        }

        public void setLiveUrl_1300(String liveUrl_1300) {
            this.liveUrl_1300 = liveUrl_1300;
        }

        public String getLiveUrlRate_1300() {
            return this.liveUrlRate_1300;
        }

        public void setLiveUrlRate_1300(String liveUrlRate_1300) {
            this.liveUrlRate_1300 = liveUrlRate_1300;
        }

        public String getLiveUrl_720p() {
            return this.liveUrl_720p;
        }

        public void setLiveUrl_720p(String liveUrl_720p) {
            this.liveUrl_720p = liveUrl_720p;
        }

        public String getLiveUrlRate_720p() {
            return this.liveUrlRate_720p;
        }

        public void setLiveUrlRate_720p(String liveUrlRate_720p) {
            this.liveUrlRate_720p = liveUrlRate_720p;
        }

    }

    public static class SuperStreamInfo {
        private String rateType;
        private String streamId;
        private String streamName;
        private String streamUrl;

        public String getRateType() {
            return rateType;
        }

        public void setRateType(String rateType) {
            this.rateType = rateType;
        }

        public String getStreamId() {
            return streamId;
        }

        public void setStreamId(String streamId) {
            this.streamId = streamId;
        }

        public String getStreamName() {
            return streamName;
        }

        public void setStreamName(String streamName) {
            this.streamName = streamName;
        }

        public String getStreamUrl() {
            return streamUrl;
        }

        public void setStreamUrl(String streamUrl) {
            this.streamUrl = streamUrl;
        }

    }

    static class MultiProgramDto {
        private String branchDesc;
        private List<BranchDto> branches;

        public String getBranchDesc() {
            return branchDesc;
        }

        public void setBranchDesc(String branchDesc) {
            this.branchDesc = branchDesc;
        }

        public List<BranchDto> getBranches() {
            return branches;
        }

        public void setBranches(List<BranchDto> branches) {
            this.branches = branches;
        }
    }

    static class BranchDto {
        private String channelId;
        private String channelName;
        private String channelPic;
        private String channelEname;
        private List<StreamDto> streams;

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public String getChannelPic() {
            return channelPic;
        }

        public void setChannelPic(String channelPic) {
            this.channelPic = channelPic;
        }

        public String getChannelEname() {
            return channelEname;
        }

        public void setChannelEname(String channelEname) {
            this.channelEname = channelEname;
        }

        public List<StreamDto> getStreams() {
            return streams;
        }

        public void setStreams(List<StreamDto> streams) {
            this.streams = streams;
        }
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getChannelClass() {
        return channelClass;
    }

    public void setChannelClass(String channelClass) {
        this.channelClass = channelClass;
    }

}
