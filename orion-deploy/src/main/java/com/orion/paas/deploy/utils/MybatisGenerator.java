package com.orion.paas.deploy.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/16 9:50
 */
public class MybatisGenerator {

    public static void main(String[] args) {
        // 全局配置
        GlobalConfig gbConfig = new GlobalConfig()
                // 是否支持AR模式
                .setActiveRecord(true)
                // 设置作者
                .setAuthor("Li Jiahang")
                // 生成路径
                .setOutputDir("D:/MP/")
                // 文件是否覆盖
                .setFileOverride(true)
                // 主键策略
                .setIdType(IdType.AUTO)
                // 映射接口名称
                .setMapperName("%sDAO")
                // 映射文件名称
                .setXmlName("%sMapper")
                // service名称
                .setServiceName("%sService")
                // 业务实现类名称
                .setServiceImplName("%sServiceImpl")
                // controller名称
                .setControllerName("%sController")
                // entity名称
                .setEntityName("%sDO")
                // 是否生成ResultMap
                .setBaseResultMap(true)
                // 是否生成二级缓存
                .setEnableCache(false)
                // 使用 date
                .setDateType(DateType.ONLY_DATE)
                // 生成 swagger2
                .setSwagger2(true)
                // 是否生成SQL片段
                .setBaseColumnList(true);

        // 数据源配置
        DataSourceConfig dsConfig = new DataSourceConfig()
                // 配置数据库类型
                .setDbType(DbType.MYSQL)
                // 配置驱动
                .setDriverName("com.mysql.jdbc.Driver")
                // 配置路径
                .setUrl("jdbc:mysql://localhost:3306/orion-deploy?characterEncoding=utf8")
                // 配置账号
                .setUsername("root")
                // 配置密码
                .setPassword("admin123");

        // 策略配置
        StrategyConfig stConfig = new StrategyConfig()
                // 全局大写命名
                .setCapitalMode(true)
                // 生成实体类注解
                .setEntityTableFieldAnnotationEnable(true)
                // 下滑线转驼峰命名策略
                .setNaming(NamingStrategy.underline_to_camel)
                // 是否生成字段常量
                .setEntityColumnConstant(false)
                // 使用restful的controller
                .setRestControllerStyle(true)
                // 是否为构建者模型
                .setEntityBuilderModel(true)
                // 配置表前缀
                .setTablePrefix("")
                // 生成的表
                .setInclude();

        // 包名策略配置
        PackageConfig pkConfig = new PackageConfig()
                // 声明父包
                .setParent("com.orion.paas.deploy")
                // 映射接口的包
                .setMapper("dao")
                // service接口的包
                .setService("service")
                // serviceImpl接口的包
                .setServiceImpl("service.impl")
                // controller接口的包
                .setController("controller")
                // 实体类的包
                .setEntity("domain")
                // 映射文件的包
                .setXml("mapper");

        // 整合配置
        AutoGenerator ag = new AutoGenerator()
                // 整合全局配置
                .setGlobalConfig(gbConfig)
                // 整合数据源配置
                .setDataSource(dsConfig)
                // 整合表名配置
                .setStrategy(stConfig)
                // 整合包名策略
                .setPackageInfo(pkConfig);

        // 执行
        ag.execute();
    }

}
