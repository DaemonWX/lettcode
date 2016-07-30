package xserver.api.module.subject.dto;

import java.util.List;

public class SubjectDto {

    private String name;

    private String pic;

    private String desc;

    private List<Block> block;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Block> getBlock() {
        return block;
    }

    public void setBlock(List<Block> block) {
        this.block = block;
    }
}
