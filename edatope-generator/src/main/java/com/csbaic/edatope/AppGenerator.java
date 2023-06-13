package com.csbaic.edatope;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class AppGenerator {



    public static void main(String[] args) {

        FastAutoGenerator.create(Constants.DB_URL, Constants.DB_USERNAME, Constants.DB_PASSWORD)
                .globalConfig(builder -> {
                    builder.author("bage") // 设置作者
//                            .fileOverride() // 覆盖已生成文件
                            .outputDir(Constants.PROJECT_ROOT + "\\edatope-app\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(Constants.BASE_PACKAGE + ".app") ;// 设置父包名
                })
                .strategyConfig(builder -> {
                    builder // 设置需要生成的表名
                            .addInclude("sys_project", "sys_block", "sys_enterprise")
                            .addInclude("sys_block_work_stage")
                            .addInclude("sys_device")
                            .addInclude("sys_device_authorize")
                            .addInclude("sys_tech_organization_authorize_city")
//                            .addInclude("sys_acl_menu")
//                            .addInclude("sys_acl_permission")
//                            .addInclude("sys_acl_permission_data_rule")
//                            .addInclude("sys_acl_role")
//                            .addInclude("sys_acl_role_permission")
//                            .addInclude("sys_acl_user_role")
//                            .addInclude("sys_org")
//                            .addInclude("sys_user")
//                            .addInclude("sys_organization")
                            .addTablePrefix("sys_acl", "sys"); // 设置过滤表前缀


                    builder.entityBuilder()
                            .enableColumnConstant()
                            .enableLombok()
                            .disableSerialVersionUID()
                            .addSuperEntityColumns(Constants.SUPER_ENTITY_COLUMNS)
                            .superClass(Constants.SUPER_ENTITY_CLASS);


                })
                .execute();
    }

}