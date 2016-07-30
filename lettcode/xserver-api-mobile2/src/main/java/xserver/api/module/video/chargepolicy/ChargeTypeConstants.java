package xserver.api.module.video.chargepolicy;

public class ChargeTypeConstants {

    public interface chargePolicy {

        public static final Integer FREE = 1;// 免费

        public static final Integer CHARGE_BY_CONTENT = 2;// 按内容收费

        public static final Integer FREE_350 = 3;// 350免费

        public static final Integer CHARGE_YUAN_XIAN = 4;// 院线收费

        public static final Integer CHARGE_BY_STREAM = 5;// 按码流收费
    }

    public static final String CHARGE = "1";

    public static final String NOT_CHARGE = "0";
}
