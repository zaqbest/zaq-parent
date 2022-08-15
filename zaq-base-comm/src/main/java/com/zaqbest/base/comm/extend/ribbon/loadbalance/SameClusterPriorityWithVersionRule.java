package com.zaqbest.base.comm.extend.ribbon.loadbalance;

import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.ribbon.ExtendBalancer;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 小工匠
 * @version 1.0
 * @description: 金丝雀版本权重负载均衡策略
 * @date 2022/2/3 13:43
 * @mark: show me the code , change the world
 *
 * 修改by lipan：
 *  - 适配版本2.2.5
 *  - 支持服务指定版本
 *  - 精简代码逻辑
 */

@Slf4j
public class SameClusterPriorityWithVersionRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties discoveryProperties;

    @Autowired
    private NacosServiceManager nacosServiceManager;

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    @Override
    public Server choose(Object key) {

        try {
            //获取本地所部署集群的名称 NJ-CLUSTER
            String localClusterName = discoveryProperties.getClusterName();

            String currentVersion = getServiceVersion(discoveryProperties);

            //去nacos上获取和本地 相同集群   相同版本的所有实例信息
            Instance clusterInst = getInstancesWithVersion(discoveryProperties, currentVersion, true);

            if (clusterInst != null){
                log.info("同集群同版本调用--->当前微服务所在集群:{},被调用微服务所在集群:{},当前微服务的版本:{},被调用微服务版本:{},Host:{},Port:{}",
                        localClusterName,clusterInst.getClusterName(),discoveryProperties.getMetadata().get("currentVersion"),
                        clusterInst.getMetadata().get("currentVersion"),clusterInst.getIp(),clusterInst.getPort());
            } else {
                clusterInst = getInstancesWithVersion(discoveryProperties, currentVersion, false);

                if (clusterInst == null) {
                    log.info("跨集群调用找不到对应合适的版本当前版本为:currentVersion:{}", currentVersion);
                    throw new RuntimeException("找不到相同版本的微服务实例");
                }

                log.info("跨集群同版本调用--->当前微服务所在集群:{},被调用微服务所在集群:{},当前微服务的版本:{},被调用微服务版本:{},Host:{},Port:{}",
                        localClusterName ,clusterInst.getClusterName(),discoveryProperties.getMetadata().get("currentVersion"),
                        clusterInst.getMetadata().get("currentVersion"),clusterInst.getIp(),clusterInst.getPort());
            }

            return new NacosServer(clusterInst);
        } catch (NacosException e) {
            log.error("同集群优先权重负载均衡算法选择异常:{}",e);
            return null;
        }
    }

    private String getServiceVersion(NacosDiscoveryProperties discoveryProperties){
        //尝试获取服务的指定版本
        String currentVersion = discoveryProperties.getMetadata().get(StrUtil.format("{}.currentVersion", ((BaseLoadBalancer)this.getLoadBalancer()).getName()));

        //当前的版本号
        if (StrUtil.isEmpty(currentVersion)){
            currentVersion = discoveryProperties.getMetadata().get("currentVersion");
        }

        return currentVersion;
    }


    /**
     * 方法实现说明:获取相同集群下,相同版本的 所有实例
     * @author:smlz
     * @param discoveryProperties nacos的配置
     * @return: List
     * @exception: NacosException
     */
    private Instance getInstancesWithVersion(NacosDiscoveryProperties discoveryProperties, String currentVersion, Boolean mustSameCluster) throws NacosException {

        //当前的集群的名称
        String currentClusterName = discoveryProperties.getClusterName();

        //获取所有实例的信息(包括不同集群的,不同版本号的)
        List<Instance> allInstance =  getAllInstances(discoveryProperties);

        List<Instance> theSameClusterNameAndTheSameVersionInstList = new ArrayList<>();

        if (StrUtil.isNotEmpty(currentVersion)) {
            //过滤相同集群  同版本号的实例
            for (Instance instance : allInstance) {
                if ((mustSameCluster && StringUtils.endsWithIgnoreCase(instance.getClusterName(), currentClusterName)) &&
                        StringUtils.endsWithIgnoreCase(instance.getMetadata().get("currentVersion"), currentVersion)) {

                    theSameClusterNameAndTheSameVersionInstList.add(instance);
                }
            }
        }

        return  ExtendBalancer.getHostByRandomWeight2(theSameClusterNameAndTheSameVersionInstList);
    }

    /**
     * 方法实现说明:获取被调用服务的所有实例
     * @author:smlz
     * @param discoveryProperties nacos的配置
     * @return: List
     * @exception: NacosException
     */
    private List<Instance> getAllInstances(NacosDiscoveryProperties discoveryProperties) throws NacosException {

        //第1步:获取一个负载均衡对象
        BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) getLoadBalancer();

        //第2步:获取当前调用的微服务的名称
        String invokedSerivceName = baseLoadBalancer.getName();

        //第3步:获取nacos clinet的服务注册发现组件的api
        NamingService namingService = nacosServiceManager.getNamingService(discoveryProperties.getNacosProperties());

        //第4步:获取所有的服务实例
        List<Instance> allInstance =  namingService.getAllInstances(invokedSerivceName, discoveryProperties.getGroup());

        return allInstance;
    }
}