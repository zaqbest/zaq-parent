package com.zaqbest.base.workflow.config;

import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class ActivitiConfig {

	// 流程配置，与spring整合，采用SpringProcessEngineConfiguration实现
	@Bean
	public ProcessEngineConfiguration processEngineConfiguration(DataSource dataSource,
			PlatformTransactionManager transactionManager) {
		SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
		processEngineConfiguration.setDataSource(dataSource);
		/**
		 * public static final String DB_SCHEMA_UPDATE_FALSE = "false"; // 不能自动创建表，需要表存在
		 * public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop"; // 先删除表再创建表
		 * public static final String DB_SCHEMA_UPDATE_TRUE = "true"; // 如果表不存在，自动创建表
		 */
		processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		processEngineConfiguration.setDatabaseType("mysql");

		processEngineConfiguration.setTransactionManager(transactionManager);

		// 流程图字体
		processEngineConfiguration.setActivityFontName("宋体");
		processEngineConfiguration.setAnnotationFontName("宋体");
		processEngineConfiguration.setLabelFontName("宋体");

		return processEngineConfiguration;
	}

	// 流程引擎，与spring整合使用factoryBean
	@Bean
	public ProcessEngineFactoryBean processEngine(ProcessEngineConfiguration processEngineConfiguration) {
		ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
		processEngineFactoryBean
				.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);
		return processEngineFactoryBean;
	}

	// 八大接口
	@Bean
	public RepositoryService repositoryService(ProcessEngine processEngine) {
		return processEngine.getRepositoryService();
	}

	@Bean
	public RuntimeService runtimeService(ProcessEngine processEngine) {
		return processEngine.getRuntimeService();
	}

	@Bean
	public TaskService taskService(ProcessEngine processEngine) {
		return processEngine.getTaskService();
	}

	@Bean
	public HistoryService historyService(ProcessEngine processEngine) {
		return processEngine.getHistoryService();
	}

	@Bean
	public FormService formService(ProcessEngine processEngine) {
		return processEngine.getFormService();
	}

	@Bean
	public IdentityService identityService(ProcessEngine processEngine) {
		return processEngine.getIdentityService();
	}

	@Bean
	public ManagementService managementService(ProcessEngine processEngine) {
		return processEngine.getManagementService();
	}

	@Bean
	public DynamicBpmnService dynamicBpmnService(ProcessEngine processEngine) {
		return processEngine.getDynamicBpmnService();
	}

}
