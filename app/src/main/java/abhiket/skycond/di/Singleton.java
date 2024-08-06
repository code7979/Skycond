package abhiket.skycond.di;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

public class Singleton {
    private final AppModule appModule;
    private static volatile Singleton instance;

    private Singleton(Context context) {
        this.appModule = new AppModule(context);
    }

    public static Singleton getInstance(@NotNull Context context) {
        Singleton result = instance;
        if (result == null) {
            synchronized (Singleton.class) {
                result = instance;
                if (result == null) {
                    instance = result = new Singleton(context);
                }
            }
        }
        return result;
    }

    public AppModule getAppModule() {
        return this.appModule;
    }

}