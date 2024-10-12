package top.twip.blog.constant;

import org.apache.commons.lang.StringUtils;

public enum MemeTypeEnum {
    NO_MEME_TYPE("1", "无分类"),
    SODRICA_MEME_TYPE("2", "万象物语"),
    BROWN_DUST_MEME_TYPE("3", "棕色尘埃");


    private final String value;
    private final String desc;

    MemeTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static MemeTypeEnum getInstanceByValue(String value) {
        if (StringUtils.isNotBlank(value)) {
            MemeTypeEnum[] values = MemeTypeEnum.values();
            for (MemeTypeEnum s : values) {
                if (s.getValue().equals(value)) {
                    return s;
                }
            }
        }
        return NO_MEME_TYPE;
    }

    public static MemeTypeEnum getInstanceByDesc(String desc) {
        if (StringUtils.isNotBlank(desc)) {
            MemeTypeEnum[] values = MemeTypeEnum.values();
            for (MemeTypeEnum s : values) {
                if (s.getDesc().equals(desc)) {
                    return s;
                }
            }
        }
        return NO_MEME_TYPE;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
