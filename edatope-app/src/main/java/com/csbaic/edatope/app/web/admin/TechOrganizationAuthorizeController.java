package com.csbaic.edatope.app.web.admin;


import com.csbaic.edatope.app.model.command.*;
import com.csbaic.edatope.app.model.dto.*;
import com.csbaic.edatope.app.model.query.OrganizationQuery;
import com.csbaic.edatope.app.service.IOrganizationService;
import com.csbaic.edatope.app.service.IRoleService;
import com.csbaic.edatope.app.service.ITechOrganizationAuthorizeService;
import com.csbaic.edatope.app.service.IUserService;
import com.csbaic.edatope.app.service.impl.TechOrganizationService;
import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * 技术单位授权 前端控制器
 * </p>
 *
 * @author bage
 * @since 2022-01-24
 */
@RestController
@Api(tags = "技术单位授权")
@RequestMapping("/api/v1/tech-organization-authorize")
public class TechOrganizationAuthorizeController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private ITechOrganizationAuthorizeService authorizeService;

    @ApiPermission("sys:tech-organization-authorize:create")
    @ApiOperation("创建授权")
    @PostMapping("/create")
    public Result create(@Validated @RequestBody CreateOrgAuthorizeCmd cmd) {
        authorizeService.create(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:tech-organization-authorize:view")
    @ApiOperation("授权技术单位列表")
    @GetMapping("/listAll")
    public Result<List<TechOrganizationAuthorizeDTO>> listAll( ) {
        return Result.ok(
                authorizeService.listAll()
        );
    }

    @ApiPermission("sys:tech-organization-authorize:update")
    @ApiOperation("启用or停用授权")
    @PostMapping("/updateAuthorizeStatus")
    public Result<List<TechOrganizationAuthorizeDTO>> updateAuthorizeStatus(@Validated @RequestBody UpdateAuthorizeStatusCmd cmd) {
        authorizeService.updateAuthorizeStatus(cmd);
        return Result.ok(
        );
    }

    @ApiPermission("sys:tech-organization-authorize:update")
    @ApiOperation("更新授权信息")
    @PostMapping("/update")
    public Result update(@Validated @RequestBody UpdateOrgAuthorizeCmd cmd) {
        authorizeService.update(cmd);
        return Result.ok();
    }


    @ApiPermission("sys:tech-organization-authorize:delete")
    @ApiOperation("删除授权")
    @PostMapping("/delete")
    public Result delete(@RequestBody @Validated DeleteOrgAuthorizeCmd cmd) {
        authorizeService.delete(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:tech-organization-authorize:view")
    @ApiOperation("单位列表(下拉选择)")
    @GetMapping("/organization/list")
    public Result<List<OrganizationDTO>> orgList() {
        return Result.ok(
                organizationService.listTechOrgAll()
        );
    }

    @ApiPermission("sys:tech-organization-authorize:view")
    @ApiOperation("技术负责人(下拉选择)")
    @GetMapping("/principal/list")
    public Result<List<UserDTO>> principal(@NotBlank(message = "单位id不能为空") @RequestParam("orgId") String orgId, @RequestParam(value = "name", required = false) String name) {
        return Result.ok(
                userService.listAllUserByOrgIdLikeName(orgId, name)
        );
    }

    @ApiPermission("sys:tech-organization-authorize:view")
    @ApiOperation("角色(下拉选择)")
    @GetMapping("/role/list")
    public Result<List<RoleDTO>> roleList() {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        if (StringUtils.isEmpty(details.getOrgId())) {
            throw BizRuntimeException.from(ResultCode.ERROR, "当前用户没有单位信息，无法获取角色");
        }
        return Result.ok(
                roleService.listRoleByOrgId(details.getOrgId())
        );
    }

    @ApiPermission("sys:tech-organization-authorize:view")
    @ApiOperation("分配地区(下拉选择)")
    @GetMapping("/area/list")
    public Result<DictDTO> areaList() {
        return Result.ok(
                authorizeService.listAreaByOrgId()
        );
    }

}

