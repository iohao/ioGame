package com.iohao.game.action.skeleton.i18n;

import com.iohao.game.action.skeleton.toy.IoGameBanner;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author 渔民小镇
 * @date 2024-10-02
 * @since 21.18
 */
public class BundleTest {

    @Test
    public void getMessage() {
        var b1 = ResourceBundle.getBundle(Bundle.baseName, Locale.getDefault());
        var b2 = ResourceBundle.getBundle(Bundle.baseName, Locale.getDefault());

        Assert.assertEquals(b1, b2);

        extracted();

        Locale.setDefault(Locale.US);
        extracted();
    }

    private static void extracted() {
        System.out.println("------------------");

        Bundle.bundle = null;

        System.out.println(Locale.getDefault().toString());

        String value = Bundle.getMessage(MessageKey.printActionKitPrintClose);
        IoGameBanner.println(value + " BarSkeletonBuilder.setting.printRunners");
        Assert.assertNotNull(value);
    }
}