package com.csbaic.edatope;

public interface Constants {

//    String PROJECT_ROOT = "C:\\Users\\yjwfn\\Desktop\\project\\edatope";
    String PROJECT_ROOT = "/Users/wuhao/JavaProjects/土壤/edatope-backend";

    String DB_URL = "jdbc:mysql://rm-uf6b97nql9v25p8t8eo.mysql.rds.aliyuncs.com:3306/edatope?serverTimezone=Asia/Shanghai";

    String DB_USERNAME = "edatope";

    String DB_PASSWORD = "Edatope@2021";

    String BASE_PACKAGE = "com.csbaic.edatope";

    String SUPER_ENTITY_CLASS = "com.csbaic.edatope.common.persistence.entity.BaseEntity";

    String[] SUPER_ENTITY_COLUMNS = new String[]{
            "id", "update_by", "create_by", "update_time", "create_time", "deleted"
    } ;


}
