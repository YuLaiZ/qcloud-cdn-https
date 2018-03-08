package com.yulaiz.https.qcloud.cdn.response;

/**
 * Created by YuLai on 2018/3/5.
 *
 * @see <a href="https://cloud.tencent.com/document/product/228/12965">HTTPS 配置</a>
 */
public class PushHttpsRes {
    /**
     * 公共错误码，0表示成功，其他值表示失败
     * 详见错误码页面公共错误码
     *
     * @see <a href="https://cloud.tencent.com/document/api/228/5078#1.-.E5.85.AC.E5.85.B1.E9.94.99.E8.AF.AF.E7.A0.81">公共错误码</a>
     */
    private int code;
    /**
     * 模块错误信息描述，与接口相关。
     */
    private String message;
    /**
     * 英文错误信息，或业务侧错误码
     * 详见错误码页面业务错误码
     *
     * @see <a href="https://cloud.tencent.com/document/product/228/5078#2.-.E6.A8.A1.E5.9D.97.E9.94.99.E8.AF.AF.E7.A0.81">业务错误码</a>
     */
    private String codeDesc;
    /**
     * 返回结果数据
     */
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PushHttpsRes{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", codeDesc='" + codeDesc + '\'' +
                ", data=" + data +
                '}';
    }
}
