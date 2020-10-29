package com.example.boot.snmp;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.snmp4j.log.ConsoleLogFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;

public class SnmpTest {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

        // TODO Auto-generated method stub
        SnmpTest test = new SnmpTest();

        test.testGet4();




    }

    private Map<String,String> getMap(String des,String oid){
        Map<String,String> temp = new HashMap<>();
        temp.put(des,oid);
        return temp;
    }


    private void testGet1() {
        String ip = "192.168.153.128";
        String community = "public";
        List<Map<String,String>> GetOidMapList = new ArrayList<>();
        System.out.println("系统参数（1.3.6.1.2.1.1）");
        GetOidMapList.add(getMap("获取系统基本信息 SysDesc GET",".1.3.6.1.2.1.1.1.0"));
        GetOidMapList.add(getMap("监控时间 sysUptime GET",".1.3.6.1.2.1.1.3.0"));
        GetOidMapList.add(getMap("系统联系人 sysContact GET",".1.3.6.1.2.1.1.4.0"));
        GetOidMapList.add(getMap("获取机器名 SysName GET",".1.3.6.1.2.1.1.5.0"));
        GetOidMapList.add(getMap("机器所在位置 SysLocation GET",".1.3.6.1.2.1.1.6.0"));
        GetOidMapList.add(getMap(" 机器提供的服务 SysService GET",".1.3.6.1.2.1.1.7.0"));
        int i=1;
        for(Map<String,String> oidMap:GetOidMapList){
            for (Map.Entry<String, String> entry : oidMap.entrySet()) {
                System.out.println(i + entry.getKey());
                SnmpData.snmpGet(ip, community,entry.getValue());
            }
            i++;
        }


        List<Map<String,String>> WALKOidMapList = new ArrayList<>();
        System.out.println("系统参数（1.3.6.1.2.1.1）");
        WALKOidMapList.add(getMap(" 系统运行的进程列表 hrSWRunName WALK",".1.3.6.1.2.1.25.4.2.1.2"));
        WALKOidMapList.add(getMap("系统安装的软件列表 hrSWInstalledName WALK",".1.3.6.1.2.1.25.6.3.1.2"));
        int j=1;
        for(Map<String,String> oidMap:WALKOidMapList){
            for (Map.Entry<String, String> entry : oidMap.entrySet()) {
                System.out.println(j + entry.getKey());
                SnmpData.snmpWalk(ip, community,entry.getValue());
            }
            j++;
        }

    }



