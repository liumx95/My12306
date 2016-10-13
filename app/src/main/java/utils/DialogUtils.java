package utils;

import android.content.DialogInterface;

import java.lang.reflect.Field;

public class DialogUtils {
	public static void setClosable(DialogInterface dialog, boolean b) {
		Field field;
		try {
			field = dialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, b);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
}
