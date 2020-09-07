package com.example.boot.utils.cert;

import java.util.Date;

public class SslCertInfo {
    private Integer certId;  // id

    private String name;  //证书别名

    private String dn;  // DN

    private String algFlag;  // 密钥算法：1. RSA-1024    2. RSA-2048   3. SM2

    private String publicKey;  //公钥数据(Der+Base64)

    private String csr;  //证书请求数据

    private String cert; //证书数据

    private String issueDn;  //证书签发者DN

    private Integer status;  // 1. 有效   2. 已申请，未导入  3. 禁用   4. 过期，使用原密钥更新证书时覆盖记录  5. 删除
    private Date notAfter;  //有效期止

    private Date notBefore;  //有效期起

    //查询开始页
    private int curr=1;
    //每页展示条数
    private int pageSize=20;
    public Integer getCertId() {
        return certId;
    }
    public void setCertId(Integer certId) {
        this.certId = certId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDn() {
        return dn;
    }
    public void setDn(String dn) {
        this.dn = dn;
    }
    public String getAlgFlag() {
        return algFlag;
    }
    public void setAlgFlag(String algFlag) {
        this.algFlag = algFlag;
    }
    public String getPublicKey() {
        return publicKey;
    }
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
    public String getCsr() {
        return csr;
    }
    public void setCsr(String csr) {
        this.csr = csr;
    }
    public String getCert() {
        return cert;
    }
    public void setCert(String cert) {
        this.cert = cert;
    }
    public String getIssueDn() {
        return issueDn;
    }
    public void setIssueDn(String issueDn) {
        this.issueDn = issueDn;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Date getNotAfter() {
        return notAfter;
    }
    public void setNotAfter(Date notAfter) {
        this.notAfter = notAfter;
    }
    public Date getNotBefore() {
        return notBefore;
    }
    public void setNotBefore(Date notBefore) {
        this.notBefore = notBefore;
    }
    public int getCurr() {
        return curr;
    }
    public void setCurr(int curr) {
        this.curr = curr;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
