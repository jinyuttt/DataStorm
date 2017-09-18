package dataStrom.bus.mq;

public enum RPCCode {
sucess,//任务执行成功
error,//任务错误，执行失败
outTime,//任务超时
request,//刚刚建立监视
busMonitor;//任务还处于监视中
}
