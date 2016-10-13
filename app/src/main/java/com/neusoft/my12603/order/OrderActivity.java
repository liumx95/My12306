package com.neusoft.my12603.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.karics.library.zxing.android.CaptureActivity;
import com.neusoft.my12603.R;

import java.util.HashMap;

/**
 * Created by 明星 on 2016/9/12.
 */
public class OrderActivity extends Activity {
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);
        try {
            initView();


        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void initView() throws WriterException {

        Button btn_wm = (Button) findViewById(R.id.btn_wm);
        final ImageButton image = new ImageButton(OrderActivity.this);
        //将资源文件转换成Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.yang);
        image.setImageBitmap(addLogo(makeQr(400, 400), bitmap));
        btn_wm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(OrderActivity.this)
                        .setTitle("查看我二维码")
                        .setView(image)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);

            }
        });
    }

    private Bitmap makeQr(int width, int height) throws WriterException {
        HashMap<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //图像数据转换，使用了矩阵转换
        String url = "山有木兮木有枝，心悦君兮君不知";
        BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, width
                , height, hints);
        //创建一个像素数组
        int[] pixels = new int[bitMatrix.getWidth() * bitMatrix.getHeight()];
        //逐个生成二维码图片
        for (int y = 0; y < bitMatrix.getHeight(); y++) {
            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * bitMatrix.getWidth() + x] = 0xff0000ff;
                } else {
                    pixels[y * bitMatrix.getHeight() + x] = 0xffffffff;
                }

            }
        }
        //生产二维码图片，使用ARGB
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        //offset偏移量，stride=宽度，

        return bitmap;
    }

    //在二维码中添加logo图案
    public static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
        if (logo == null) {
            return null;
        }
        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }
        //logo大小为二维码整体的大小1/6
        float scaleFactor = srcWidth * 1.0f / 6 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(src, 0, 0, null);
        canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
        canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bitmap;
    }

    //
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);

                Log.v("hehe", "解码结果:" + content);
            }
        }
    }
}