//    private void testGet2() {
//        String ip = "192.168.153.128";
//        String community = "public";
//        List<Map<String,String>> GetOidMapList = new ArrayList<>();
//        System.out.println("网络接口（1.3.6.1.2.1.2）");
//        GetOidMapList.add(getMap("网络接口的数目 IfNumber GET",".1.3.6.1.2.1.2.1.0"));
//        GetOidMapList.add(getMap("监控时间 sysUptime GET",".1.3.6.1.2.1.1.3.0"));
//        GetOidMapList.add(getMap("系统联系人 sysContact GET",".1.3.6.1.2.1.1.4.0"));
//        GetOidMapList.add(getMap("获取机器名 SysName GET",".1.3.6.1.2.1.1.5.0"));
//        GetOidMapList.add(getMap("机器坐在位置 SysLocation GET",".1.3.6.1.2.1.1.6.0"));
//        GetOidMapList.add(getMap(" 机器提供的服务 SysService GET",".1.3.6.1.2.1.1.7.0"));
//        int i=1;
//        for(Map<String,String> oidMap:GetOidMapList){
//            for (Map.Entry<String, String> entry : oidMap.entrySet()) {
//                System.out.println(i + entry.getKey());
//                SnmpData.snmpGet(ip, community,entry.getValue());
//            }
//            i++;
//        }
//
//
//        List<Map<String,String>> WALKOidMapList = new ArrayList<>();
//        System.out.println("网络接口（1.3.6.1.2.1.2）");
//        WALKOidMapList.add(getMap("网络接口信息描述 IfDescr WALK",".1.3.6.1.2.1.2.2.1.2"));
//        WALKOidMapList.add(getMap("系统安装的软件列表 hrSWInstalledName WALK",".1.3.6.1.2.1.25.6.3.1.2"));
//        int j=1;
//        for(Map<String,String> oidMap:WALKOidMapList){
//            for (Map.Entry<String, String> entry : oidMap.entrySet()) {
//                System.out.println(j + entry.getKey());
//                SnmpData.snmpWalk(ip, community,entry.getValue());
//            }
//            j++;
//        }
//
//    }

    private void testGet3() {
        String ip = "192.168.153.128";
        String community = "public";
        List<Map<String,String>> GetOidMapList = new ArrayList<>();
        System.out.println("CPU及负载");
        GetOidMapList.add(getMap("用户CPU百分比 ssCpuUser GET",". 1.3.6.1.4.1.2021.11.9.0"));
        GetOidMapList.add(getMap("系统CPU百分比 ssCpuSystem GET",". 1.3.6.1.4.1.2021.11.10.0"));
        GetOidMapList.add(getMap("空闲CPU百分比 ssCpuIdle GET",". 1.3.6.1.4.1.2021.11.11.0"));
        int i=1;
        for(Map<String,String> oidMap:GetOidMapList){
            for (Map.Entry<String, String> entry : oidMap.entrySet()) {
                System.out.println(i + entry.getKey());
                SnmpData.snmpGet(ip, community,entry.getValue());
            }
            i++;
        }


        List<Map<String,String>> WALKOidMapList = new ArrayList<>();
        System.out.println("CPU及负载");
        WALKOidMapList.add(getMap("CPU的当前负载，N个核就有N个负载 hrProcessorLoad WALK",". 1.3.6.1.2.1.25.3.3.1.2"));
        int j=1;
        for(Map<String,String> oidMap:WALKOidMapList){
            for (Map.Entry<String, String> entry : oidMap.entrySet()) {
                System.out.println(j + entry.getKey());
                SnmpData.snmpWalk(ip, community,entry.getValue());
            }
            j++;
        }

    }

    private void testGet4() {
        String ip = "192.168.153.128";
        String community = "public";
        List<Map<String,String>> GetOidMapList = new ArrayList<>();
        System.out.println("内存及磁盘（1.3.6.1.2.1.25）");
        GetOidMapList.add(getMap("获取内存大小 hrMemorySize GET",".1.3.6.1.2.1.25.2.2.0"));
        GetOidMapList.add(getMap("系统CPU百分比 ssCpuSystem GET",". 1.3.6.1.4.1.2021.11.10.0"));
        GetOidMapList.add(getMap("空闲CPU百分比 ssCpuIdle GET",". 1.3.6.1.4.1.2021.11.11.0"));
        int i=1;
        for(Map<String,String> oidMap:GetOidMapList){
            for (Map.Entry<String, String> entry : oidMap.entrySet()) {
                System.out.println(i + entry.getKey());
                SnmpData.snmpGet(ip, community,entry.getValue());
            }
            i++;
        }


        List<Map<String,String>> WALKOidMapList = new ArrayList<>();
        System.out.println("内存及磁盘（1.3.6.1.2.1.25）");
        WALKOidMapList.add(getMap("存储设备编号 hrStorageIndex WALK",".1.3.6.1.2.1.25.2.3.1.1"));
        WALKOidMapList.add(getMap("使用多少，跟总容量相除就是占用率 hrStorageUsed WALK",".1.3.6.1.2.1.25.2.3.1.6"));
        WALKOidMapList.add(getMap("存储设备编号 hrStorageIndex WALK",".1.3.6.1.2.1.25.2.3.1.1"));
        int j=1;
        for(Map<String,String> oidMap:WALKOidMapList){
            for (Map.Entry<String, String> entry : oidMap.entrySet()) {
                System.out.println(j + entry.getKey());
                SnmpData.snmpWalk(ip, community,entry.getValue());
            }
            j++;
        }

    }


    public void testGet() {
        String ip = "192.168.153.128";
        String community = "public";
        String oidval = "1.3.6.1.2.1.1.6.0";
        SnmpData.snmpGet(ip, community, oidval);
    }


    public void testGetList() {
        String ip = "192.168.153.128";
        String community = "public";
        List<String> oidList = new ArrayList<String>();
        oidList.add("1.3.6.1.2.1.1.5.0");
        oidList.add("1.3.6.1.2.1.1.7.0");
        SnmpData.snmpGetList(ip, community, oidList);
    }


    public void testGetAsyList() {
        String ip = "192.168.153.128";
        String community = "public";
        List<String> oidList = new ArrayList<String>();
        oidList.add("1.3.6.1.2.1");
        oidList.add("1.3.6.1.2.12");
        SnmpData.snmpAsynGetList(ip, community, oidList);
        System.out.println("i am first!");
    }


    public void testWalk() {
        String ip = "192.168.153.128";
        String community = "public";
        String targetOid = "1.3.6.1.2.1.1.5.0";
        SnmpData.snmpWalk(ip, community, targetOid);
    }


    public void testAsyWalk() {
        String ip = "192.168.153.128";
        String community = "public";
        // 异步采集数据
        SnmpData.snmpAsynWalk(ip, community, "1.3.6.1.2.1.25.4.2.1.2");
    }


    public void testSetPDU() throws Exception {
        String ip = "192.168.153.128";
        String community = "public";
        SnmpData.setPDU(ip, community, "1.3.6.1.2.1.1.6.0", "jianghuiwen");
    }


    public void testVersion() {
        System.out.println(org.snmp4j.version.VersionInfo.getVersion());
    }

