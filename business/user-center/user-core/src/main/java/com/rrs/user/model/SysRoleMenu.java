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
public class SysRoleMenu extends Model<SysRoleMenu> {

    private static final long serialVersionUID = 1L;

    private Integer roleId;

    private Integer menuId;


    @Override
    protected Serializable pkVal() {
        return this.roleId;
    }

}
