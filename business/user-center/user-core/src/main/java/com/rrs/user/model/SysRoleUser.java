package com.rrs.user.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
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
public class SysRoleUser extends Model<SysRoleUser> {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private Integer roleId;


    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

}