//    系统参数（1.3.6.1.2.1.1）
//
//    OID 描述 备注 请求方式
//
//.1.3.6.1.2.1.1.1.0 获取系统基本信息 SysDesc GET
//
//.1.3.6.1.2.1.1.3.0 监控时间 sysUptime GET
//
//.1.3.6.1.2.1.1.4.0 系统联系人 sysContact GET
//
//.1.3.6.1.2.1.1.5.0 获取机器名 SysName GET
//
//.1.3.6.1.2.1.1.6.0 机器坐在位置 SysLocation GET
//
//.1.3.6.1.2.1.1.7.0 机器提供的服务 SysService GET
//
//.1.3.6.1.2.1.25.4.2.1.2 系统运行的进程列表 hrSWRunName WALK
//
//.1.3.6.1.2.1.25.6.3.1.2 系统安装的软件列表 hrSWInstalledName WALK
//
//
//    网络接口（1.3.6.1.2.1.2）
//
//    OID 描述 备注 请求方式
//
//.1.3.6.1.2.1.2.1.0 网络接口的数目 IfNumber GET
//
//.1.3.6.1.2.1.2.2.1.2 网络接口信息描述 IfDescr WALK
//
//.1.3.6.1.2.1.2.2.1.3 网络接口类型 IfType WALK
//
//.1.3.6.1.2.1.2.2.1.4 接口发送和接收的最大IP数据报[BYTE] IfMTU WALK
//
//.1.3.6.1.2.1.2.2.1.5 接口当前带宽[bps] IfSpeed WALK
//
//.1.3.6.1.2.1.2.2.1.6 接口的物理地址 IfPhysAddress WALK
//
//.1.3.6.1.2.1.2.2.1.8 接口当前操作状态[up|down] IfOperStatus WALK
//
//.1.3.6.1.2.1.2.2.1.10 接口收到的字节数 IfInOctet WALK
//
//.1.3.6.1.2.1.2.2.1.16 接口发送的字节数 IfOutOctet WALK
//
//.1.3.6.1.2.1.2.2.1.11 接口收到的数据包个数 IfInUcastPkts WALK
//
//.1.3.6.1.2.1.2.2.1.17 接口发送的数据包个数 IfOutUcastPkts WALK
//
//
//
//            CPU及负载
//
//    OID 描述 备注 请求方式
//
//. 1.3.6.1.4.1.2021.11.9.0 用户CPU百分比 ssCpuUser GET
//
//            . 1.3.6.1.4.1.2021.11.10.0 系统CPU百分比 ssCpuSystem GET
//
//            . 1.3.6.1.4.1.2021.11.11.0 空闲CPU百分比 ssCpuIdle GET
//
//            . 1.3.6.1.4.1.2021.11.50.0 原始用户CPU使用时间 ssCpuRawUser GET
//
//.1.3.6.1.4.1.2021.11.51.0 原始nice占用时间 ssCpuRawNice GET
//
//            . 1.3.6.1.4.1.2021.11.52.0 原始系统CPU使用时间 ssCpuRawSystem GET
//
//            . 1.3.6.1.4.1.2021.11.53.0 原始CPU空闲时间 ssCpuRawIdle GET
//
//            . 1.3.6.1.2.1.25.3.3.1.2 CPU的当前负载，N个核就有N个负载 hrProcessorLoad WALK
//
//            . 1.3.6.1.4.1.2021.11.3.0 ssSwapIn GET
//
//. 1.3.6.1.4.1.2021.11.4.0 SsSwapOut GET
//
//. 1.3.6.1.4.1.2021.11.5.0 ssIOSent GET
//
//. 1.3.6.1.4.1.2021.11.6.0 ssIOReceive GET
//
//. 1.3.6.1.4.1.2021.11.7.0 ssSysInterrupts GET
//
//. 1.3.6.1.4.1.2021.11.8.0 ssSysContext GET
//
//. 1.3.6.1.4.1.2021.11.54.0 ssCpuRawWait GET
//
//. 1.3.6.1.4.1.2021.11.56.0 ssCpuRawInterrupt GET
//
//. 1.3.6.1.4.1.2021.11.57.0 ssIORawSent GET
//
//. 1.3.6.1.4.1.2021.11.58.0 ssIORawReceived GET
//
//. 1.3.6.1.4.1.2021.11.59.0 ssRawInterrupts GET
//
//. 1.3.6.1.4.1.2021.11.60.0 ssRawContexts GET
//
//. 1.3.6.1.4.1.2021.11.61.0 ssCpuRawSoftIRQ GET
//
//. 1.3.6.1.4.1.2021.11.62.0 ssRawSwapIn. GET
//
//            . 1.3.6.1.4.1.2021.11.63.0 ssRawSwapOut GET
//
//.1.3.6.1.4.1.2021.10.1.3.1 Load5 GET
//
//.1.3.6.1.4.1.2021.10.1.3.2 Load10 GET
//
//.1.3.6.1.4.1.2021.10.1.3.3 Load15 GET
//
//
//    内存及磁盘（1.3.6.1.2.1.25）
//
//    OID 描述 备注 请求方式
//
//.1.3.6.1.2.1.25.2.2.0 获取内存大小 hrMemorySize GET
//
//.1.3.6.1.2.1.25.2.3.1.1 存储设备编号 hrStorageIndex WALK
//
//.1.3.6.1.2.1.25.2.3.1.2 存储设备类型 hrStorageType[OID] WALK
//
//.1.3.6.1.2.1.25.2.3.1.3 存储设备描述 hrStorageDescr WALK
//
//.1.3.6.1.2.1.25.2.3.1.4 簇的大小 hrStorageAllocationUnits WALK
//
//.1.3.6.1.2.1.25.2.3.1.5 簇的的数目 hrStorageSize WALK
//
//.1.3.6.1.2.1.25.2.3.1.6 使用多少，跟总容量相除就是占用率 hrStorageUsed WALK
//
//.1.3.6.1.4.1.2021.4.3.0 Total Swap Size(虚拟内存) memTotalSwap GET
//
//.1.3.6.1.4.1.2021.4.4.0 Available Swap Space memAvailSwap GET
//
//.1.3.6.1.4.1.2021.4.5.0 Total RAM in machine memTotalReal GET
//
//.1.3.6.1.4.1.2021.4.6.0 Total RAM used memAvailReal GET
//
//.1.3.6.1.4.1.2021.4.11.0 Total RAM Free memTotalFree GET
//
//.1.3.6.1.4.1.2021.4.13.0 Total RAM Shared memShared GET
//
//.1.3.6.1.4.1.2021.4.14.0 Total RAM Buffered memBuffer GET
//
//.1.3.6.1.4.1.2021.4.15.0 Total Cached Memory memCached GET
//
//.1.3.6.1.4.1.2021.9.1.2 Path where the disk is mounted dskPath WALK
//
//.1.3.6.1.4.1.2021.9.1.3 Path of the device for the partition dskDevice WALK
//
//.1.3.6.1.4.1.2021.9.1.6 Total size of the disk/partion (kBytes) dskTotal WALK
//
//.1.3.6.1.4.1.2021.9.1.7 Available space on the disk dskAvail WALK
//
//.1.3.6.1.4.1.2021.9.1.8 Used space on the disk dskUsed WALK
//
//.1.3.6.1.4.1.2021.9.1.9 Percentage of space used on disk dskPercent WALK
//
//.1.3.6.1.4.1.2021.9.1.10 Percentage of inodes used on disk dskPercentNode WALK


}
