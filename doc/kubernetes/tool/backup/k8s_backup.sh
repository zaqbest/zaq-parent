#!/bin/bash

#############################################
# 备份k8s 指定namespace
# 依赖工具：kubectl yq awk sed
#############################################
BACKUP_BASE=`date +%Y%m%d%H%M%S`

export NS=aireport

DEPLOY=`kubectl -n $NS get deploy | sed '1d' |  awk -F' ' '{ print $1}'`
STATEFULSET=`kubectl -n $NS get sts | sed '1d' |  awk -F' ' '{ print $1}'`
SERVICE=`kubectl -n $NS get svc | sed '1d' |  awk -F' ' '{ print $1}'`
CONFIGMAP=`kubectl -n $NS get cm | sed '1d' |  awk -F' ' '{ print $1}'`
SECRET=`kubectl -n $NS get secret | sed '1d' |  awk -F' ' '{ print $1}'`
SA=`kubectl -n $NS get sa | sed '1d' |  awk -F' ' '{ print $1}'`
ROLE=`kubectl -n $NS get role | sed '1d' |  awk -F' ' '{ print $1}'`
ROLEBINDING=`kubectl -n $NS get rolebinding | sed '1d' |  awk -F' ' '{ print $1}'`

if [ ! -e $BACKUP_BASE ];then
    mkdir $BACKUP_BASE
    mkdir $BACKUP_BASE/deploy
    mkdir $BACKUP_BASE/svc
    mkdir $BACKUP_BASE/sts
    mkdir $BACKUP_BASE/cm
    mkdir $BACKUP_BASE/secret
    mkdir $BACKUP_BASE/sa
    mkdir $BACKUP_BASE/role
    mkdir $BACKUP_BASE/rolebinding
fi

printf "导出DEPLOY START\n"
for deploy in $DEPLOY
do
  kubectl get deployment $deploy -n $NS -o yaml  > $BACKUP_BASE/deploy/"${deploy}".yaml;
done
printf "导出DEPLOY END\n"

printf "导出SVC START\n"
for svc in $SERVICE
do
  kubectl get svc $svc -n $NS -o yaml  > $BACKUP_BASE/svc/"${svc}".yaml;
done
printf "导出SVC END\n"

printf "导出STS START\n"
for sts in $STATEFULSET
do
  kubectl get sts $sts -n $NS -o yaml  > $BACKUP_BASE/sts/"${sts}".yaml;
done
printf "导出STS END\n"

printf "导出CM START\n"
for cm in $CONFIGMAP
do
  kubectl get cm $cm -n $NS -o yaml  > $BACKUP_BASE/cm/"${cm}".yaml;
done
printf "导出CM END\n"

printf "导出SECRET START\n"
for secret in $SECRET
do
  kubectl get secret $secret -n $NS -o yaml  > $BACKUP_BASE/secret/"${secret}".yaml;
done
printf "导出SECRET END\n"

printf "导出SA START\n"
for sa in $SA
do
  kubectl get sa $sa -n $NS -o yaml  > $BACKUP_BASE/sa/"${sa}".yaml;
done
printf "导出SA END\n"

printf "导出ROLEBINDING START\n"
for rb in $ROLEBINDING
do
  kubectl get rolebinding $rb -n $NS -o yaml  > $BACKUP_BASE/rolebinding/"${rb}".yaml;
done
printf "导出ROLEBINDING END\n"

printf "导出ROLE START\n"
for role in $ROLE
do
  kubectl get role $role -n $NS -o yaml  > $BACKUP_BASE/role/"${role}".yaml;
done
printf "导出ROLE END\n"


printf "删除yaml无用节点 START\n"
for object in $(find $BACKUP_BASE -name '*.yaml')
do
  objectName=$object
  yq eval 'del(.metadata.annotations)'  $object > ${object}.1
  object=${object}.1
  yq eval 'del(.metadata.creationTimestamp)' $object > ${object}.1
  object=${object}.1
  yq eval 'del(.metadata.generation)' $object > ${object}.1
  object=${object}.1
  yq eval 'del(.metadata.resourceVersion)' $object > ${object}.1
  object=${object}.1
  yq eval 'del(.metadata.selfLink)' $object > ${object}.1
  object=${object}.1
  yq eval 'del(.metadata.uid)' $object > ${object}.1
  object=${object}.1
  yq eval 'del(.status)' $object > ${object}.1
  object=${object}.1
  yq eval 'del(.spec.clusterIP)' $object > ${object}.1
  object=${object}.1
  yq eval 'del(.spec.clusterIPs)' $object > $objectName
done
printf "删除yaml无用节点 END\n"

find $BACKUP_BASE -name "*.1" -delete