package com.bbs;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.spring.ProcessEngineFactoryBean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 73598.
 * @Date 2018/5/23 0023.
 * @Time 19:05.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class ActivitiTest {

    Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    ProcessEngineFactoryBean processEngine;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IdentityService identityService;

    @Test
    public void deploymentProcessDefinition() {
        Deployment deployment = repositoryService.createDeployment() //创建一个部署对象
                .name("activiti test")//添加部署的名称
                .addClasspathResource("activiti/testBPMN.bpmn")//从classpath的资源中加载，一次只能加载一个文件
                .deploy(); //完成部署
        System.out.println("部署ID:" + deployment.getId());  //1
        System.out.println("部署名称" + deployment.getName()); //helloworld入门程序

    }

    /**
     * 启动流程实例
     **/
    @Test
    public void startProcessInstance() {
        //流程定义的key
        String processDefinitionKey = "myProcess_1";
        //使用流程定义的key启动流程实例,key对应helloworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println("流程实例ID:" + pi.getId());
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
    }

    /**
     * 查询当前人的个人任务
     */
    @Test
    public void findMyPersonalTask() {
        String assignee = "_3";
        List<Task> list = taskService.createTaskQuery()//创建任务查询
                .taskAssignee(assignee)//指定个人任查询，指定办理人
                .list();
        List<String> userIdlist = new ArrayList<String>();
        userIdlist.add("_3");
        list = taskService.createTaskQuery().taskAssigneeIds(userIdlist).list();
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("任务ID:" + task.getId());
                System.out.println("任务名称:" + task.getName());
                System.out.println("任务的创建时间:" + task.getCreateTime());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("流程实例ID:" + task.getProcessInstanceId());
                System.out.println("执行对象ID:" + task.getExecutionId());
                System.out.println("流程定义ID:" + task.getProcessDefinitionId());
                System.out.println("############################################");
            }
        }
    }

    /**
     * 完成我的任务
     */
    @Test
    public void completeMyPersonalTask() {
        //任务ID
        String taskId = "57502";
        taskService.complete(taskId);
        System.out.println("完成任务：任务ID:" + taskId);
    }
}
