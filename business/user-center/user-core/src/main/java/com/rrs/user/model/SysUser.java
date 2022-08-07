package com.rrs.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author hcq
 * @since 2022-07-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    /**
     * ç™»å½•å¯†ç 
     */
    private String password;

    private String nickname;

    private String headImgUrl;

    private String mobile;

    private Boolean sex;

    private Boolean enabled;

    private String type;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String company;

    private String openId;

    private Boolean isDel;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
