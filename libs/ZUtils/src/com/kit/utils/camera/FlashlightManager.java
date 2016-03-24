package com.kit.utils.camera;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.IBinder;
import android.util.Log;

import com.kit.utils.ZogUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FlashlightManager {

    private static final String TAG = FlashlightManager.class.getSimpleName();
    private static Camera camera = null;

    private static final Object iHardwareService;
    private static final Method setFlashEnabledMethod;
    private static final Method getFlashEnabledMethod;

    /**
     * Use Static Intialize Object,Setting HardwareService Manager Object and
     * flash method.
     */
    static {
        iHardwareService = getHardwareService();
        setFlashEnabledMethod = getMethod("setFlashlightEnabled",
                iHardwareService, boolean.class);
        getFlashEnabledMethod = getMethod("getFlashlightEnabled",
                iHardwareService, new Class[0]); // here must set null
        if (iHardwareService == null) {
            Log.v(TAG, "This device does supports control of a flashlight");
        } else {
            Log.v(TAG, "This device does not support control of a flashlight");
        }

        camera = getCamera();
    }


    public static Camera getCamera() {
        if (camera != null) {
            return camera;
        } else {
            camera = Camera.open();
        }
        return camera;
    }

    /**
     * Get Hardware Service
     *
     * @return
     */
    private static Object getHardwareService() {

        // Use reflect get system service mamger object
        Class<?> serviceManagerClass = maybeForName("android.os.ServiceManager");
        if (serviceManagerClass == null) {
            return null;
        }

        // Get getService function method object
        Method getServiceMethod = maybeGetMethod(serviceManagerClass,
                "getService", String.class);
        if (getServiceMethod == null) {
            return null;
        }

        Object hardwareService = invoke(getServiceMethod, null, "hardware");
        if (hardwareService == null) {
            return null;
        }

        Class<?> iHardwareServiceStubClass = maybeForName("android.os.IHardwareService$Stub");
        if (iHardwareServiceStubClass == null) {
            return null;
        }

        Method asInterfaceMethod = maybeGetMethod(iHardwareServiceStubClass,
                "asInterface", IBinder.class);
        if (asInterfaceMethod == null) {
            return null;
        }

        return invoke(asInterfaceMethod, null, hardwareService);
    }

    /**
     * Use reflect
     *
     * @param methodName
     * @param iHardwareService
     * @param argClasses
     * @return
     */
    private static Method getMethod(String methodName, Object iHardwareService,
                                    Class<?>... argClasses) {
        if (iHardwareService == null) {
            return null;
        }
        Class<?> proxyClass = iHardwareService.getClass();

        // test
        Method[] methods = proxyClass.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Log.i("method---", methods[i].getName());
        }

        return maybeGetMethod(proxyClass, methodName, argClasses);
    }

    private static Class<?> maybeForName(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException cnfe) {
            // OK
            return null;
        } catch (RuntimeException re) {
            Log.w(TAG, "Unexpected error while finding class " + name, re);
            return null;
        }
    }

    private static Method maybeGetMethod(Class<?> clazz, String name,
                                         Class<?>... argClasses) {
        try {
            return clazz.getMethod(name, argClasses);
        } catch (NoSuchMethodException nsme) {
            // OK
            return null;
        } catch (RuntimeException re) {
            Log.w(TAG, "Unexpected error while finding method " + name, re);
            return null;
        }
    }

    private static Object invoke(Method method, Object instance, Object... args) {
        try {
            return method.invoke(instance, args);
        } catch (IllegalAccessException e) {
            Log.w(TAG, "Unexpected error while invoking " + method, e);
            return null;
        } catch (InvocationTargetException e) {
            Log.w(TAG, "Unexpected error while invoking " + method,
                    e.getCause());
            return null;
        } catch (RuntimeException re) {
            Log.w(TAG, "Unexpected error while invoking " + method, re);
            return null;
        }
    }

    public static void enableFlashlight() {
        setFlashlight(true);
    }

    public static void disableFlashlight() {
        setFlashlight(false);
    }

    /**
     * Set Flahlight if activate
     *
     * @param active
     */
    public static void setFlashlight(boolean active) {

//        LogUtils.printLog(FlashlightManager.class, "iHardwareService:" + iHardwareService
//                + " setFlashEnabledMethod:" + setFlashEnabledMethod
//                + " getFlashEnabledMethod:" + getFlashEnabledMethod);

        ZogUtils.printLog(FlashlightManager.class, "active:" + active);

        if (iHardwareService != null && setFlashEnabledMethod != null
                && getFlashEnabledMethod != null) {
            try {
                //是否可以通过反射来打开手电筒
                Boolean enabled = (Boolean) getFlashEnabledMethod.invoke(
                        iHardwareService, (Object[]) null);

                if (enabled)
                    setFlashEnabledMethod.invoke(iHardwareService, active);
                else
                    setFlashLightNormal(active);

            } catch (Exception e) {
                setFlashLightNormal(active);
            }
        } else {
            setFlashLightNormal(active);
        }
    }

//    private static void setFlashlightConventional(boolean active) {
//        if (camera == null)
//            return;
//
//        Parameters p = camera.getParameters();
//
//        // Set
//        if (active) {
//            p.setFlashMode(Parameters.FLASH_MODE_TORCH);
//            camera.setParameters(p);
//            camera.startPreview(); // 开始亮灯;
//        } else {
//            p.setFlashMode(Parameters.FLASH_MODE_OFF);
//            camera.stopPreview(); // 关掉亮灯
//            camera.release(); // 关掉照相机
//        }
//
//        LogUtils.printLog(FlashlightManager.class, "setFlashlightConventional active:" + active);
//    }


    public static void setFlashLightNormal(boolean active) {

        if (camera == null)
            camera = getCamera();


        Parameters params = camera.getParameters();
        if (active) {
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview(); // 开始亮灯;
        } else {
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.stopPreview(); // 关掉亮灯
            camera.release(); // 关掉照相机
            camera =null;
        }

        ZogUtils.printLog(FlashlightManager.class, "setFlashLightNormal active:" + active);
    }

}
