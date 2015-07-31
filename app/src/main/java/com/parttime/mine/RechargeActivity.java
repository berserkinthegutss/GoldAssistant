package com.parttime.mine;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.base.LocalInitActivity;
import com.parttime.net.BaseRequest;
import com.parttime.net.Callback;
import com.parttime.widget.SingleSelectLayout;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.volley.VolleySington;
import com.thirdparty.alipay.PayResult;
import com.thirdparty.alipay.SignUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cjz on 2015/7/31.
 */
public class RechargeActivity extends LocalInitActivity {
    @ViewInject(R.id.ssl_amount)
    protected SingleSelectLayout sslAmount;
    @ViewInject(R.id.btn_pay)
    protected Button btnPay;

    private String orderId;


    // �̻�PID
    public static final String PARTNER = "2088811181647667";
    // �̻��տ��˺�
    public static final String SELLER = "510445519@qq.com";
    // �̻�˽Կ��pkcs8��ʽ
    public static final String RSA_PRIVATE = "MIICXQIBAAKBgQDF7XxcSoSA/mB4fkiBuCxFk8XDuviW3le7RALN/fJfuvMQDwkNtfLa1xm4HUlIRL6W+kL6+JHDLvIjE+2+wKTS4//m96NwswZjYUO6mPwIfe5rjXdLIx6qDzmRjvZIbv6BlHsEGgAlq4NLnvEdFbnPVE32UvjB9ajhf7sM+Fs95QIDAQABAoGAb7s0rNTUIA15YAvJ2pChTVWyGl/93Qz+8ZPfEXH91NSwSaxzK+4+fhNXTXwa1lUYUhpMnWicwFZMEkk5uKj/YZ9Ly7uarJd4y4VEay3m1RG3BZJJapyunCNDVdUEv9OEafGpa6TbUNoFXpcVIzUVpTJYGBZX/zI2U60K+pvfvLECQQDpezSrF7ZSfzgqTvaTnAQfyxmnb4ym4lktD83wN7iFUnXQ+8UxL8ns3ZpWOi5T77CteDo3X+s7+s6jspNuD2jTAkEA2QRwtGg+HNgUnVcFmndUYgo/e7Igs85QtFS2vz+7j2kGmd9rihO3aD8qn8smYDBLKLjSSgqPTI9khBRhBl0LZwJBAJKLIB2a/nZ9HxV/BkjTjcsewPVUkGVWgD5GQy3Y61nSzdvjins60XR4CpzAW7+XG79lTLTg4VZ+LyCTvvE/fr0CQG3qEs880OC5DE/YaG0grStut1KGGIwZHcUH9vsMY4myDvbWMthfPhBdldATC1/Cdf6tBU0c5hFHuwgubinT7FcCQQCCA59CGj7rcgpqG/V5sIlLjAHyI4gZDU0cemR/e5vF90ZijNr5x2hOPy7a+A3LgfkaKsN95/+7U2nNVu30QzEU";
    // ֧������Կ
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDF7XxcSoSA/mB4fkiBuCxFk8XDuviW3le7RALN/fJfuvMQDwkNtfLa1xm4HUlIRL6W+kL6+JHDLvIjE+2+wKTS4//m96NwswZjYUO6mPwIfe5rjXdLIx6qDzmRjvZIbv6BlHsEGgAlq4NLnvEdFbnPVE32UvjB9ajhf7sM+Fs95QIDAQAB";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // ֧�������ش˴�֧���������ǩ�������֧����ǩ����Ϣ��ǩԼʱ֧�����ṩ�Ĺ�Կ����ǩ
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // �ж�resultStatus Ϊ��9000�������֧���ɹ�������״̬�������ɲο��ӿ��ĵ�
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // ���÷��������г�ֵ
                        // getServerPayResult();
                        Toast.makeText(RechargeActivity.this, "֧���ɹ�",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // �ж�resultStatus Ϊ�ǡ�9000����������֧��ʧ��
                        // ��8000������֧�������Ϊ֧������ԭ�����ϵͳԭ���ڵȴ�֧�����ȷ�ϣ����ս����Ƿ�ɹ��Է�����첽֪ͨΪ׼��С����״̬��
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(RechargeActivity.this, "֧�����ȷ����",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // ����ֵ�Ϳ����ж�Ϊ֧��ʧ�ܣ������û�����ȡ��֧��������ϵͳ���صĴ���
                            Toast.makeText(RechargeActivity.this, "֧��ʧ��",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(RechargeActivity.this, "�����Ϊ��" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };

    private int rechargeAmount;

    private int[] yuans;
    private String yuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_recharge);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        super.initViews();
        center(R.string.recharge);
        left(TextView.class, R.string.back);
        initRechargeAmount();
    }

    private void initRechargeAmount(){
        String[] amounts = getResources().getStringArray(R.array.recharge_amounts);
        if(amounts != null){
            yuan = getString(R.string.yuan);
            yuans = new int[amounts.length];
            for(int i = 0; i < amounts.length; ++i){
                yuans[i] = Integer.parseInt(amounts[i]);
                sslAmount.add(amounts[i] + yuan);
            }
        }
    }

    protected boolean validate(){
        if(sslAmount.getSelectedindex() < 0){
            showToast(R.string.please_select_recharge_amount);
            return false;
        }
        return true;
    }

    @OnClick(R.id.btn_pay)
    public void pay(View v){
        if(!validate()){
            return;
        }

        showWait(true);
        ;
        rechargeAmount = yuans[sslAmount.getSelectedindex()];
        Map<String, String> params = new HashMap<>();
        params.put("company_id", getCompanyId());
        params.put("charge_money", rechargeAmount + "");
        params.put("charge_type", 0 + "");
        new BaseRequest().request(Url.COMPANY_recharge_lproduct, params, VolleySington.getInstance().getRequestQueue(), new Callback() {
            @Override
            public void success(Object obj) throws JSONException {
                showWait(false);
                JSONObject json = (JSONObject) obj;
                orderId = json.getString("out_trade_no");
                aliPay();
            }

            @Override
            public void failed(Object obj) {
                showWait(false);
            }
        });
    }

    public void aliPay() {
        // ����
        String orderInfo = getOrderInfo(getString(R.string.recharge), getString(R.string.jian_zhe_da_ren_recharge) + rechargeAmount + getString(R.string.yuan), rechargeAmount
                + "");

        // �Զ�����RSA ǩ��
        String sign = sign(orderInfo);
        try {
            // �����sign ��URL����
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // �����ķ���֧���������淶�Ķ�����Ϣ
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // ����PayTask ����
                PayTask alipay = new PayTask(RechargeActivity.this);
                // ����֧���ӿڣ���ȡ֧�����
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // �����첽����
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * sign the order info. �Զ�����Ϣ����ǩ��
     *
     * @param content
     *            ��ǩ��������Ϣ
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. ��ȡǩ����ʽ
     *
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * create the order info. ����������Ϣ
     *
     */
    public String getOrderInfo(String subject, String body, String price) {
        // ǩԼ���������ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // ǩԼ����֧�����˺�
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // �̻���վΨһ������
        orderInfo += "&out_trade_no=" + "\"" + orderId + "\"";

        // ��Ʒ����
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // ��Ʒ����
        orderInfo += "&body=" + "\"" + body + "\"";

        // ��Ʒ���
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // �������첽֪ͨҳ��·��
        orderInfo += "&notify_url=" + "\""
                + Url.COMPANY_recharge_AliPayAsynNotify + "\"";

        // ����ӿ����ƣ� �̶�ֵ
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // ֧�����ͣ� �̶�ֵ
        orderInfo += "&payment_type=\"1\"";

        // �������룬 �̶�ֵ
        orderInfo += "&_input_charset=\"utf-8\"";

        // ����δ����׵ĳ�ʱʱ��
        // Ĭ��30���ӣ�һ����ʱ���ñʽ��׾ͻ��Զ����رա�
        // ȡֵ��Χ��1m��15d��
        // m-���ӣ�h-Сʱ��d-�죬1c-���죨���۽��׺�ʱ����������0��رգ���
        // �ò�����ֵ������С���㣬��1.5h����ת��Ϊ90m��
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_tokenΪ���������Ȩ��ȡ����alipay_open_id,���ϴ˲����û���ʹ����Ȩ���˻�����֧��
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // ֧��������������󣬵�ǰҳ����ת���̻�ָ��ҳ���·�����ɿ�
        orderInfo += "&return_url=\"m.alipay.com\"";

        // �������п�֧���������ô˲���������ǩ���� �̶�ֵ ����ҪǩԼ���������п����֧��������ʹ�ã�
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    @Override
    protected ViewGroup getLeftWrapper() {
        return null;
    }

    @Override
    protected ViewGroup getRightWrapper() {
        return null;
    }

    @Override
    protected TextView getCenter() {
        return null;
    }
}
